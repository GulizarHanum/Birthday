package com.example.api.students.service;

import com.example.api.students.model.Role;
import com.example.api.students.dto.BirthdayDTO;
import com.example.api.students.model.Birthday;
import com.example.api.students.repository.BirthdayRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
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
        birthdayRecord.setPhoto(newBirthday.getPhoto());

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
        birthdayRecord.setPhoto(newBirthday.getPhoto());

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

    private static BirthdayDTO buildDto(Birthday birthday) {
        BirthdayDTO birthdayDTO = new BirthdayDTO();
        birthdayDTO.setId(birthday.getId());
        birthdayDTO.setName(birthday.getName());
        birthdayDTO.setDate(birthday.getDate().toString());
        birthdayDTO.setRole(birthday.getRole().toString());
        birthdayDTO.setPhoto(birthday.getPhoto());
        return birthdayDTO;
    }
}
