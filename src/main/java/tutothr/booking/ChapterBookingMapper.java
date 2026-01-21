package tutothr.booking;

import org.springframework.stereotype.Component;

/**
 * Mapper fuer ChapterBooking zu BookingDTO.
 */
@Component
public class ChapterBookingMapper implements BookingDTOMapper {
	
	@Override
	public boolean supports(Booking booking) {
		return booking instanceof ChapterBooking;
	}

	@Override
	public void fillDTO(Booking booking, BookingDTO dto) {
		ChapterBooking cb = (ChapterBooking) booking;
		dto.setBookingType("CHAPTER");
		dto.setChapterId(cb.getChapter().getId());
		dto.setCourseId(cb.getChapter().getCourse().getId());
	}
}