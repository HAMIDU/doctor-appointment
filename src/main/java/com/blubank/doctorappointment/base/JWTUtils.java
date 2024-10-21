package com.blubank.doctorappointment.base;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.blubank.doctorappointment.exception.CoreException;
import com.blubank.doctorappointment.exception.FaultCode;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.removeStart;


@Component
public class JWTUtils {

    private final DoctorAppointmentPropertiesWrapper doctorAppointmentPropertiesWrapper;

    public JWTUtils(DoctorAppointmentPropertiesWrapper doctorAppointmentPropertiesWrapper) {
        this.doctorAppointmentPropertiesWrapper = doctorAppointmentPropertiesWrapper;
    }


    public Map<String, Claim> getAllClaims(String token) {
        String jwt = removePart(token);
        return createVerifier(Algorithm.HMAC512(doctorAppointmentPropertiesWrapper.getSecretKey())).verify(jwt).getClaims();
    }

    public String generateToken(Map<String, Object> claims, String subject, Integer expirationTime) {
        return JWT.create()
                .withPayload(claims)
                .withSubject(subject)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(Algorithm.HMAC512(doctorAppointmentPropertiesWrapper.getSecretKey()));
    }


    private String removePart(String token) {
        Optional<String> jwt = Optional.ofNullable(token)
                .map(value -> removeStart(value, "Bearer"))
                .map(String::trim);
        return jwt.orElseThrow(() -> {
            throw new CoreException(FaultCode.INVALID_TOKEN);
        });
    }

    public boolean validate(String token) {
        String jwt = removePart(token);
        try {
            createVerifier(Algorithm.HMAC512(doctorAppointmentPropertiesWrapper.getSecretKey())).verify(jwt);
            return true;
        } catch (JWTVerificationException exception) {
            return false;
        }
    }

    public JWTVerifier createVerifier(Algorithm algorithm) {
        return JWT.require(algorithm).build();
    }

}
