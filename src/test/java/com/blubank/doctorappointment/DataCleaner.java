package com.blubank.doctorappointment;

import com.blubank.doctorappointment.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataCleaner {
    private final AppointmentScheduleRepository appointmentScheduleRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final ReservedAppointmentRepository reservedAppointmentRepository;
    private final SecurityUserRepository securityUserRepository;

    public void deleteAllData() {
        appointmentScheduleRepository.deleteAll();
        doctorRepository.deleteAll();
        patientRepository.deleteAll();
        reservedAppointmentRepository.deleteAll();
        securityUserRepository.deleteAll();
    }
}
