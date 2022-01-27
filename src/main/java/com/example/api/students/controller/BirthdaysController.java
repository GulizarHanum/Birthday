package com.example.api.students.controller;

import com.example.api.students.dto.BirthdayDTO;
import com.example.api.students.service.BirthdaysService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/birthdays")
public class BirthdaysController {

    private final BirthdaysService birthdaysService;

    /**
     * Конструктор класса
     *
     * @param birthdaysService сервис для работы с записями ДР
     */
    public BirthdaysController(BirthdaysService birthdaysService) {
        this.birthdaysService = birthdaysService;
    }

    /**
     * Получить запись ДР по ее id
     *
     * @param id идентификатор записи
     *
     * @return DTO с данными о ДР
     */
    @GetMapping(path = "{id}")
    public BirthdayDTO getBirthdayById(@PathVariable("id") Long id) {
        return birthdaysService.getBirthdayById(id);
    }

    /**
     * Получить список всех ДР
     *
     * @return список DTO всех ДР
     */
    @GetMapping(path = "list")
    public List<BirthdayDTO> list() {
        return birthdaysService.getAllBirthdays();
    }

    /**
     * Получить список всех ДР в ближайшие 14 дней
     *
     * @return список DTO всех ДР в ближайшие 14 дней
     */
    @GetMapping(path = "upcoming")
    public List<BirthdayDTO> upcoming() {
        return birthdaysService.getUpcomingBirthdays();
    }

    /**
     * Создать запись о ДР
     *
     * @return DTO или 400 Bad Request с текстом ошибки
     */
    @PostMapping
    public ResponseEntity<?> add(@RequestBody BirthdayDTO birthday) {
        try {
            birthdaysService.addBirthday(birthday);
            return ResponseEntity.ok().body(birthdaysService.getAllBirthdays());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Редактировать запись о ДР
     *
     * @return 200 OK
     */
    @PutMapping
    public ResponseEntity<?> edit(@RequestBody BirthdayDTO birthday) {
        try {
            return ResponseEntity.ok(birthdaysService.editBirthday(birthday));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Удалить запись о ДР
     *
     * @return 204 No Content
     */
    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam Long id) {
        try {
            birthdaysService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
