package com.blubank.doctorappointment.service.impl;

import com.blubank.doctorappointment.dto.TakeOpenAppointmentRequestDTO;
import com.blubank.doctorappointment.entity.Patient;
import com.blubank.doctorappointment.entity.ReservedAppointment;
import com.blubank.doctorappointment.enumeration.AppointmentStatus;
import com.blubank.doctorappointment.exception.CoreException;
import com.blubank.doctorappointment.exception.FaultCode;
import com.blubank.doctorappointment.repository.AppointmentScheduleRepository;
import com.blubank.doctorappointment.repository.PatientRepository;
import com.blubank.doctorappointment.repository.ReservedAppointmentRepository;
import com.blubank.doctorappointment.service.PatientService;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final AppointmentScheduleRepository appointmentScheduleRepository;
    private final ReservedAppointmentRepository reservedAppointmentRepository;

    public PatientServiceImpl(PatientRepository patientRepository, AppointmentScheduleRepository appointmentScheduleRepository, ReservedAppointmentRepository reservedAppointmentRepository) {
        this.patientRepository = patientRepository;
        this.appointmentScheduleRepository = appointmentScheduleRepository;
        this.reservedAppointmentRepository = reservedAppointmentRepository;
    }


    @Override
    public synchronized ReservedAppointment takeOpenAppointment(@Valid TakeOpenAppointmentRequestDTO takeOpenAppointmentRequestDTO) {

        var patientInfo = patientRepository.findByPhoneNumber(takeOpenAppointmentRequestDTO.getPhoneNumber());

        if (patientInfo == null) patientInfo = patientRepository.save(
                Patient.builder().createDate(new Date())
                        .lastName(takeOpenAppointmentRequestDTO.getLastName().strip())
                        .firstName(takeOpenAppointmentRequestDTO.getFirstName().strip())
                        .phoneNumber(takeOpenAppointmentRequestDTO.getPhoneNumber().strip()).build());

        var appointmentSchedule = appointmentScheduleRepository.findById(takeOpenAppointmentRequestDTO.getAppointmentScheduleId())
                .orElseThrow(() -> new CoreException(FaultCode.INVALID_APPOINTMENT_SCHEDULE));

        if (appointmentSchedule.getStatus() != AppointmentStatus.OPEN) {
            throw new CoreException(FaultCode.APPOINTMENT_IS_NOT_OPEN);
        }
        appointmentSchedule.setStatus(AppointmentStatus.RESERVED);
        appointmentScheduleRepository.save(appointmentSchedule);
        return reservedAppointmentRepository.save(ReservedAppointment.builder()
                .patient(patientInfo).createDate(new Date()).appointmentSchedule(appointmentSchedule)
                .build());
    }

    @Override
    public List<ReservedAppointment> findMyAppointment(String phoneNumber) {
        var patientInfo = patientRepository.findByPhoneNumber(phoneNumber);
        if (patientInfo == null) {
            throw new CoreException(FaultCode.PATIENT_IS_INVALID);
        }

        return reservedAppointmentRepository.findByPatient(patientInfo);

    }
}
