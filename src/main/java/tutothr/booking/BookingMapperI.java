package tutothr.booking;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookingMapperI {
	@Mapping(source = "student.id", target = "studentId")
	@Mapping(source = "student.username", target = "studentName")
	@Mapping(source = "timeSlot.id", target = "timeSlotId")
	@Mapping(source = "course.id", target = "courseId")
	@Mapping(source = "course.title", target = "courseName")
	@Mapping(source = "invoice.id", target = "invoiceId")
	BookingDTO toDTO(Booking entity);
	
	@Mapping(source = "studentId", target = "student.id")
	@Mapping(source = "timeSlotId", target = "timeSlot.id")
	@Mapping(source = "courseId", target = "course.id")
	Booking toEntity(BookingDTO dto);
}
