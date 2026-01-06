package tutothr.booking;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import tutothr.booking.interfaces.InvoiceMapperI;
import tutothr.booking.interfaces.InvoiceRepositoryI;
import tutothr.booking.interfaces.BookingRepositoryI;

@Service
public class InvoiceService {
	private final InvoiceRepositoryI invoiceRepository;
	private final BookingRepositoryI bookingRepository;
	private final InvoiceMapperI mapper;

	public InvoiceService(InvoiceRepositoryI invoiceRepository, BookingRepositoryI bookingRepository,
			InvoiceMapperI mapper) {
		this.invoiceRepository = invoiceRepository;
		this.bookingRepository = bookingRepository;
		this.mapper = mapper;
	}

	public InvoiceDTO findById(Long id) {
		return invoiceRepository.findById(id).map(mapper::toDTO).orElse(null);
	}

	public InvoiceDTO findByBookingId(Long bookingId) {
		Booking booking = bookingRepository.findById(bookingId).orElse(null);
		if (booking == null) {
			return null;
		}
		return invoiceRepository.findByBooking(booking).map(mapper::toDTO).orElse(null);
	}

	public InvoiceDTO createBillForBooking(Long bookingId) {
		Booking booking = bookingRepository.findById(bookingId).orElse(null);
		if (booking == null) {
			return null;
		}

		// Pruefen ob schon eine Invoice existiert
		if (invoiceRepository.findByBooking(booking).isPresent()) {
			return null; // Oder Exception werfen
		}

		// Bill mit Snapshot-Daten erstellen
		Invoice invoice = new Invoice(generateInvoiceNumber(), booking, booking.getStudent().getUsername(),
				booking.getTimeSlot().getTutor().getUsername(), booking.getCourse().getTitle(), booking.getPrice(),
				LocalDateTime.now());

		booking.setInvoice(invoice);
		Invoice saved = invoiceRepository.save(invoice);
		return mapper.toDTO(saved);
	}

	private String generateInvoiceNumber() {
		int year = LocalDateTime.now().getYear();
		long countThisYear = invoiceRepository.countByYear(year) + 1;
		return String.format("%d-%04d", year, countThisYear);
	}

	public byte[] generatePdf(Long invoiceId, boolean isTutorView) {
		Invoice invoice = invoiceRepository.findById(invoiceId).orElse(null);
		if (invoice == null) {
			return null;
		}
		return generatePdfFromInvoice(invoice, isTutorView);
	}

	private byte[] generatePdfFromInvoice(Invoice invoice, boolean isTutorView) {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Document document = new Document();
			PdfWriter.getInstance(document, out);
			document.open();

			// Fonts
			Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
			Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
			Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

			// Titel
			String title = isTutorView ? "Einnahmenbeleg" : "Rechnung";
			Paragraph titlePara = new Paragraph(title, titleFont);
			titlePara.setAlignment(Element.ALIGN_CENTER);
			titlePara.setSpacingAfter(20);
			document.add(titlePara);

			// Belegnummer und Datum
			document.add(new Paragraph("Belegnummer: " + invoice.getInvoiceNumber(), normalFont));
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
			document.add(new Paragraph("Datum: " + invoice.getPaidAt().format(formatter), normalFont));
			document.add(new Paragraph(" ")); // Leerzeile

			// Empfaenger
			document.add(new Paragraph("Empfaenger: ", headerFont));
			document.add(new Paragraph(isTutorView ? invoice.getTutorName() : invoice.getStudentName(), normalFont));
			document.add(new Paragraph(" "));

			// Transaktionspartner
			String label = isTutorView ? "Erhalten von: " : "Bezahlt an: ";
			String name = isTutorView ? invoice.getStudentName() : invoice.getTutorName();
			document.add(new Paragraph(label, headerFont));
			document.add(new Paragraph(name, normalFont));
			document.add(new Paragraph(" "));

			// Tabelle mit Buchungsdetails
			PdfPTable table = new PdfPTable(2);
			table.setWidthPercentage(100);
			table.setSpacingBefore(10);

			addTableRow(table, "Kurs: ", invoice.getCourseName(), headerFont, normalFont);
			addTableRow(table, "Preis: ", String.format("%.2f EUR", invoice.getPrice()), headerFont, normalFont);

			document.add(table);

			document.close();
			return out.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private void addTableRow(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
		PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
		labelCell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(labelCell);

		PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));
		valueCell.setBorder(PdfPCell.NO_BORDER);
		table.addCell(valueCell);
	}
}
