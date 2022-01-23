package com.example.api.students;

import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

@Service
public class BirthdaysService {

    private final BirthdayRepository birthdayRepository;

    public BirthdaysService(BirthdayRepository birthdayRepository) {
        this.birthdayRepository = birthdayRepository;
    }

    public List<Birthday> getBirthdays() {
        return birthdayRepository.findAll();
    }

    public void addBirthday(Birthday birthday) throws IllegalArgumentException {
        birthdayChecks(birthday);
        birthdayRepository.save(birthday);
    }

    public Birthday editBirthday(Birthday birthday) throws IllegalArgumentException {
        birthdayChecks(birthday);
        Birthday birthdayRecord = birthdayRepository.findById(birthday.getId()).orElseThrow(() -> new EntityNotFoundException(String.format("Запись с id %s не найдена!", birthday.getId())));
        birthdayRecord.setName(birthday.getName());
        birthdayRecord.setDate(birthday.getDate());
        birthdayRecord.setPhoto(birthday.getPhoto());

        return birthdayRepository.save(birthday);
    }

    private void birthdayChecks(Birthday checkBirthday) throws IllegalArgumentException {
        if (checkBirthday.getName() == null) {
            throw new IllegalArgumentException("Некорректное имя");
        }
        if (checkBirthday.getDate().isAfter(LocalDate.now())
                || checkBirthday.getDate().isBefore(LocalDate.now().minusYears(100))) {
            throw new IllegalArgumentException("Некорректная дата рождения");
        }
    }

    public void deleteById(Long birthdayId) {
        if (birthdayId == null) {
            throw new IllegalArgumentException("Введите айди записи");
        }
        birthdayRepository.findById(birthdayId).orElseThrow(() -> new EntityNotFoundException(String.format("Запись с id %s не найдена!", birthdayId)));

        birthdayRepository.deleteById(birthdayId);
    }
}
