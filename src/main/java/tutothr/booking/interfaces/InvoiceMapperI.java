package tutothr.booking.interfaces;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import tutothr.booking.Invoice;
import tutothr.booking.InvoiceDTO;

@Mapper(componentModel = "spring")
public interface InvoiceMapperI {
	@Mapping(source = "booking.id", target = "bookingId")
	InvoiceDTO toDTO(Invoice entity);
	
	@Mapping(source = "bookingId", target = "booking.id")
	Invoice toEntity(InvoiceDTO dto);
}
