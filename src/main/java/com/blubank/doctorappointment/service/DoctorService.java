package com.blubank.doctorappointment.service;

import com.blubank.doctorappointment.base.SecurityContext;
import com.blubank.doctorappointment.dto.AppointmentScheduleRequestDTO;
import com.blubank.doctorappointment.dto.DoctorRequestDTO;
import com.blubank.doctorappointment.dto.OpenAppointmentRequestDTO;
import com.blubank.doctorappointment.entity.AppointmentSchedule;
import com.blubank.doctorappointment.entity.Doctor;
import com.blubank.doctorappointment.entity.SecurityUser;
import com.blubank.doctorappointment.projection.DoctorAppointmentDetailDTO;

import java.util.List;

public interface DoctorService {

    /**
     * @param appointmentScheduleRequestDTO includes start time to end time of appointment schedule
     * @param securityContext               who is the doctor that want to set open appointments for patient
     */
    void scheduleNewAppointment(AppointmentScheduleRequestDTO appointmentScheduleRequestDTO, SecurityContext securityContext);

    /**
     * @param doctorId whose appointment schedule should be returned
     * @return doctor <code>doctor id</code> all appointment schedule, if these are reserved by patient, also return
     * patients info
     */
    List<DoctorAppointmentDetailDTO> getDailyAppointmentScheduleStatusByDoctorId(Long doctorId);

    /**
     * remove the available and open appointment by doctor that the appointment is theirs
     *
     * @param appointmentScheduleId must be removed
     */
    void removeOpenAppointmentSchedule(Long appointmentScheduleId);

    /**
     * @param doctorRequestDTO includes login profile info that must be saved for new doctor
     * @param securityUser     doctor login info
     * @return doctor saved profile
     */
    Doctor saveDoctorProfile(DoctorRequestDTO doctorRequestDTO, SecurityUser securityUser);

    /**
     * @param openAppointmentRequestDTO consists of due date and doctor info
     * @return all an open and available appointment for a specific doctor <code>openAppointmentRequestDTO.doctorId</code>
     * in a defined day <code>openAppointmentRequestDTO.dueDate</code>
     */
    List<AppointmentSchedule> findOpenAppointment(OpenAppointmentRequestDTO openAppointmentRequestDTO);
}
