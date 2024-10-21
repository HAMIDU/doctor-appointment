package com.blubank.doctorappointment.base;

import com.auth0.jwt.interfaces.Claim;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@Order(1)
@Slf4j
public class RequestFilter extends OncePerRequestFilter {

    private final SecurityContext securityContext;
    private final JWTUtils jwtUtils;


    public RequestFilter(SecurityContext securityContext, JWTUtils jwtUtils) {
        this.securityContext = securityContext;
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        logger.info("Request Filter ....");
        if (httpServletRequest.getHeader(AUTHORIZATION) != null) {
            logger.info("Request Needs AUTHORIZATION ...");
            jwtUtils.validate(httpServletRequest.getHeader(AUTHORIZATION));
            Map<String, Claim> claims = jwtUtils.getAllClaims(httpServletRequest.getHeader(AUTHORIZATION));
            securityContext.setDoctorId(claims.get("doctorId") != null ? claims.get("doctorId").asLong() : null);
            securityContext.setSecurityUserId(claims.get("securityUserId") != null ? claims.get("securityUserId").asLong() : null);
            logger.info(claims);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }
}
