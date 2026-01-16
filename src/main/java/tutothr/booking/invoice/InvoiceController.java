package tutothr.booking.invoice;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class InvoiceController {
	private final InvoiceService invoiceService;
	
	public InvoiceController(InvoiceService invoiceService) {
		this.invoiceService = invoiceService;
	}
	
	// Student: Rechnung herunterladen
	@GetMapping("/invoice/{invoiceId}/download")
	public ResponseEntity<byte[]> downloadInvoice(@PathVariable Long invoiceId) {
		return buildPdfResponse(invoiceId, false, "rechnung");
	}
	
	// Tutor: Einnahmenbeleg herunterladen
	@GetMapping("/tutor/invoice/{invoiceId}/download")
	public ResponseEntity<byte[]> downloadTutorBill(@PathVariable Long invoiceId) {
		return buildPdfResponse(invoiceId, true, "einnahmenbeleg");
	}
	
	private ResponseEntity<byte[]> buildPdfResponse(Long invoiceId, boolean isTutorView, String filenamePrefix) {
		InvoiceDTO invoice = invoiceService.findById(invoiceId);
		if (invoice == null) {
			return ResponseEntity.notFound().build();
		}
		
		byte[] pdf = invoiceService.generatePdf(invoiceId, isTutorView);
		if (pdf == null) {
			return ResponseEntity.notFound().build();
		}
		
		String filename	= filenamePrefix + "-" + invoice.getInvoiceNumber() + ".pdf";
		return ResponseEntity.ok()
				.header("Content-Disposition", "attachment; filename=" + filename)
				.contentType(MediaType.APPLICATION_PDF)
				.body(pdf);
	}
}
