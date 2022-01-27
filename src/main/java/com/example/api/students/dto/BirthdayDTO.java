package com.example.api.students.dto;

import lombok.Data;

/**
 * Класс с данными для отправки на презентацию (Data Transfer Object)
 */
@Data
public class BirthdayDTO {

    private Long id;
    private String name;
    private String date;
    private String role;
    private String photo;
}
