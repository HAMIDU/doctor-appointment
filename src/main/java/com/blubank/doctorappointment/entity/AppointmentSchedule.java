package com.blubank.doctorappointment.entity;


import com.blubank.doctorappointment.enumeration.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


@Table(name = "AppointmentSchedule")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentSchedule implements Serializable {

    private static final long serialVersionUID = 3708074112632977333L;

    @Id
    @Column(name = "Id", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "DueDate")
    @Temporal(TemporalType.DATE)
    private Date dueDate;

    @NotNull
    @Column(name = "FromTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fromTime;

    @NotNull
    @Column(name = "TotTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date toTime;

    @NotNull
    @Column(name = "Status")
    private AppointmentStatus status;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DoctorId")
    private Doctor doctor;

}
