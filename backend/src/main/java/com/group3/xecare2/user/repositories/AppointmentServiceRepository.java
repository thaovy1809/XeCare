package com.group3.xecare2.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.group3.xecare2.user.entities.AppointmentService;

public interface AppointmentServiceRepository extends JpaRepository<AppointmentService, Integer> {
}
