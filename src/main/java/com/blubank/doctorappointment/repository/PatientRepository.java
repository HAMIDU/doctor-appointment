package com.blubank.doctorappointment.repository;

import com.blubank.doctorappointment.entity.Patient;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends GenericRepository<Patient, Long> {

    Patient findByPhoneNumber(String phoneNumber);
}
