package com.blubank.doctorappointment.controller;

import com.blubank.doctorappointment.dto.DoctorRequestDTO;
import com.blubank.doctorappointment.dto.LoginRequestDTO;
import com.blubank.doctorappointment.service.SecurityUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/iam")
@Slf4j
@Tag(name = "IAM APIs", description = "all authentication and authorization services")
public class IAMController {

    private SecurityUserService securityUserService;

    public IAMController(SecurityUserService securityUserService) {
        this.securityUserService = securityUserService;
    }

    @GetMapping("/login")
    @Tag(name = "login", description = "Doctors can login ")
    public String login(@RequestBody @Valid LoginRequestDTO loginDTO) {
        return securityUserService.login(loginDTO);
    }

    @PostMapping("/register")
    @Tag(name = "register", description = "Doctors can register in system ")
    public void register(@RequestBody @Valid DoctorRequestDTO requestDTO) {
        securityUserService.register(requestDTO);
    }
}
