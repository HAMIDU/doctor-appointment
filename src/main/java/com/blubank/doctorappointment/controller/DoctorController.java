package com.blubank.doctorappointment.controller;

import com.blubank.doctorappointment.base.SecurityContext;
import com.blubank.doctorappointment.dto.AppointmentScheduleRequestDTO;
import com.blubank.doctorappointment.projection.DoctorAppointmentDetailDTO;
import com.blubank.doctorappointment.service.DoctorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@RequestMapping("/doctor")
@Tag(name = "Doctor APIs", description = "the controller API illustrates the services that doctors need to register visible " +
        "appointments")
public class DoctorController {

    private final DoctorService doctorService;
    private final SecurityContext securityContext;

    public DoctorController(DoctorService doctorService, SecurityContext securityContext) {
        this.doctorService = doctorService;
        this.securityContext = securityContext;
    }

    @Tag(name = "doctor set daily appointments", description = "doctor input start date time and end date time to system to " +
            "define a 30 minutes appointment schedule that patients could take them")
    @PostMapping("/add-appointment-schedule")
    public void addNewAppointmentSchedule(@Valid @RequestBody AppointmentScheduleRequestDTO appointmentScheduleRequestDTO) {
        doctorService.scheduleNewAppointment(appointmentScheduleRequestDTO, securityContext);
    }

    @Tag(name = "doctor's appointment schedule", description = "doctor can get all status of own open and reserved " +
            " appointment by patient info who reserved their appointment ")
    @GetMapping("/get-all-appointment")
    public List<DoctorAppointmentDetailDTO> getAll() {
        return doctorService.getDailyAppointmentScheduleStatusByDoctorId(securityContext.getDoctorId());
    }

    @Tag(name = "remove doctor own appointment", description = "doctor can remove own open appointment from schedule table")
    @DeleteMapping("/remove/{appointmentScheduleId}")
    public void removeOpenAppointment(@PathVariable @NotEmpty Long appointmentScheduleId) {
        doctorService.removeOpenAppointmentSchedule(appointmentScheduleId);
    }
}
