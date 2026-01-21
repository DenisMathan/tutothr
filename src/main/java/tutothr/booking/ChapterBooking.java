package tutothr.booking;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import tutothr.booking.timeslot.TimeSlot;
import tutothr.chapter.Chapter;
import tutothr.user.User;

@Entity
@DiscriminatorValue("CHAPTER")
public class ChapterBooking extends Booking {
    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;

    public ChapterBooking() {
        
    }

    public ChapterBooking(User student, Chapter chapter, float price) {
        super(student, price);
        this.chapter = chapter;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }

    @Override
    public String getBookingDescription() {
        return "Kapitel: " + chapter.getTitle();
    }
    
    @Override
    public User getTutor() {
        return chapter.getCourse().getOwner();
    }
    
    @Override
    public TimeSlot cleanup() {
        return null;
    }
}