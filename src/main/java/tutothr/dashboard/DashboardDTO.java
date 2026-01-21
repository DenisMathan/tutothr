package tutothr.dashboard;

public class DashboardDTO {

    private long unreadMessages;

    // Tutor Stats
    private Long activeCourses;
    private Double averageRating;
    private Long receivedBookings;
    private Double totalRevenue;

    // Best performing course (by revenue)
    private String bestCourseTitle;
    private Double bestCourseRevenue;
    private Long bestCourseBookings;

    // Most booked course
    private String mostBookedCourseTitle;
    private Long mostBookedCourseCount;

    // Best rated course
    private String bestRatedCourseTitle;
    private Double bestRatedCourseRating;
    private Long bestRatedCourseReviews;

    // Student Stats
    private Long myBookings;
    private Double totalSpent;
    private Double spentLastMonth;

    public DashboardDTO() {}

    // Getters and Setters

    public long getUnreadMessages() { return unreadMessages; }
    public void setUnreadMessages(long unreadMessages) { this.unreadMessages = unreadMessages; }

    public Long getActiveCourses() { return activeCourses; }
    public void setActiveCourses(Long activeCourses) { this.activeCourses = activeCourses; }

    public Double getAverageRating() { return averageRating; }
    public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }

    public Long getReceivedBookings() { return receivedBookings; }
    public void setReceivedBookings(Long receivedBookings) { this.receivedBookings = receivedBookings; }

    public Double getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(Double totalRevenue) { this.totalRevenue = totalRevenue; }

    public String getBestCourseTitle() { return bestCourseTitle; }
    public void setBestCourseTitle(String bestCourseTitle) { this.bestCourseTitle = bestCourseTitle; }

    public Double getBestCourseRevenue() { return bestCourseRevenue; }
    public void setBestCourseRevenue(Double bestCourseRevenue) { this.bestCourseRevenue = bestCourseRevenue; }

    public Long getBestCourseBookings() { return bestCourseBookings; }
    public void setBestCourseBookings(Long bestCourseBookings) { this.bestCourseBookings = bestCourseBookings; }

    public String getMostBookedCourseTitle() { return mostBookedCourseTitle; }
    public void setMostBookedCourseTitle(String mostBookedCourseTitle) { this.mostBookedCourseTitle = mostBookedCourseTitle; }

    public Long getMostBookedCourseCount() { return mostBookedCourseCount; }
    public void setMostBookedCourseCount(Long mostBookedCourseCount) { this.mostBookedCourseCount = mostBookedCourseCount; }

    public String getBestRatedCourseTitle() { return bestRatedCourseTitle; }
    public void setBestRatedCourseTitle(String bestRatedCourseTitle) { this.bestRatedCourseTitle = bestRatedCourseTitle; }

    public Double getBestRatedCourseRating() { return bestRatedCourseRating; }
    public void setBestRatedCourseRating(Double bestRatedCourseRating) { this.bestRatedCourseRating = bestRatedCourseRating; }

    public Long getBestRatedCourseReviews() { return bestRatedCourseReviews; }
    public void setBestRatedCourseReviews(Long bestRatedCourseReviews) { this.bestRatedCourseReviews = bestRatedCourseReviews; }

    public Long getMyBookings() { return myBookings; }
    public void setMyBookings(Long myBookings) { this.myBookings = myBookings; }

    public Double getTotalSpent() { return totalSpent; }
    public void setTotalSpent(Double totalSpent) { this.totalSpent = totalSpent; }

    public Double getSpentLastMonth() { return spentLastMonth; }
    public void setSpentLastMonth(Double spentLastMonth) { this.spentLastMonth = spentLastMonth; }
}