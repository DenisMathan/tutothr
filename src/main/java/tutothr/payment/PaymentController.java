package tutothr.payment;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import tutothr.booking.BookingDTO;
import tutothr.booking.BookingService;
import tutothr.booking.BookingStatus;
import tutothr.booking.invoice.InvoiceDTO;
import tutothr.booking.invoice.InvoiceService;
import tutothr.common.services.MailService;

@Controller
public class PaymentController {
	private final PayPalService payPalService;
	private final BookingService bookingService;
	private final InvoiceService invoiceService;
	private final MailService mailService;

	public PaymentController(PayPalService payPalService, BookingService bookingService, InvoiceService invoiceService,
			MailService mailService) {
		this.payPalService = payPalService;
		this.bookingService = bookingService;
		this.invoiceService = invoiceService;
		this.mailService = mailService;
	}

	@GetMapping("/booking/{bookingId}/pay")
	public String startPayment(@PathVariable Long bookingId) {
		BookingDTO booking = bookingService.findById(bookingId);

		String returnUrl = "http://localhost:8080/payment/success?bookingId=" + bookingId;
		String cancelUrl = "http://localhost:8080/payment/cancel?bookingId=" + bookingId;

		try {
			String approvalUrl = payPalService.createPayment(booking.getPrice(), bookingId, returnUrl, cancelUrl);
			return "redirect:" + approvalUrl;
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/my-bookings?error=payment";
		}
	}

	@GetMapping("/payment/success")
	public String paymentSuccess(@RequestParam String token, @RequestParam Long bookingId, 
			RedirectAttributes redirectAttributes) {
		boolean success = payPalService.capturePayment(token);

		if (success) {
			bookingService.updateStatus(bookingId, BookingStatus.CONFIRMED);
			InvoiceDTO invoice = invoiceService.createBillForBooking(bookingId);
			
			// E-Mail-Bestaetigungen senden
	        BookingDTO booking = bookingService.findById(bookingId);
	        mailService.sendBookingConfirmationToStudent(booking, invoice);
	        mailService.sendBookingConfirmationToTutor(booking);
			
	        redirectAttributes.addFlashAttribute("success", "Zahlung erfolgreich!");
	        return "redirect:/my-bookings";
		} else {
			redirectAttributes.addFlashAttribute("error", "Zahlung fehlgeschlagen.");
			return "redirect:/my-bookings";
		}
	}

	@GetMapping("/payment/cancel")
	public String paymentCancel(@RequestParam Long bookingId) {
	    // Buchung und TimeSlot aufraeumen
	    BookingDTO booking = bookingService.findById(bookingId);
	    if (booking != null) {
	        bookingService.cancelAndCleanup(bookingId);
	    }
	    return "redirect:/courses?cancelled=true";
	}
}
