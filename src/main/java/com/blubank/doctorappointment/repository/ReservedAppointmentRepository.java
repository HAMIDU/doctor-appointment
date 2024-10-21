package com.blubank.doctorappointment.repository;

import com.blubank.doctorappointment.entity.Patient;
import com.blubank.doctorappointment.entity.ReservedAppointment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservedAppointmentRepository extends GenericRepository<ReservedAppointment, Long> {

    Optional<ReservedAppointment> findByAppointmentSchedule_Id(Long appointmentId);


    List<ReservedAppointment> findByPatient(Patient appointmentId);
}
