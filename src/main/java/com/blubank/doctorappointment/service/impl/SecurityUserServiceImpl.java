package com.blubank.doctorappointment.service.impl;

import com.blubank.doctorappointment.base.DoctorAppointmentPropertiesWrapper;
import com.blubank.doctorappointment.base.JWTUtils;
import com.blubank.doctorappointment.base.ValidationUtility;
import com.blubank.doctorappointment.dto.DoctorRequestDTO;
import com.blubank.doctorappointment.dto.LoginRequestDTO;
import com.blubank.doctorappointment.entity.SecurityUser;
import com.blubank.doctorappointment.exception.CoreException;
import com.blubank.doctorappointment.exception.FaultCode;
import com.blubank.doctorappointment.repository.DoctorRepository;
import com.blubank.doctorappointment.repository.SecurityUserRepository;
import com.blubank.doctorappointment.service.CompositePasswordEncoderService;
import com.blubank.doctorappointment.service.DoctorService;
import com.blubank.doctorappointment.service.SecurityUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import static com.blubank.doctorappointment.exception.FaultCode.DOCTOR_MOBILE_NUMBER_REGISTERED_BEFORE;
import static com.blubank.doctorappointment.exception.FaultCode.USERNAME_DECLARED_BEFORE;

@Service
@Slf4j
public class SecurityUserServiceImpl implements SecurityUserService {

    private final DoctorService doctorService;
    private final SecurityUserRepository securityUserRepository;
    private final DoctorRepository doctorRepository;
    private final JWTUtils jwtUtils;
    private final DoctorAppointmentPropertiesWrapper doctorAppointmentPropertiesWrapper;
    private final CompositePasswordEncoderService passwordEncoder;


    public SecurityUserServiceImpl(DoctorService doctorService, SecurityUserRepository securityUserRepository, DoctorRepository doctorRepository, JWTUtils jwtUtils, DoctorAppointmentPropertiesWrapper doctorAppointmentPropertiesWrapper, CompositePasswordEncoderService passwordEncoder) {
        this.doctorService = doctorService;
        this.securityUserRepository = securityUserRepository;
        this.doctorRepository = doctorRepository;
        this.jwtUtils = jwtUtils;

        this.doctorAppointmentPropertiesWrapper = doctorAppointmentPropertiesWrapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String login(LoginRequestDTO loginDTO) {


        var securityUser = securityUserRepository.findByUsername(loginDTO.getUsername());
        if (securityUser == null) {
            throw new CoreException(FaultCode.INVALID_CREDENTIALS);
        }

        if (Boolean.FALSE.equals(securityUser.getIsEnabled())) {
            throw new CoreException(FaultCode.USER_IS_NOT_ENABLED);
        }

        var newHasPassword = passwordEncoder.encode(
                loginDTO.getUsername(), loginDTO.getPassword());

        if (!passwordEncoder.check(newHasPassword, securityUser.getPassword())) {
            throw new CoreException(FaultCode.INVALID_CREDENTIALS);
        }


        var loggedInDoctorProfile = doctorRepository.findDoctorBySecurityUser(securityUser);

        //Create Token
        Map<String, Object> claims = new TreeMap<>();
        claims.put("securityUserId", securityUser.getId());
        claims.put("doctorId", loggedInDoctorProfile.getId());
        return jwtUtils.generateToken(claims, securityUser.getUsername(), doctorAppointmentPropertiesWrapper.getTokenValidity());

    }

    @Override

    @Transactional
    public void register(DoctorRequestDTO requestDTO) {

        if (securityUserRepository.findSecurityUserByUsername(requestDTO.getUsername()) != null) {
            throw new CoreException(USERNAME_DECLARED_BEFORE);
        }
        if (doctorRepository.findDoctorByMobileNumber(requestDTO.getMobileNumber()) != null) {
            throw new CoreException(DOCTOR_MOBILE_NUMBER_REGISTERED_BEFORE);
        }
        checkPassword(requestDTO.getUsername(), requestDTO.getPassword());
        var securityUser = saveSecurityUser(requestDTO.getUsername(), requestDTO.getPassword());
        var doctor = doctorService.saveDoctorProfile(requestDTO, securityUser);
        log.info(doctor.getId().toString());
    }

    @Transactional
    public SecurityUser saveSecurityUser(String username, String password) {
        var bcryptPassword = passwordEncoder.encode(username, password);
        var securityUser = SecurityUser.builder().registrationDate(new Date()).username(username).
                password(bcryptPassword)
                .isEnabled(Boolean.TRUE).build();
        return securityUserRepository.save(securityUser);

    }

    private void checkPassword(String username, String password) {
        if (password.toLowerCase().contains(username.toLowerCase())) {
            log.error(FaultCode.PASSWORD_CONTAIN_USERNAME.getMessage());
            throw new CoreException(FaultCode.PASSWORD_CONTAIN_USERNAME);
        }

        if (password.length() > doctorAppointmentPropertiesWrapper.getMaximumPasswordLength())
            throw new CoreException(FaultCode.PASSWORD_TOO_LONG_ERROR);

        if (!ValidationUtility.validatePassword(password)) {
            log.error(FaultCode.PASSWORD_PATTERN_INVALID.getMessage());
            throw new CoreException(FaultCode.PASSWORD_PATTERN_INVALID);
        }
    }

}
