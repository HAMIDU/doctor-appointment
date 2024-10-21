package com.blubank.doctorappointment.projection;

import com.blubank.doctorappointment.enumeration.AppointmentStatus;

import java.util.Date;

public interface DoctorAppointmentDetailDTO {

    Long getDoctorId();

    Date getDueDate();

    Date getFromTime();

    Date getToTime();

    AppointmentStatus getStatus();

    String getFirstName();

    String getLastName();

    String getPhoneNumber();


}
