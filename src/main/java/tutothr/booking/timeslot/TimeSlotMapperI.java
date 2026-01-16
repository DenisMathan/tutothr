package tutothr.booking.timeslot;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TimeSlotMapperI {
	@Mapping(source = "tutor.id", target = "tutorId")
	TimeSlotDTO toDTO(TimeSlot entity);
	
	@Mapping(source = "tutorId", target = "tutor.id")
	TimeSlot toEntity(TimeSlotDTO dto);
}
