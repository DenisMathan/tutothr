package tutothr.booking;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

/**
 * Service zur Pruefung von Zugriffsrechten auf Kurse und Kapitel.
 */
@Service
public class ContentAccessService {
	
	// === Felder ===
	
	private final BookingRepositoryI bookingRepository;
	
	// === Konstruktor ===
	
	public ContentAccessService(BookingRepositoryI bookingRepository) {
		this.bookingRepository = bookingRepository;
	}
	
	// === Zugriffspruefungen ===
	
	/**
	 * Prueft, ob der User Zugriff auf ein Kapitel hat.
	 */
	public boolean canAccessChapter(Long userId, Long chapterId, Long courseId, boolean isPaywalled) {
		// Nicht paywalled → immer Zugriff
		if (!isPaywalled) {
			return true;
		}
		// Kurs gekauft → Zugriff auf alle Kapitel
		if (bookingRepository.existsByStudentIdAndCourseId(userId, courseId)) {
			return true;
		}
		// Einzelnes Kapitel gekauft
		return bookingRepository.existsByStudentIdAndChapterId(userId, chapterId);
	}
	
	/**
	 * Prueft, ob der User ein Kapitel kaufen kann.
	 */
	public boolean canPurchaseChapter(Long userId, Long chapterId, Long courseId, boolean isPaywalled) {
		// Nicht paywalled → nicht kaufbar
		if (!isPaywalled) {
			return false;
		}
		// Schon Zugriff → nicht nochmal kaufbar
		if (canAccessChapter(userId, chapterId, courseId, true)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Holt alle Chapter-IDs, auf die der User Zugriff hat (Batch-Abfrage).
	 * Gibt null zurueck, wenn der User den ganzen Kurs gekauft hat.
	 */
	public Set<Long> getAccessibleChapterIds(Long userId, Long courseId) {
		Set<Long> result = new HashSet<>();
		
		// Hat der User den ganzen Kurs gekauft?
		if (bookingRepository.existsByStudentIdAndCourseId(userId, courseId)) {
			return null; // null bedeutet: alle zugänglich
		}
		
		// Einzeln gekaufte Kapitel
		result.addAll(bookingRepository.findPurchasedChapterIdsByUserAndCourse(userId, courseId));
		
		return result;
	}
	
	/**
	 * Prueft, ob der User den ganzen Kurs gekauft hat.
	 */
	public boolean hasUserPurchasedCourse(Long userId, Long courseId) {
		return bookingRepository.existsByStudentIdAndCourseId(userId, courseId);
	}
}