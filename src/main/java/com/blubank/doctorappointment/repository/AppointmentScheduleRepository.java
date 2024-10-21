package com.blubank.doctorappointment.repository;

import com.blubank.doctorappointment.entity.AppointmentSchedule;
import com.blubank.doctorappointment.enumeration.AppointmentStatus;
import com.blubank.doctorappointment.projection.DoctorAppointmentDetailDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

@Repository
public interface AppointmentScheduleRepository extends GenericRepository<AppointmentSchedule, Long> {

    @Query(value = "SELECT aps.fromTime as fromTime, aps.toTime as toTime, aps.status as status, " +
            " p.firstName as firstName, p.lastName as lastName, p.phoneNumber as phoneNumber, " +
            " aps.doctor.id as doctorId, " +
            " aps.dueDate as dueDate " +
            " from AppointmentSchedule aps " +
            " left join ReservedAppointment ra on ra.appointmentSchedule.id = aps.id " +
            " left join Patient p on p.id=ra.patient.id " +
            " where aps.doctor.id= :doctorId")
    List<DoctorAppointmentDetailDTO> getDailyAppointmentScheduleByStatusDoctorId(Long doctorId);

    List<AppointmentSchedule> findByDoctor_IdAndDueDateAndStatus(@NotNull Long doctorId,
                                                                 @NotNull Date dueDate,
                                                                 @NotNull AppointmentStatus appointmentStatus);
}
