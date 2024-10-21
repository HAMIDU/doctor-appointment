package com.blubank.doctorappointment.exception;

import com.blubank.doctorappointment.dto.RestResponseDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@AllArgsConstructor
@Slf4j
public class BaseExceptionHandler {

    @ExceptionHandler(BaseException.class)
    @ResponseBody
    public ResponseEntity<RestResponseDTO<Void>> handleWalletException(HttpServletRequest req, BaseException e) {
        try {

            e.setMessage(e.getMessage());
            return ResponseEntity.status(e.getHttpResponseCode()).body(new RestResponseDTO<>(e));
        } finally {
            log.info("exception occurred: ", e);
        }
    }
}
