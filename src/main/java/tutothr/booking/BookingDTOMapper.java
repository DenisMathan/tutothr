package tutothr.booking;

/**
 * Interface fuer Mapper, die Booking-Subtypen in BookingDTO umwandeln.
 * Jeder Buchungstyp (Kurs, Kapitel, Tutorium) hat einen eigenen Mapper.
 */
public interface BookingDTOMapper {
	boolean supports(Booking booking);
	void fillDTO(Booking booking, BookingDTO dto);
}