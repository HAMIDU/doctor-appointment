package com.blubank.doctorappointment.mapper;

import com.blubank.doctorappointment.dto.ReservedAppointmentDTO;
import com.blubank.doctorappointment.entity.ReservedAppointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ReservedAppointmentMapper extends GenericMapper<ReservedAppointment, ReservedAppointmentDTO> {
    ReservedAppointmentMapper INSTANCE = Mappers.getMapper(ReservedAppointmentMapper.class);

    @Mapping(source = "appointmentSchedule.doctor.id",target = "doctorId")
    @Mapping(source = "id",target = "reservationId")
    @Mapping(source = "appointmentSchedule.fromTime",target = "appointmentScheduleFromTime")
    @Mapping(source = "appointmentSchedule.toTime",target = "appointmentScheduleToTime")
    @Override
    ReservedAppointmentDTO toDTO(ReservedAppointment reservedAppointment);


    @Override
    ReservedAppointment toEntity(ReservedAppointmentDTO reservedAppointmentDTO);

    @Override
    List<ReservedAppointmentDTO> toDtoList(List<ReservedAppointment> list);

    @Override
    List<ReservedAppointment> toEntityList(List<ReservedAppointmentDTO> list);
}
