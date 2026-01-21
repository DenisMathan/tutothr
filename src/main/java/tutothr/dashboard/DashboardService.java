package tutothr.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import tutothr.course.interfaces.CourseRepositoryI;
import tutothr.message.interfaces.MessageRepositoryI;
import tutothr.user.User;
import tutothr.booking.BookingRepositoryI;
import tutothr.common.utils.enums.RolesEnum;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DashboardService {

    @Autowired
    private MessageRepositoryI messageRepository;

    @Autowired
    private CourseRepositoryI courseRepository;

    @Autowired
    private BookingRepositoryI bookingRepository;

    public DashboardDTO getStatsForUser(User user) {
        DashboardDTO dto = new DashboardDTO();

        // ungelesene Nachrichten (bei allen)
        dto.setUnreadMessages(messageRepository.countByReceiverIdAndReadFalse(user.getId()));

        boolean isTutor = user.getRoles().contains(RolesEnum.TUTOR);
        boolean isStudent = user.getRoles().contains(RolesEnum.STUDENT);

        // ====TUTOR STATISTIK====
        if (isTutor) {
            // Anzahl aktiver Kurse
            dto.setActiveCourses(courseRepository.countByOwner_Id(user.getId()));

            // Durchschnittsbewertung über alle Kurse
            Double avg = courseRepository.getAverageRatingByTutor(user.getId());
            dto.setAverageRating(avg != null ? avg : 0.0);
            
            // Anzahl erhaltener Buchungen
            dto.setReceivedBookings(bookingRepository.countByTutor(user));

            // Gesamtumsatz
            Double courseRevenue = bookingRepository.calculateCourseRevenue(user.getId());
            Double chapterRevenue = bookingRepository.calculateChapterRevenue(user.getId());
            Double timeSlotRevenue = bookingRepository.calculateTimeSlotRevenue(user.getId());
            Double revenue = courseRevenue + chapterRevenue + timeSlotRevenue;
            dto.setTotalRevenue(revenue != null ? revenue : 0.0);

            // best-performender Kurs (höchster Umsatz)
            List<Object[]> bestCourse = bookingRepository.findBestPerformingCourse(user, PageRequest.of(0, 1));
            if (!bestCourse.isEmpty()) {
                Object[] row = bestCourse.get(0);
                dto.setBestCourseTitle((String) row[0]);
                dto.setBestCourseRevenue(row[1] != null ? ((Number) row[1]).doubleValue() : 0.0);
            }

            // Kurs mit den meisten Buchungen
            List<Object[]> mostBooked = bookingRepository.findMostBookedCourse(user, PageRequest.of(0, 1));
            if (!mostBooked.isEmpty()) {
                Object[] row = mostBooked.get(0);
                dto.setMostBookedCourseTitle((String) row[0]);
                dto.setMostBookedCourseCount(row[1] != null ? ((Number) row[1]).longValue() : 0L);
            }

            // best-bewerteter Kurs
            List<Object[]> bestRated = courseRepository.findBestRatedCourseByTutor(user.getId(), PageRequest.of(0, 1));
            if (!bestRated.isEmpty()) {
                Object[] row = bestRated.get(0);
                dto.setBestRatedCourseTitle((String) row[0]);
                dto.setBestRatedCourseRating(row[1] != null ? ((Number) row[1]).doubleValue() : 0.0);
                dto.setBestRatedCourseReviews(row[2] != null ? ((Number) row[2]).longValue() : 0L);
            }
        }

        // ====STUDENT STATISTIK====
        if (isStudent) {
            // Meine Buchungen
            dto.setMyBookings(bookingRepository.countByStudent(user));

            // Gesamtausgaben
            Double totalSpent = bookingRepository.calculateTotalSpent(user);
            dto.setTotalSpent(totalSpent != null ? totalSpent : 0.0);

            // Ausgaben im letzten Monat
            LocalDateTime oneMonthAgo = LocalDateTime.now().minusDays(30);
            Double lastMonth = bookingRepository.calculateSpentSince(user, oneMonthAgo);
            dto.setSpentLastMonth(lastMonth != null ? lastMonth : 0.0);
        }

        return dto;
    }
}