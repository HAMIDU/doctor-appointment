package com.blubank.doctorappointment.controller;


import com.blubank.doctorappointment.dto.AppointmentScheduleDTO;
import com.blubank.doctorappointment.dto.OpenAppointmentRequestDTO;
import com.blubank.doctorappointment.dto.ReservedAppointmentDTO;
import com.blubank.doctorappointment.dto.TakeOpenAppointmentRequestDTO;
import com.blubank.doctorappointment.mapper.AppointmentScheduleMapper;
import com.blubank.doctorappointment.mapper.ReservedAppointmentMapper;
import com.blubank.doctorappointment.service.DoctorService;
import com.blubank.doctorappointment.service.PatientService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;


@RestController
@RequestMapping("/patient")
@Tag(name = "Patient APIs", description = "the controller API illustrates the services that patients will use")
public class PatientController {

    private final DoctorService doctorService;
    private final PatientService patientService;

    public PatientController(DoctorService doctorService, PatientService patientService) {
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    @PostMapping("/all-open-appointments")
    @Tag(name = "Open and valid appointments", description = "Get all valid appointments")
    public List<AppointmentScheduleDTO> findOpenAppointment(@RequestBody @Valid OpenAppointmentRequestDTO openAppointmentRequestDTO) {
        return AppointmentScheduleMapper.INSTANCE.toDtoList(doctorService.findOpenAppointment(openAppointmentRequestDTO));

    }

    @Tag(name = "Taken appointment ", description = "Patient reserved a valid appointment")
    @PostMapping("/take-appointment")
    public ReservedAppointmentDTO takeOpenAppointment(@RequestBody @Valid TakeOpenAppointmentRequestDTO takeOpenAppointmentRequestDTO) {

        return ReservedAppointmentMapper.INSTANCE.toDTO(patientService.takeOpenAppointment(takeOpenAppointmentRequestDTO));
    }

    @Tag(name = "Patient own reserved appointments ", description = "Patient get all own reserved appointments")
    @GetMapping("/my-appointment/{phoneNumber}")
    public List<ReservedAppointmentDTO> findMyAppointment(@PathVariable @NotEmpty String phoneNumber) {

        return ReservedAppointmentMapper.INSTANCE.toDtoList(patientService.findMyAppointment(phoneNumber));
    }
}
