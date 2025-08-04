package com.group3.xecare2.user.services;

import com.group3.xecare2.user.dtos.AppointmentRequest;
import com.group3.xecare2.user.entities.Appointment;


public interface AppointmentServiceService {
	Appointment createAppointment(AppointmentRequest request);

}
