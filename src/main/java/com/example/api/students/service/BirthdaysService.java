package com.example.api.students.service;

import com.example.api.students.dto.BirthdayDTO;
import com.example.api.students.model.Birthday;
import com.example.api.students.model.Role;
import com.example.api.students.repository.BirthdayRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.MonthDay;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Бизнес-логика приложения
 */
@Service
public class BirthdaysService {

    private final BirthdayRepository birthdayRepository;

    public BirthdaysService(BirthdayRepository birthdayRepository) {
        this.birthdayRepository = birthdayRepository;
    }

    /**
     * Получить запись о дне рождения по его идентификатору
     *
     * @param id айди записи
     */
    public BirthdayDTO getBirthdayById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Введите айди записи.");
        }
        return birthdayRepository.findById(id).map(BirthdaysService::buildDto).orElseGet(BirthdayDTO::new);
    }

    /**
     * Получить все дни рождения
     *
     * @return список дней рождений
     */
    public List<BirthdayDTO> getAllBirthdays() {
        return birthdayRepository.findAll()
                .stream()
                .map(BirthdaysService::buildDto)
                .collect(Collectors.toList());
    }

    /**
     * Получить дни рождения в ближайшие 14 дней включительно
     *
     * @return список дней рождений
     */
    public List<BirthdayDTO> getUpcomingBirthdays() {
        return birthdayRepository.findAll()
                .stream()
                .filter(entity -> {
                    LocalDate currentDay = LocalDate.now();
                    //левая граница интервала
                    MonthDay currentMonthDay = MonthDay.from(currentDay);
                    //правая граница интервала
                    MonthDay currentMonthDayPlus14 = MonthDay.from(currentDay.plusDays(14));

                    MonthDay birthMonthDay = MonthDay.from(entity.getDate());
                    //если дата находится после левой границы интервала включительно
                    if (currentMonthDay.equals(birthMonthDay) || birthMonthDay.isAfter(currentMonthDay)) {
                        // дата находится до правой границы интервала включительно?
                        return currentMonthDayPlus14.equals(birthMonthDay) || birthMonthDay.isBefore(currentMonthDayPlus14);
                    }
                    return false;
                })
                .map(BirthdaysService::buildDto)
                .collect(Collectors.toList());
    }

    /**
     * Добавить новую запись о дне рождения
     *
     * @param newBirthday запись о дне рождения
     */
    public void addBirthday(BirthdayDTO newBirthday) {
        checkMainInfo(newBirthday);

        Birthday birthdayRecord = new Birthday();
        birthdayRecord.setName(newBirthday.getName());
        birthdayRecord.setDate(LocalDate.parse(newBirthday.getDate()));
        birthdayRecord.setRole(Role.fromValue(newBirthday.getRole()));
        addPhoto(newBirthday, birthdayRecord);

        birthdayRepository.save(birthdayRecord);
    }

    /**
     * Изменить запись о дне рождения
     *
     * @param newBirthday запись о дне рождения
     *
     * @return измененная запись
     */
    public Birthday editBirthday(BirthdayDTO newBirthday) {
        checkMainInfo(newBirthday);

        Birthday birthdayRecord = birthdayRepository
                .findById(newBirthday.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Запись с id %s не найдена!", newBirthday.getId())));
        birthdayRecord.setName(newBirthday.getName());
        birthdayRecord.setDate(LocalDate.parse(newBirthday.getDate()));
        birthdayRecord.setRole(Role.fromValue(newBirthday.getRole()));
        addPhoto(newBirthday, birthdayRecord);

        return birthdayRepository.save(birthdayRecord);
    }


    /**
     * Удалить запись о дне рождения по его идентификатору
     *
     * @param birthdayId айди записи
     */
    public void deleteById(Long birthdayId) {
        if (birthdayId == null) {
            throw new IllegalArgumentException("Введите айди записи.");
        }
        birthdayRepository
                .findById(birthdayId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Запись с id %s не найдена!", birthdayId)));

        birthdayRepository.deleteById(birthdayId);
    }

    /**
     * Логика по добавлению фото
     *
     * @param newBirthday    новые данные
     * @param birthdayRecord куда сохраняем
     */
    private static void addPhoto(BirthdayDTO newBirthday, Birthday birthdayRecord) {
        if (newBirthday.getPhoto() != null) {
            try {
                byte[] decode = newBirthday.getPhoto().split(",")[1].getBytes(StandardCharsets.UTF_8);
                birthdayRecord.setPhoto(decode);
            } catch (Exception e) {
                throw new IllegalArgumentException("Загружен некорректный файл.");
            }
        }
    }

    /**
     * Логика по получению фото
     *
     * @param mainRecord откуда достаем
     * @param dto        куда передаем
     */
    private static void getPhoto(Birthday mainRecord, BirthdayDTO dto) {
        if (mainRecord.getPhoto() != null) {
            try {
                dto.setPhoto("data:image/png;base64," + new String(mainRecord.getPhoto(), StandardCharsets.UTF_8));
            } catch (Exception e) {
                throw new IllegalArgumentException("Что-то пошло не так при выгрузке фото из базы данных.");
            }
        }
    }

    /**
     * Проверки введенных с презентации данных на корректность (ФИО, роль, дата рождения)
     *
     * @param checkBirthday данные с презентации для проверки
     *
     * @throws IllegalArgumentException ошибка
     */
    private static void checkMainInfo(BirthdayDTO checkBirthday) throws IllegalArgumentException {
        if (checkBirthday.getName() == null) {
            throw new IllegalArgumentException("Имя не введено.");
        }
        if (Role.fromValue(checkBirthday.getRole()) == null) {
            throw new IllegalArgumentException("Неверно введена роль человека. Возможные варианты: FRIEND FAMILIAR COWORKER FAMILY");
        }
        LocalDate date;
        try {
            date = LocalDate.parse(checkBirthday.getDate());
        } catch (Exception e) {
            throw new IllegalArgumentException("Дата не введена или введена некорректно! Требуется: гггг-мм-дд");
        }
        if (date.isAfter(LocalDate.now()) || date.isBefore(LocalDate.now().minusYears(100))) {
            throw new IllegalArgumentException("Введена некорректная дата рождения.");
        }
    }

    /**
     * Превратить модель в DTO
     *
     * @param birthday модель
     *
     * @return Data Transfer Object
     */
    private static BirthdayDTO buildDto(Birthday birthday) {
        BirthdayDTO birthdayDTO = new BirthdayDTO();
        birthdayDTO.setId(birthday.getId());
        birthdayDTO.setName(birthday.getName());
        birthdayDTO.setDate(birthday.getDate().toString());
        birthdayDTO.setRole(birthday.getRole().toString());
        getPhoto(birthday, birthdayDTO);
        return birthdayDTO;
    }
}
