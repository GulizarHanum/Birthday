package com.example.api.students.controller;

import com.example.api.students.service.BirthdaysService;
import com.example.api.students.dto.BirthdayDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/birthdays")
public class BirthdaysController {

    private final BirthdaysService birthdaysService;

    public BirthdaysController(BirthdaysService birthdaysService) {
        this.birthdaysService = birthdaysService;
    }

    @GetMapping(path = "list")
    public List<BirthdayDTO> list() {
        return birthdaysService.getAllBirthdays();
    }

    @GetMapping(path = "upcoming")
    public List<BirthdayDTO> upcoming() {
        return birthdaysService.getUpcomingBirthdays();
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody BirthdayDTO birthday) {
        try {
            birthdaysService.addBirthday(birthday);
            return ResponseEntity.ok().body(birthdaysService.getAllBirthdays());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> edit(@RequestBody BirthdayDTO birthday) {
        try {
            return ResponseEntity.ok(birthdaysService.editBirthday(birthday));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

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
