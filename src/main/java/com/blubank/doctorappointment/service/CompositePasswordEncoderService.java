package com.blubank.doctorappointment.service;

public interface CompositePasswordEncoderService {

    byte[] encode(String username, String password);

    boolean check(byte[] var1, byte[] var2);
}
