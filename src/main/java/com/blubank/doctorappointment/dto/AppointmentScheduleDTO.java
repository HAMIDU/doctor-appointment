package com.blubank.doctorappointment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentScheduleDTO  {

    private Long id;
    private Date dueDate;
    private Date fromTime;
    private Date toTime;

    private Long doctorId;
}
