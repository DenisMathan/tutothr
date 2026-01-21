package tutothr.booking;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import tutothr.booking.timeslot.TimeSlot;
import tutothr.chapter.Chapter;
import tutothr.user.User;

/**
 * Buchung fuer ein einzelnes Kapitel.
 */
@Entity
@DiscriminatorValue("CHAPTER")
public class ChapterBooking extends Booking {
	
	// === Felder ===
	
	@ManyToOne
	@JoinColumn(name = "chapter_id")
	private Chapter chapter;

	// === Konstruktoren ===
	
	public ChapterBooking() {
	}

	public ChapterBooking(User student, Chapter chapter, float price) {
		super(student, price);
		this.chapter = chapter;
	}

	// === Getter und Setter ===
	
	public Chapter getChapter() {
		return chapter;
	}

	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
	}

	// === Ueberschriebene Methoden ===
	
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