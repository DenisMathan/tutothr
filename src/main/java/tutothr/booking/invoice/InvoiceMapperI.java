package tutothr.booking.invoice;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InvoiceMapperI {
	@Mapping(source = "booking.id", target = "bookingId")
	InvoiceDTO toDTO(Invoice entity);
	
	@Mapping(source = "bookingId", target = "booking.id")
	Invoice toEntity(InvoiceDTO dto);
}
