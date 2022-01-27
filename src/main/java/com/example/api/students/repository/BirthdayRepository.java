package com.example.api.students.repository;

import com.example.api.students.model.Birthday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс для работы с БД
 */
@Repository
public interface BirthdayRepository extends JpaRepository<Birthday, Long> {

}
