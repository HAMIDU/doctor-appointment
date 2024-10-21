package com.blubank.doctorappointment.service;

import com.blubank.doctorappointment.dto.TakeOpenAppointmentRequestDTO;
import com.blubank.doctorappointment.entity.ReservedAppointment;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public interface PatientService {


    /**
     * @param takeOpenAppointmentRequestDTO includes patient info who want to reserved an available appointment
     *                                      and an open appointment belongs a specific doctor
     * @return after taking a open appointment, the reserved info will be returned
     */

    ReservedAppointment takeOpenAppointment(@Valid TakeOpenAppointmentRequestDTO takeOpenAppointmentRequestDTO);

    /**
     * @param phoneNumber that demonstrate whose appointment should be returned
     * @return my reserved appointments detail info
     */
    List<ReservedAppointment> findMyAppointment(@NotEmpty String phoneNumber);

}
