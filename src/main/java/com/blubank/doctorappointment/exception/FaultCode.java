package com.blubank.doctorappointment.exception;

public enum FaultCode {

    INTERNAL_SERVER_ERROR(9000, "Internal Server Error", 500),
    INVALID_TOKEN(9001, "Invalid token", 403),
    TOKEN_REQUIRED(9002, "Token Required", 403),
    HAS_NOT_ACCESS(9003, "Has Not Access", 403),
    SERVICE_NOT_AVAILABLE(9004, "Service Not Available", 503),
    DATA_SIGN_FAILED(9005, "data sign failed", 603),
    DATA_INTEGRITY_VIOLATION_EXCEPTION(9006, "constraint violation exception", 400),
    INVALID_CREDENTIALS(9007, "incorrect password", 400),
    USER_IS_NOT_ENABLED(9008, "user is not enabled", 400),


    APPOINTMENT_LENGTH_IS_THIRTY_MINUTES(9020, "appointment length is 30 minutes", 422),
    USERNAME_DECLARED_BEFORE(9021, "username declared before", 422),
    DOCTOR_MOBILE_NUMBER_REGISTERED_BEFORE(9022, "doctor mobile number registered before", 422),
    APPOINTMENT_END_DATE_COULD_NOT_LESS_THAN_START_DATE(9023, "appointment end date could not be " +
            "less than start date", 422),
    PASSWORD_CONTAIN_USERNAME(9024, "password must not contain username.", 422),
    PASSWORD_PATTERN_INVALID(9025, "password pattern invalid.", 422),
    PASSWORD_TOO_LONG_ERROR(9026, "password is too long", 422),
    INVALID_APPOINTMENT_SCHEDULE(9027, "invalid appointment", 422),
    APPOINTMENT_HAS_BEEN_RESERVED(9028, "appointment has been reserved", 406),
    APPOINTMENT_IS_NOT_OPEN(9029, "appointment is not open", 404),
    PATIENT_IS_INVALID(9030, "Patient Not Found", 402);

    private final Integer code;

    private final String message;

    private final Integer httpResponseCode;

    FaultCode(Integer code, String message, Integer httpResponseCode) {
        this.code = code;
        this.message = message;
        this.httpResponseCode = httpResponseCode;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Integer getHttpResponseCode() {
        return httpResponseCode;
    }
}
