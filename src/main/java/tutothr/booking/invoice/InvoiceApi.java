package tutothr.booking.invoice;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tutothr.auth.config.AppPrincipal;
import tutothr.user.User;
import tutothr.user.UserService;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceApi {

    private final InvoiceService invoiceService;
    private final InvoiceRepositoryI invoiceRepository;
    private final InvoiceMapperI invoiceMapper;
    private final UserService userService;

    public InvoiceApi(InvoiceService invoiceService, InvoiceRepositoryI invoiceRepository,
                      InvoiceMapperI invoiceMapper, UserService userService) {
        this.invoiceService = invoiceService;
        this.invoiceRepository = invoiceRepository;
        this.invoiceMapper = invoiceMapper;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<InvoiceDTO>> getMyInvoices(@AuthenticationPrincipal AppPrincipal principal) {
        User user = userService.findById(principal.getId());
        
        List<Invoice> invoices = invoiceRepository.findByStudentOrTutor(user.getUsername());
        List<InvoiceDTO> dtos = invoices.stream()
                .map(invoiceMapper::toDTO)
                .toList();
        
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceDTO> getInvoiceById(@PathVariable Long id, 
                                                      @AuthenticationPrincipal AppPrincipal principal) {
        InvoiceDTO invoice = invoiceService.findById(id);
        
        if (invoice == null) {
            return ResponseEntity.notFound().build();
        }
        
        User user = userService.findById(principal.getId());
        if (!invoice.getStudentName().equals(user.getUsername()) && 
            !invoice.getTutorName().equals(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        return ResponseEntity.ok(invoice);
    }

    @GetMapping("/number/{invoiceNumber}")
    public ResponseEntity<InvoiceDTO> getInvoiceByNumber(@PathVariable String invoiceNumber,
                                                          @AuthenticationPrincipal AppPrincipal principal) {
        Invoice invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber).orElse(null);
        
        if (invoice == null) {
            return ResponseEntity.notFound().build();
        }
        
        User user = userService.findById(principal.getId());
        if (!invoice.getStudentName().equals(user.getUsername()) && 
            !invoice.getTutorName().equals(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        return ResponseEntity.ok(invoiceMapper.toDTO(invoice));
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadInvoicePdf(@PathVariable Long id,
                                                      @AuthenticationPrincipal AppPrincipal principal) {
        InvoiceDTO invoice = invoiceService.findById(id);
        
        if (invoice == null) {
            return ResponseEntity.notFound().build();
        }
        
        User user = userService.findById(principal.getId());
        boolean isTutor = invoice.getTutorName().equals(user.getUsername());
        boolean isStudent = invoice.getStudentName().equals(user.getUsername());
        
        if (!isTutor && !isStudent) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        byte[] pdfBytes = invoiceService.generatePdf(id, isTutor);
        
        if (pdfBytes == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", 
                "invoice-" + invoice.getInvoiceNumber() + ".pdf");
        headers.setContentLength(pdfBytes.length);
        
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}
