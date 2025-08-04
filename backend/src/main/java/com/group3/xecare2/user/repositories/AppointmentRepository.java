package com.group3.xecare2.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.group3.xecare2.user.entities.Appointment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    @Query("SELECT COUNT(a) FROM Appointment a WHERE DATE(a.createdAt) = :date")
    long countByDate(@Param("date") LocalDate date);
}