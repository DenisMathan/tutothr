package tutothr.booking.interfaces;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import tutothr.booking.TimeSlot;
import tutothr.booking.TimeSlotDTO;

@Mapper(componentModel = "spring")
public interface TimeSlotMapperI {
	@Mapping(source = "tutor.id", target = "tutorId")
	TimeSlotDTO toDTO(TimeSlot entity);
	
	@Mapping(source = "tutorId", target = "tutor.id")
	TimeSlot toEntity(TimeSlotDTO dto);
}
