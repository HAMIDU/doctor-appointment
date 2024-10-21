package com.blubank.doctorappointment.service.impl;

import com.blubank.doctorappointment.base.DoctorAppointmentPropertiesWrapper;
import com.blubank.doctorappointment.base.SecurityContext;
import com.blubank.doctorappointment.dto.AppointmentScheduleRequestDTO;
import com.blubank.doctorappointment.dto.DoctorRequestDTO;
import com.blubank.doctorappointment.dto.OpenAppointmentRequestDTO;
import com.blubank.doctorappointment.entity.AppointmentSchedule;
import com.blubank.doctorappointment.entity.Doctor;
import com.blubank.doctorappointment.entity.SecurityUser;
import com.blubank.doctorappointment.enumeration.AppointmentStatus;
import com.blubank.doctorappointment.exception.CoreException;
import com.blubank.doctorappointment.exception.FaultCode;
import com.blubank.doctorappointment.projection.DoctorAppointmentDetailDTO;
import com.blubank.doctorappointment.repository.AppointmentScheduleRepository;
import com.blubank.doctorappointment.repository.DoctorRepository;
import com.blubank.doctorappointment.repository.ReservedAppointmentRepository;
import com.blubank.doctorappointment.service.DoctorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@Service
@Slf4j
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final AppointmentScheduleRepository appointmentScheduleRepository;
    private final ReservedAppointmentRepository reservedAppointmentRepository;
    private final DoctorAppointmentPropertiesWrapper doctorAppointmentPropertiesWrapper;

    public DoctorServiceImpl(DoctorRepository doctorRepository, AppointmentScheduleRepository appointmentScheduleRepository, ReservedAppointmentRepository reservedAppointmentRepository, DoctorAppointmentPropertiesWrapper doctorAppointmentPropertiesWrapper) {
        this.doctorRepository = doctorRepository;
        this.appointmentScheduleRepository = appointmentScheduleRepository;
        this.reservedAppointmentRepository = reservedAppointmentRepository;
        this.doctorAppointmentPropertiesWrapper = doctorAppointmentPropertiesWrapper;
    }


    @Transactional
    @Override
    public void scheduleNewAppointment(@Valid AppointmentScheduleRequestDTO appointmentScheduleRequestDTO,
                                       @Valid SecurityContext securityContext) {
        if (appointmentScheduleRequestDTO.getEndDate().before(appointmentScheduleRequestDTO.getStartDate()))
            throw new CoreException(FaultCode.APPOINTMENT_END_DATE_COULD_NOT_LESS_THAN_START_DATE);
        var duration = appointmentScheduleRequestDTO.getEndDate().getTime()
                - appointmentScheduleRequestDTO.getStartDate().getTime();
        if (duration < doctorAppointmentPropertiesWrapper.getAppointmentLength())
            throw new CoreException(FaultCode.APPOINTMENT_LENGTH_IS_THIRTY_MINUTES);

        var scheduleCount = Math.floorDiv( duration , doctorAppointmentPropertiesWrapper.getAppointmentLength());

        var doctorInfo = doctorRepository.findDoctorBySecurityUser(SecurityUser.builder()
                .id(securityContext.getSecurityUserId()).build());

        var startTime = appointmentScheduleRequestDTO.getStartDate();
        var toTime = DateUtils.addMilliseconds(startTime, doctorAppointmentPropertiesWrapper.getAppointmentLength());

        var counter = 0;
        while (counter < scheduleCount) {

            appointmentScheduleRepository.save(AppointmentSchedule.builder().fromTime(startTime)
                    .toTime(toTime).dueDate(startTime)
                    .doctor(doctorInfo)
                    .status(AppointmentStatus.OPEN).build());
            startTime = toTime;
            toTime = DateUtils.addMilliseconds(startTime, doctorAppointmentPropertiesWrapper.getAppointmentLength());

            counter++;
        }
    }

    @Override
    public List<DoctorAppointmentDetailDTO> getDailyAppointmentScheduleStatusByDoctorId(Long doctorId) {
        return appointmentScheduleRepository.getDailyAppointmentScheduleByStatusDoctorId(doctorId);
    }

    @Transactional
    @Override
    public synchronized void removeOpenAppointmentSchedule(Long appointmentScheduleId) {
        var appointmentSchedule = appointmentScheduleRepository.findById(appointmentScheduleId);
        if (appointmentSchedule.isEmpty())
            throw new CoreException(FaultCode.INVALID_APPOINTMENT_SCHEDULE);

        if (appointmentSchedule.get().getStatus() != AppointmentStatus.OPEN)
            throw new CoreException(FaultCode.APPOINTMENT_IS_NOT_OPEN);
        if (!reservedAppointmentRepository.findByAppointmentSchedule_Id(appointmentScheduleId).isEmpty())
            throw new CoreException(FaultCode.APPOINTMENT_HAS_BEEN_RESERVED);
        appointmentScheduleRepository.delete(appointmentSchedule.get());
    }

    @Override
    public Doctor saveDoctorProfile(DoctorRequestDTO requestDTO, SecurityUser securityUser) {
        return doctorRepository.save(Doctor.builder().firstName(requestDTO.getFirstName()).lastName(requestDTO.getLastName())
                .mobileNumber(requestDTO.getMobileNumber()).securityUser(securityUser).build());

    }

    @Override
    public List<AppointmentSchedule> findOpenAppointment(OpenAppointmentRequestDTO openAppointmentRequestDTO) {
        return appointmentScheduleRepository.findByDoctor_IdAndDueDateAndStatus(openAppointmentRequestDTO.getDoctorId(),
                openAppointmentRequestDTO.getDueDate(), AppointmentStatus.OPEN);


    }
}
