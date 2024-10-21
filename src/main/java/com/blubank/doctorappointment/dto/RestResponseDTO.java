package com.blubank.doctorappointment.dto;

import com.blubank.doctorappointment.exception.BaseException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
public class RestResponseDTO<T> {

    private boolean successful;
    private T response;
    private ErrorData errorData;

    public static <S> RestResponseDTO<S> of(S response) {
        return new RestResponseDTO<>(response);
    }

    public static <S> RestResponseDTO<S> ok() {
        return new RestResponseDTO<>();
    }

    public static RestResponseDTO<?> error(ErrorData errorData) {
        return new RestResponseDTO<>(errorData);
    }

    public RestResponseDTO(T response) {
        this.successful = true;
        this.response = response;
    }

    public RestResponseDTO() {
        this.successful = true;
    }

    public RestResponseDTO(ErrorData errorData) {
        this.successful = false;
        this.errorData = errorData;
    }

    public RestResponseDTO(BaseException exception) {
        this.successful = false;
        this.errorData = ErrorData.builder()
                .errorCode(exception.getCode())
                .message(exception.getMessage())
                .build();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ErrorData {
        private int errorCode;
        private String message;
        private Map<String, Object> data;
    }

}
