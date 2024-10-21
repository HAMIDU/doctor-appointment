package com.blubank.doctorappointment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenAppointmentRequestDTO {

    @NotEmpty
    Date dueDate;
    @NotEmpty
    Long doctorId;
}
