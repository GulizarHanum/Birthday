package com.example.api.students;

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
    public List<Birthday> list() {
        return birthdaysService.getBirthdays();
    }

    @PostMapping
    public ResponseEntity add(@RequestBody Birthday birthday) {
        try {
            birthdaysService.addBirthday(birthday);
            return ResponseEntity.ok().body(birthdaysService.getBirthdays());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity edit(@RequestBody Birthday birthday) {
        try {
            return ResponseEntity.ok(birthdaysService.editBirthday(birthday));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestParam Long id) {
        try {
            birthdaysService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
