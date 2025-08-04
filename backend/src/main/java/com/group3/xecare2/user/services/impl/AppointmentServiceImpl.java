package com.group3.xecare2.user.services.impl;

import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.group3.xecare2.admin.entities.Services;
import com.group3.xecare2.admin.entities.VehicleType;
import com.group3.xecare2.admin.repositories.ServiceRepository;
import com.group3.xecare2.admin.repositories.VehicleTypeRepository;
import com.group3.xecare2.enums.AppointmentStatus;
import com.group3.xecare2.garage.entities.Garage;
import com.group3.xecare2.garage.repositories.GarageRepository;
import com.group3.xecare2.user.dtos.AppointmentRequest;
import com.group3.xecare2.user.entities.Appointment;
import com.group3.xecare2.user.entities.AppointmentService;
import com.group3.xecare2.user.entities.User;
import com.group3.xecare2.user.repositories.AppointmentRepository;
import com.group3.xecare2.user.repositories.AppointmentServiceRepository;
import com.group3.xecare2.user.repositories.UserRepository;
import com.group3.xecare2.user.services.AppointmentServiceService;

@Service
public class AppointmentServiceImpl implements AppointmentServiceService {
	@Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AppointmentServiceRepository appointmentServiceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GarageRepository garageRepository;

    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Override
    public Appointment createAppointment(AppointmentRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Garage garage = garageRepository.findById(request.getGarageId())
                .orElseThrow(() -> new RuntimeException("Garage not found"));
        VehicleType vehicleType = vehicleTypeRepository.findById(request.getVehicleTypeId())
                .orElseThrow(() -> new RuntimeException("Vehicle type not found"));

        Appointment appointment = Appointment.builder()
                .user(user)
                .garage(garage)
                .vehicleType(vehicleType)
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .appointmentTime(request.getAppointmentTime())
                .notes(request.getNotes())
                .status(AppointmentStatus.PENDING)
                .build();

        Appointment savedAppointment = appointmentRepository.save(appointment);

        for (AppointmentRequest.ServiceItem item : request.getServices()) {
        	Services service = serviceRepository.findById(item.getServiceId())
                    .orElseThrow(() -> new RuntimeException("Service not found"));

            AppointmentService  appointmentService = AppointmentService.builder()
                    .appointment(savedAppointment)
                    .service(service)
                    .price(new BigDecimal(item.getPrice()))
                    .build();

            appointmentServiceRepository.save(appointmentService);
        }

        return savedAppointment;
    }
}
