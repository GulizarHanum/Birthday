package com.example.api.students;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Service
public class StudentService {
    public List<Student> list() {
        return List.of(
                new Student("Alex", LocalDate.of(2000, Month.JANUARY, 1)),
                new Student("Gulizar", LocalDate.of(2002, Month.JANUARY, 31))
        );
    }

    public void add(Student student) {
        System.out.println(student);
    }
}
