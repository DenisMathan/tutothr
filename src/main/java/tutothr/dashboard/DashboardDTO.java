package tutothr.dashboard;

public class DashboardDTO {

    private long unreadMessages;

    private Long activeCourses;
    private Double averageRating;
    private Long receivedBookings;
    private Double totalRevenue;

    private String bestCourseTitle;
    private Double bestCourseRevenue;

    private Long myBookings;

    private Double totalSpent;
    private Double spentLastMonth;

    public DashboardDTO() {}


    public String getBestCourseTitle() { return bestCourseTitle; }
    public void setBestCourseTitle(String bestCourseTitle) { this.bestCourseTitle = bestCourseTitle; }

    public Double getBestCourseRevenue() { return bestCourseRevenue; }
    public void setBestCourseRevenue(Double bestCourseRevenue) { this.bestCourseRevenue = bestCourseRevenue; }

    public Double getTotalSpent() { return totalSpent; }
    public void setTotalSpent(Double totalSpent) { this.totalSpent = totalSpent; }

    public Double getSpentLastMonth() { return spentLastMonth; }
    public void setSpentLastMonth(Double spentLastMonth) { this.spentLastMonth = spentLastMonth; }

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
    public Long getMyBookings() { return myBookings; }
    public void setMyBookings(Long myBookings) { this.myBookings = myBookings; }
}