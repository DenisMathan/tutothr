package tutothr.booking;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

/**
 * Service zur Integration mit Google Calendar.
 * Ermoeglicht das Hinzufuegen von Buchungsterminen zum Kalender des Users.
 */
@Service
public class CalendarService {
	
	// === Felder ===
	
	private final OAuth2AuthorizedClientService authorizedClientService;
	
	// === Konstruktor ===
	
	public CalendarService(OAuth2AuthorizedClientService authorizedClientService) {
		this.authorizedClientService = authorizedClientService;
	}
	
	// === Kalender-Operationen ===
	
	/**
	 * Fuegt einen Termin zum Google Calendar des eingeloggten Users hinzu.
	 * 
	 * @param authentication OAuth2-Token des eingeloggten Users
	 * @param title Titel des Kalendereintrags
	 * @param description Beschreibung des Termins
	 * @param date Datum des Termins
	 * @param startTime Startzeit
	 * @param endTime Endzeit
	 * @throws Exception wenn kein Access Token vorhanden oder API-Fehler
	 */
	public void addEventToGoogleCalendar(
			OAuth2AuthenticationToken authentication,
			String title,
			String description,
			LocalDate date,
			LocalTime startTime,
			LocalTime endTime) throws Exception {
		
		// Access Token holen
		Calendar service = buildCalendarService(authentication);
		
		// Event erstellen
		Event event = createEvent(title, description, date, startTime, endTime);
		
		// In den primaeren Kalender einfuegen
		service.events().insert("primary", event).execute();
	}
	
	// === Private Hilfsmethoden ===
	
	/**
	 * Erstellt einen authentifizierten Google Calendar Service.
	 */
	private Calendar buildCalendarService(OAuth2AuthenticationToken authentication) {
		OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
				authentication.getAuthorizedClientRegistrationId(),
				authentication.getName()
		);
		
		String accessToken = client.getAccessToken().getTokenValue();
		
		if (accessToken == null) {
			throw new RuntimeException("Kein Google Access Token vorhanden. Bitte neu einloggen.");
		}
		
		GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
		
		return new Calendar.Builder(
				new NetHttpTransport(),
				GsonFactory.getDefaultInstance(),
				credential)
				.setApplicationName("TutOTHr")
				.build();
	}
	
	/**
	 * Erstellt ein Google Calendar Event mit den angegebenen Daten.
	 */
	private Event createEvent(String title, String description, LocalDate date, 
			LocalTime startTime, LocalTime endTime) {
		
		Event event = new Event()
				.setSummary(title)
				.setDescription(description);
		
		// Start-Zeit
		LocalDateTime start = date.atTime(startTime);
		DateTime startDateTime = new DateTime(
				Date.from(start.atZone(ZoneId.of("Europe/Berlin")).toInstant()));
		event.setStart(new EventDateTime().setDateTime(startDateTime));
		
		// End-Zeit
		LocalDateTime end = date.atTime(endTime);
		DateTime endDateTime = new DateTime(
				Date.from(end.atZone(ZoneId.of("Europe/Berlin")).toInstant()));
		event.setEnd(new EventDateTime().setDateTime(endDateTime));
		
		return event;
	}
}