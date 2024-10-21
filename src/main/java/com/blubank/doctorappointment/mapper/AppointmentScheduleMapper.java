package com.blubank.doctorappointment.mapper;

import com.blubank.doctorappointment.dto.AppointmentScheduleDTO;
import com.blubank.doctorappointment.entity.AppointmentSchedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AppointmentScheduleMapper extends GenericMapper<AppointmentSchedule, AppointmentScheduleDTO> {
    AppointmentScheduleMapper INSTANCE = Mappers.getMapper(AppointmentScheduleMapper.class);

    @Mapping(source = "doctor.id",target = "doctorId")
    @Override
    AppointmentScheduleDTO toDTO(AppointmentSchedule appointmentSchedule);

    @Mapping(source = "doctorId",target = "doctor.id")
    @Override
    AppointmentSchedule toEntity(AppointmentScheduleDTO appointmentScheduleDTO);

    @Override
    List<AppointmentScheduleDTO> toDtoList(List<AppointmentSchedule> list);

    @Override
    List<AppointmentSchedule> toEntityList(List<AppointmentScheduleDTO> list);
}
