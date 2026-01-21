package tutothr.booking;

import org.springframework.stereotype.Component;

/**
 * Mapper fuer CourseBooking zu BookingDTO.
 */
@Component
public class CourseBookingMapper implements BookingDTOMapper {

	@Override
	public boolean supports(Booking booking) {
		return booking instanceof CourseBooking;
	}

	@Override
	public void fillDTO(Booking booking, BookingDTO dto) {
		CourseBooking cb = (CourseBooking) booking;
		dto.setBookingType("COURSE");
		dto.setCourseId(cb.getCourse().getId());
	}
}