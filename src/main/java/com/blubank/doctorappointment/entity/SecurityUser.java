package com.blubank.doctorappointment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "SecurityUser")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SecurityUser implements Serializable {

    private static final long serialVersionUID = 1418273228798736535L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false, unique = true)
    private Long id;

    @NotNull
    @Column(name = "RegistrationDate")
    @Temporal(TemporalType.DATE)
    private Date registrationDate;

    @NotNull
    @Column(name = "Username", unique = true, length = 30)
    private String username;

    @NotNull
    @Column(name = "Password", columnDefinition = "binary(32)")
    private byte[] password;

    @Column(name = "IsEnabled")
    private Boolean isEnabled;


}
