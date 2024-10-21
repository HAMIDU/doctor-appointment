package com.blubank.doctorappointment.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    private String ipAddress;
}


