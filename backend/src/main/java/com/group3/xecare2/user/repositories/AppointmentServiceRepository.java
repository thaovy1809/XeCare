package com.group3.xecare2.user.repositories;

import com.group3.xecare2.garage.dtos.MonthlyRevenueDto;
import org.springframework.data.jpa.repository.JpaRepository;

import com.group3.xecare2.user.entities.AppointmentService;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AppointmentServiceRepository extends JpaRepository<AppointmentService, Integer> {
    @Query("SELECT new com.group3.xecare2.garage.dtos.MonthlyRevenueDto(MONTH(a.appointmentTime), SUM(s.price)) " +
            "FROM AppointmentService s " +
            "JOIN s.appointment a " +
            "WHERE YEAR(a.appointmentTime) = :year " +
            "GROUP BY MONTH(a.appointmentTime) " +
            "ORDER BY MONTH(a.appointmentTime)")
    List<MonthlyRevenueDto> getMonthlyRevenue(@Param("year") int year);

    @Query("SELECT s.name AS name, COUNT(asv) AS count " +
            "FROM AppointmentService asv " +
            "JOIN asv.service s " +
            "GROUP BY s.name")
    List<Object[]> countServicesUsed();

}
