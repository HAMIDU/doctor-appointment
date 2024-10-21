package com.blubank.doctorappointment.base;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class DoctorAppointmentPropertiesWrapper {


    @Value("${secret.key}")
    private String secretKey;

    @Value("${token.validity}")
    private Integer tokenValidity;

    @Value("${password.policy.max.length}")
    private Integer maximumPasswordLength;

    @Value("${appointment.length}")
    private Integer appointmentLength;

    @Value("${minimum.password.length}")
    private Short minimumPasswordLength;
}
