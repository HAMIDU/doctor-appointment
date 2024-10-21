package com.blubank.doctorappointment.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Table(name = "Doctor")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Doctor implements Serializable {

    private static final long serialVersionUID = 3702974112632977333L;

    @Id
    @Column(name = "Id", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull
    @Column(name = "FirstName", length = 50)
    private String firstName;

    @NotNull
    @Column(name = "LastName", length = 50)
    private String lastName;

    @NotNull
    @Column(name = "MobileNumber", length = 11, unique = true)
    private String mobileNumber;


    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SecurityUserId")
    private SecurityUser securityUser;

}
