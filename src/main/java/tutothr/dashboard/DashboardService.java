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

        dto.setUnreadMessages(messageRepository.countByReceiverIdAndReadFalse(user.getId()));

        boolean isTutor = user.getRoles().contains(RolesEnum.TUTOR);
        boolean isStudent = user.getRoles().contains(RolesEnum.STUDENT);

        if (isTutor) {
            dto.setActiveCourses(courseRepository.countByOwner_Id(user.getId()));

            Double avg = courseRepository.getAverageRatingByTutor(user.getId());
            dto.setAverageRating(avg != null ? avg : 0.0);

            dto.setReceivedBookings(bookingRepository.countByTutor(user));

            Double revenue = bookingRepository.calculateTotalRevenue(user);
            dto.setTotalRevenue(revenue != null ? revenue : 0.00);

            List<Object[]> bestCourse = bookingRepository.findBestPerformingCourse(user, PageRequest.of(0, 1));
            if (!bestCourse.isEmpty()) {
                Object[] row = bestCourse.get(0);
                dto.setBestCourseTitle((String) row[0]);
                dto.setBestCourseRevenue((Double) row[1]);
            }
        }

        if (isStudent) {
            dto.setMyBookings(bookingRepository.countByStudent(user));

            Double totalSpent = bookingRepository.calculateTotalSpent(user);
            dto.setTotalSpent(totalSpent != null ? totalSpent : 0.00);

            LocalDateTime oneMonthAgo = LocalDateTime.now().minusDays(30);
            Double lastMonth = bookingRepository.calculateSpentSince(user, oneMonthAgo);
            dto.setSpentLastMonth(lastMonth != null ? lastMonth : 0.00);
        }

        return dto;
    }
}