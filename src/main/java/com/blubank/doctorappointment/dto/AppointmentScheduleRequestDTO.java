package com.blubank.doctorappointment.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Getter
@Setter
public class AppointmentScheduleRequestDTO {

    @NotEmpty
    @Temporal(TemporalType.TIMESTAMP)
    Date startDate;

    @NotEmpty
    @Temporal(TemporalType.TIMESTAMP)
    Date endDate;

}
