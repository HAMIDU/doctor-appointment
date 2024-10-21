package com.blubank.doctorappointment.service;

import com.blubank.doctorappointment.base.DoctorAppointmentPropertiesWrapper;
import com.blubank.doctorappointment.base.JWTUtils;
import com.blubank.doctorappointment.dto.DoctorRequestDTO;
import com.blubank.doctorappointment.entity.Doctor;
import com.blubank.doctorappointment.entity.SecurityUser;
import com.blubank.doctorappointment.exception.CoreException;
import com.blubank.doctorappointment.exception.FaultCode;
import com.blubank.doctorappointment.repository.DoctorRepository;
import com.blubank.doctorappointment.repository.SecurityUserRepository;
import com.blubank.doctorappointment.service.impl.SecurityUserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {SecurityUserServiceImpl.class, DoctorAppointmentPropertiesWrapper.class})
@MockBean({JWTUtils.class, CompositePasswordEncoderService.class})
class SecurityUserServiceImplUTest {

    @Autowired
    private SecurityUserServiceImpl securityUserService;

    @MockBean
    private SecurityUserRepository securityUserRepository;

    @MockBean
    private DoctorRepository doctorRepository;

    @MockBean
    private DoctorService doctorService;

    @Captor
    private ArgumentCaptor<SecurityUser> securityUserCaptor;

    private SecurityUser dummyUser;
    private Doctor dummyDoctor;
    private DoctorRequestDTO registerDoctorRequestDto;

    @BeforeEach
    void setup() {
        registerDoctorRequestDto = new DoctorRequestDTO();

        dummyDoctor = Doctor.builder().id(1L).mobileNumber("0989121111111").build();

        dummyUser = SecurityUser.builder().username("username").isEnabled(true).password("password".getBytes()).build();
    }

    @Test
    void register_duplicateUsername_throwsException() {
        //arrange
        doReturn(dummyUser).when(securityUserRepository).findSecurityUserByUsername(any());

        try {
            //act
            securityUserService.register(registerDoctorRequestDto);
            fail("it throws a core exception with UNPROCESSABLE_CONTENT status. if this behaviour has been changed, please change me");
        } catch (CoreException ex) {
            //assert
            assertEquals(FaultCode.USERNAME_DECLARED_BEFORE.getCode(), ex.getCode());
            verify(securityUserRepository, never()).save(any());
            verify(doctorService, never()).saveDoctorProfile(any(), any());
        }
    }

    @Test
    void register_duplicatePhoneNumber_throwsException() {
        //arrange
        doReturn(dummyDoctor).when(doctorRepository).findDoctorByMobileNumber(any());

        try {
            //act
            securityUserService.register(registerDoctorRequestDto);
            fail("it throws a core exception with UNPROCESSABLE_CONTENT status. if this behaviour has been changed, please change me");
        } catch (CoreException ex) {
            //assert
            assertEquals(FaultCode.DOCTOR_MOBILE_NUMBER_REGISTERED_BEFORE.getCode(), ex.getCode());
            verify(securityUserRepository, never()).save(any());
            verify(doctorService, never()).saveDoctorProfile(any(), any());
        }
    }

    @Test
    void register_passwordContainsUsername_throwsException() {
        //arrange
        doReturn(null).when(securityUserRepository).findSecurityUserByUsername(any());
        doReturn(null).when(doctorRepository).findDoctorByMobileNumber(any());
        registerDoctorRequestDto.setPassword("usernamePassword");
        registerDoctorRequestDto.setUsername("username");

        try {
            //act
            securityUserService.register(registerDoctorRequestDto);
            fail("it throws a core exception with UNPROCESSABLE_CONTENT status. if this behaviour has been changed, please change me");
        } catch (CoreException ex) {
            //assert
            assertEquals(FaultCode.PASSWORD_CONTAIN_USERNAME.getCode(), ex.getCode());
            verify(securityUserRepository, never()).save(any());
            verify(doctorService, never()).saveDoctorProfile(any(), any());
        }
    }

    @Test
    void register_passwordTooLong_throwsException() {
        //arrange
        doReturn(null).when(securityUserRepository).findSecurityUserByUsername(any());
        doReturn(null).when(doctorRepository).findDoctorByMobileNumber(any());
        registerDoctorRequestDto.setPassword("usernamePassword");
        registerDoctorRequestDto.setUsername("john");

        try {
            //act
            securityUserService.register(registerDoctorRequestDto);
            fail("it throws a core exception with UNPROCESSABLE_CONTENT status. if this behaviour has been changed, please change me");
        } catch (CoreException ex) {
            //assert
            assertEquals(FaultCode.PASSWORD_TOO_LONG_ERROR.getCode(), ex.getCode());
            verify(securityUserRepository, never()).save(any());
            verify(doctorService, never()).saveDoctorProfile(any(), any());
        }
    }

    @Test
    void register_passwordInvalidPattern_throwsException() {
        //arrange
        doReturn(null).when(securityUserRepository).findSecurityUserByUsername(any());
        doReturn(null).when(doctorRepository).findDoctorByMobileNumber(any());
        registerDoctorRequestDto.setPassword("pass");
        registerDoctorRequestDto.setUsername("john");

        try {
            //act
            securityUserService.register(registerDoctorRequestDto);
            fail("it throws a core exception with UNPROCESSABLE_CONTENT status. if this behaviour has been changed, please change me");
        } catch (CoreException ex) {
            //assert
            assertEquals(FaultCode.PASSWORD_PATTERN_INVALID.getCode(), ex.getCode());
            verify(securityUserRepository, never()).save(any());
            verify(doctorService, never()).saveDoctorProfile(any(), any());
        }
    }

    @Test
    void register_happyFlow() {
        //arrange
        doReturn(null).when(securityUserRepository).findSecurityUserByUsername(any());
        doReturn(null).when(doctorRepository).findDoctorByMobileNumber(any());
        doReturn(dummyDoctor).when(doctorService).saveDoctorProfile(any(), any());
        registerDoctorRequestDto.setPassword("Pass@123");
        registerDoctorRequestDto.setUsername("john");

        //act
        securityUserService.register(registerDoctorRequestDto);

        //assert
        verify(securityUserRepository).save(securityUserCaptor.capture());
        var securityUser = securityUserCaptor.getValue();
        assertEquals(registerDoctorRequestDto.getUsername(), securityUser.getUsername());
        assertTrue(securityUser.getIsEnabled());
        verify(doctorService).saveDoctorProfile(any(), any());
    }
}
