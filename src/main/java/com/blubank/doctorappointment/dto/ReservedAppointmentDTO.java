package com.blubank.doctorappointment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservedAppointmentDTO {
    Long id;

    Long DoctorId;

//    String DoctorName;

    Date appointmentScheduleFromTime;

    Date appointmentScheduleToTime;

//    String patientName;

    Long reservationId;

}
