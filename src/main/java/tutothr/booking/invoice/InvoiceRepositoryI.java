//package tutothr.booking.invoice;
//
//import java.util.Optional;
//
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import tutothr.booking.Booking;
//import tutothr.common.MyBaseRepository;
//
//public interface InvoiceRepositoryI extends MyBaseRepository<Invoice, Long> {
//	Optional<Invoice> findByBooking(Booking booking);
//	
//	Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
//	
//	@Query("SELECT COUNT(i) FROM Invoice i WHERE YEAR(i.paidAt) = :year")
//	long countByYear(@Param("year") int year);
//}

package tutothr.booking.invoice;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tutothr.booking.Booking;
import tutothr.common.MyBaseRepository;

public interface InvoiceRepositoryI extends MyBaseRepository<Invoice, Long> {
	Optional<Invoice> findByBooking(Booking booking);
	
	Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
	
	@Query("SELECT COUNT(i) FROM Invoice i WHERE YEAR(i.paidAt) = :year")
	long countByYear(@Param("year") int year);
	
	@Query("SELECT i FROM Invoice i WHERE i.studentName = :username OR i.tutorName = :username ORDER BY i.paidAt DESC")
	List<Invoice> findByStudentOrTutor(@Param("username") String username);
}

