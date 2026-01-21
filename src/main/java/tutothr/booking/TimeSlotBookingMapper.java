package tutothr.booking;

import org.springframework.stereotype.Component;

/**
 * Mapper fuer TimeSlotBooking zu BookingDTO.
 */
@Component
public class TimeSlotBookingMapper implements BookingDTOMapper {

	@Override
	public boolean supports(Booking booking) {
		return booking instanceof TimeSlotBooking;
	}

	@Override
	public void fillDTO(Booking booking, BookingDTO dto) {
		TimeSlotBooking tsb = (TimeSlotBooking) booking;
		dto.setBookingType("TIMESLOT");
		dto.setCourseId(tsb.getCourse().getId());
		dto.setTimeSlotId(tsb.getTimeSlot().getId());
	}
}