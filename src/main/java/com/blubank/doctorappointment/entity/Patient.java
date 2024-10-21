package com.blubank.doctorappointment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


@Table(name = "Patient")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Patient implements Serializable {

    private static final long serialVersionUID = -7875947140288284576L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", unique = true)
    private Long id;

    @NotNull
    @Column(name = "CreateDate")
    @Temporal(TemporalType.DATE)
    private Date createDate;

    @NotNull
    @Column(name = "FirstName", length = 50)
    private String firstName;

    @NotNull
    @Column(name = "LastName", length = 50)
    private String lastName;

    @NotNull
    @Column(name = "PhoneNumber", length = 11, unique = true)
    private String phoneNumber;

}
