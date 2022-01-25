package com.example.api.students.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "birthday")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Birthday {

    @Id
    @SequenceGenerator(name = "student_sequence", sequenceName = "student_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_sequence")
    private Long id;

    private String name;
    private LocalDate date;
    private Role role;
    private byte[] photo;

    public Birthday(String name, LocalDate date) {
        this.name = name;
        this.date = date;
    }
}
