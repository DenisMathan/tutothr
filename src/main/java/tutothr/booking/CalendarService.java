package tutothr.booking;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.client.util.DateTime;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class CalendarService {

    private final OAuth2AuthorizedClientService authorizedClientService;

    public CalendarService(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    public void addEventToGoogleCalendar(OAuth2AuthenticationToken authentication, String title, String description, LocalDate date, LocalTime startTime, LocalTime endTime) throws Exception {

        // 1. Access Token des eingeloggten Users holen
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName()
        );

        String accessToken = client.getAccessToken().getTokenValue();

        if (accessToken == null) {
            throw new RuntimeException("Kein Google Access Token vorhanden. Bitte neu einloggen.");
        }

        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);

        Calendar service = new Calendar.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                credential)
                .setApplicationName("UniProjektApp")
                .build();

        Event event = new Event()
                .setSummary(title)
                .setDescription(description);

        // Zeit-Konvertierung (LocalTime -> Google DateTime).
        LocalDateTime start = date.atTime(startTime);
        LocalDateTime end = date.atTime(endTime);

        DateTime startDateTime = new DateTime(Date.from(start.atZone(ZoneId.of("Europe/Berlin")).toInstant()));
        EventDateTime startEventDateTime = new EventDateTime().setDateTime(startDateTime);
        event.setStart(startEventDateTime);

        DateTime endDateTime = new DateTime(Date.from(end.atZone(ZoneId.of("Europe/Berlin")).toInstant()));
        EventDateTime endEventDateTime = new EventDateTime().setDateTime(endDateTime);
        event.setEnd(endEventDateTime);

        service.events().insert("primary", event).execute();
    }
}
