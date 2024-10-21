package com.blubank.doctorappointment.repository;

import com.blubank.doctorappointment.entity.Doctor;
import com.blubank.doctorappointment.entity.SecurityUser;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotEmpty;

@Repository
public interface DoctorRepository extends GenericRepository<Doctor, Long> {

    Doctor findDoctorByMobileNumber( @NotEmpty String mobileNumber);

    Doctor findDoctorBySecurityUser(@NotEmpty SecurityUser securityUser);
}
