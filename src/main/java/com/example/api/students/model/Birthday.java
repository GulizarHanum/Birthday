package com.example.api.students.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Сущность предметной области - День рождение
 */
@Entity
@Table(name = "birthday")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Birthday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    private String name;
    private LocalDate date;
    private Role role;
    private byte[] photo;
}
