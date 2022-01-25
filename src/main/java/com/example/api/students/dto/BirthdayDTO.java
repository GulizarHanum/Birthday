package com.example.api.students.dto;

import lombok.Data;

@Data
public class BirthdayDTO {

    private Long id;
    private String name;
    private String date;
    private String role;
    private byte[] photo;
}
