package com.blubank.doctorappointment.repository;

import com.blubank.doctorappointment.entity.SecurityUser;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Repository
public interface SecurityUserRepository extends GenericRepository<SecurityUser, Long> {

    SecurityUser findSecurityUserByUsername(@NotNull @NotEmpty String username);

    SecurityUser findByUsername(String username);
}
