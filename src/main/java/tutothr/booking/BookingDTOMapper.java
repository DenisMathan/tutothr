package tutothr.booking;

public interface BookingDTOMapper {
    boolean supports(Booking booking);
    void fillDTO(Booking booking, BookingDTO dto);
}