package tutothr.booking.timeslot;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TimeSlotMapperI {
	@Mapping(source = "tutor.id", target = "tutorId")
	TimeSlotDTO toDTO(TimeSlot entity);
	
	@Mapping(source = "tutorId", target = "tutor.id")
	TimeSlot toEntity(TimeSlotDTO dto);
}
