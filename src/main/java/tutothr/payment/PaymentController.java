package tutothr.payment;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import tutothr.booking.BookingDTO;
import tutothr.booking.BookingService;
import tutothr.booking.BookingStatus;
import tutothr.booking.invoice.InvoiceService;

@Controller
public class PaymentController {
	private final PayPalService payPalService;
	private final BookingService bookingService;
	private final InvoiceService invoiceService;
	
	public PaymentController(PayPalService payPalService, BookingService bookingService, InvoiceService invoiceService) {
		this.payPalService = payPalService;
		this.bookingService = bookingService;
		this.invoiceService = invoiceService;
	}
	
	@GetMapping("/booking/{bookingId}/pay")
	public String startPayment(@PathVariable Long bookingId) {
		BookingDTO booking = bookingService.findById(bookingId);
		
		String returnUrl = "http://localhost:8080/payment/success?bookingId=" + bookingId;
		String cancelUrl = "http://localhost:8080/payment/cancel";
		
		try {
			String approvalUrl = payPalService.createPayment(booking.getPrice(), bookingId, returnUrl, cancelUrl);
			return "redirect:" + approvalUrl;
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/my-bookings?error=payment";
		}
	}
	
	@GetMapping("/payment/success")
	public String paymentSuccess(@RequestParam String token, @RequestParam Long bookingId) {
		boolean success = payPalService.capturePayment(token);
		
		if (success) {
			bookingService.updateStatus(bookingId, BookingStatus.CONFIRMED);
			invoiceService.createBillForBooking(bookingId);
			return "redirect:/my-bookings?success=payment";
		} else {
			return "redirect:/my-bookings?error=capture";
		}
	}
	
	@GetMapping("/payment/cancel")
	public String paymentCancel() {
		return "redirect:/my-bookings?cancelled=true";
	}
}
