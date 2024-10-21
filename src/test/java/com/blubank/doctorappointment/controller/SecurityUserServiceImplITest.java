package com.blubank.doctorappointment.controller;

import com.blubank.doctorappointment.AbstractBaseIT;
import com.blubank.doctorappointment.HttpTestUtil;
import com.blubank.doctorappointment.dto.RestResponseDTO;
import com.blubank.doctorappointment.dto.DoctorRequestDTO;
import com.blubank.doctorappointment.entity.Doctor;
import com.blubank.doctorappointment.entity.SecurityUser;
import com.blubank.doctorappointment.exception.FaultCode;
import com.blubank.doctorappointment.repository.DoctorRepository;
import com.blubank.doctorappointment.repository.SecurityUserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SecurityUserServiceImplITest extends AbstractBaseIT {

    @Autowired
    private SecurityUserRepository securityUserRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    private DoctorRequestDTO registerDoctorRequestDto;

    @BeforeEach
    public void beforeEach() {
        dataCleaner.deleteAllData();
        registerDoctorRequestDto = new DoctorRequestDTO();
        setupUser();
        setupDoctor();
    }

    private void setupDoctor() {
        doctorRepository.save(Doctor.builder().mobileNumber("09121111111").build());
    }

    private void setupUser() {
        securityUserRepository.save(SecurityUser.builder()
                .username("john")
                .isEnabled(true)
                .password("pass".getBytes())
                .registrationDate(new Date())
                .build());
    }

    @Test
    void register_duplicateUsername_throwsException() throws Exception {
        //arrange
        registerDoctorRequestDto.setPassword("pass");
        registerDoctorRequestDto.setUsername("john");

        // act
        var mockRq = getMockedRq(registerDoctorRequestDto);

        var perform = mvc.perform(mockRq);

        // assert
        perform.andExpect(status().isUnprocessableEntity());

        var error = getErrorResponse(perform);
        assertEquals(FaultCode.USERNAME_DECLARED_BEFORE.getCode(), error.getErrorCode());
    }

    @Test
    void register_duplicatePhoneNumber_throwsException() throws Exception {
        //arrange
        registerDoctorRequestDto.setPassword("pass");
        registerDoctorRequestDto.setUsername("doctor");
        registerDoctorRequestDto.setMobileNumber("09121111111");

        // act
        var mockRq = getMockedRq(registerDoctorRequestDto);

        var perform = mvc.perform(mockRq);

        // assert
        perform.andExpect(status().isUnprocessableEntity());

        var error = getErrorResponse(perform);
        assertEquals(FaultCode.DOCTOR_MOBILE_NUMBER_REGISTERED_BEFORE.getCode(), error.getErrorCode());
    }

    @Test
    void register_passwordContainsUsername_throwsException() throws Exception {
        //arrange
        registerDoctorRequestDto.setPassword("usernamePassword");
        registerDoctorRequestDto.setUsername("username");

        // act
        var mockRq = getMockedRq(registerDoctorRequestDto);

        var perform = mvc.perform(mockRq);

        // assert
        perform.andExpect(status().isUnprocessableEntity());

        var error = getErrorResponse(perform);
        assertEquals(FaultCode.PASSWORD_CONTAIN_USERNAME.getCode(), error.getErrorCode());
    }

    @Test
    void register_passwordTooLong_throwsException() throws Exception {
        //arrange
        registerDoctorRequestDto.setPassword("usernamePassword");
        registerDoctorRequestDto.setUsername("kevin");

        // act
        var mockRq = getMockedRq(registerDoctorRequestDto);

        var perform = mvc.perform(mockRq);

        // assert
        perform.andExpect(status().isUnprocessableEntity());

        var error = getErrorResponse(perform);
        assertEquals(FaultCode.PASSWORD_TOO_LONG_ERROR.getCode(), error.getErrorCode());
    }

    @Test
    void register_passwordInvalidPattern_throwsException() throws Exception {
        //arrange
        registerDoctorRequestDto.setPassword("pass");
        registerDoctorRequestDto.setUsername("kevin");

        // act
        var mockRq = getMockedRq(registerDoctorRequestDto);

        var perform = mvc.perform(mockRq);

        // assert
        perform.andExpect(status().isUnprocessableEntity());

        var error = getErrorResponse(perform);
        assertEquals(FaultCode.PASSWORD_PATTERN_INVALID.getCode(), error.getErrorCode());
    }

    @Test
    void register_happyFlow() throws Exception {
        //arrange
        registerDoctorRequestDto.setPassword("Pass@123");
        registerDoctorRequestDto.setUsername("kevin");

        // act
        var mockRq = getMockedRq(registerDoctorRequestDto);

        var perform = mvc.perform(mockRq);

        // assert
        perform.andExpect(status().isOk());

        var securityUser = securityUserRepository.findByUsername("kevin");
        assertNotNull(securityUser);
        assertTrue(securityUser.getIsEnabled());

        var doctor = doctorRepository.findDoctorBySecurityUser(securityUser);
        assertEquals(registerDoctorRequestDto.getMobileNumber(), doctor.getMobileNumber());
    }

    private MockHttpServletRequestBuilder getMockedRq(Object content) throws Exception {
        return HttpTestUtil.getBaseJsonMvcBuilder(HttpMethod.POST, "/iam/register")
                .content(objectMapper.writeValueAsBytes(content));
    }

    private RestResponseDTO.ErrorData getErrorResponse(ResultActions resultActions) throws Exception {
        return objectMapper.readValue(
                resultActions.andReturn().getResponse().getContentAsByteArray(),
                new TypeReference<RestResponseDTO<RestResponseDTO.ErrorData>>() {
                }
        ).getErrorData();
    }
}
