package com.blubank.doctorappointment.service;

import com.blubank.doctorappointment.dto.DoctorRequestDTO;
import com.blubank.doctorappointment.dto.LoginRequestDTO;
import com.blubank.doctorappointment.entity.SecurityUser;

public interface SecurityUserService {

    /**
     * @param loginDTO
     * @return token with doctor info in payload
     */
    String login(LoginRequestDTO loginDTO);

    /**
     * register new doctor in system
     *
     * @param requestDTO
     */
    void register(DoctorRequestDTO requestDTO);

    /**
     * @param username
     * @param password
     * @return saved login user
     */
    SecurityUser saveSecurityUser(String username, String password);

}
