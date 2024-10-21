package com.blubank.doctorappointment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


@Table(name = "ReservedAppointment")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservedAppointment implements Serializable {
    private static final long serialVersionUID = 2552841687433728475L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "CreateDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PatientId")
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AppointmentScheduleId")
    private AppointmentSchedule appointmentSchedule;


}
