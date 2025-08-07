package com.group3.xecare2.user.repositories;

import com.group3.xecare2.user.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import com.group3.xecare2.enums.AppointmentStatus;
import com.group3.xecare2.user.entities.Appointment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.group3.xecare2.user.entities.AppointmentService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    @Query("SELECT COUNT(a) FROM Appointment a WHERE DATE(a.createdAt) = :date")
    long countByDate(@Param("date") LocalDate date);

    @Query("SELECT a.status AS status, COUNT(a) AS count " +
            "FROM Appointment a " +
            "GROUP BY a.status")
    List<Object[]> countAppointmentsByStatus();

    @Query("SELECT SUM(s.price) FROM AppointmentService s WHERE s.appointment.createdAt BETWEEN :from AND :to")
    BigDecimal getTotalRevenue(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    long countByCreatedAtBetween(LocalDateTime from, LocalDateTime to);

    long countByStatusAndCreatedAtBetween(AppointmentStatus status, LocalDateTime from, LocalDateTime to);

}