package tutothr.course;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import tutothr.booking.timeslot.TimeSlot;
import tutothr.category.Category;
import tutothr.hashtag.Hashtag;
import tutothr.user.User;

/**
 * Utility-Klasse, die Filter-Bausteine fuer die Kurs-Suche bereitstellt.
 * 
 * Jede Methode liefert eine Specification, die eine einzelne WHERE-Bedingung
 * darstellt. Diese koennen im CourseService mit .and() und .or() kombiniert
 * werden.
 * 
 * Beispiel: Specification<Course> spec = textContains("Mathe").and(minPrice(10.0)).and(hasCategory(5));
 */

/**
 * Utility-Klasse, die Filter-Bausteine fuer die Kurs-Suche bereitstellt.
 *
 * Jede Methode liefert eine Specification<Course>, die folgende Parameter erhaelt:
 * - root: Zugriff auf Course-Spalten (z.B. root.get("price"))
 * - query: Die Gesamt-Query (fuer Subqueries, DISTINCT)
 * - cb (CriteriaBuilder): Werkzeugkasten fuer Bedingungen (cb.equal, cb.like, cb.and, ...)
 *
 * Beispiel: Specification<Course> spec = textContains("Mathe")
 *               .and(minPrice(10.0f))
 *               .and(hasAnyCategory(List.of(5L)));
 */
public class CourseSpecifications {
	/**
	 * Sucht im Titel UND in der Beschreibung nach dem Text.
	 */
	public static Specification<Course> textContains(String text) {
		return (root, query, cb) -> {
			String pattern = "%" + text.toLowerCase() + "%";
			return cb.or(cb.like(cb.lower(root.get("title")), pattern),
					cb.like(cb.lower(root.get("description")), pattern));
		};
	}

	/**
	 * Filtert nach Kategorie.
	 */
	public static Specification<Course> hasAnyCategory(List<Long> categoryIds) {
		return (root, query, cb) -> {
			Join<Course, Category> categories = root.join("categories", JoinType.INNER);
			return categories.get("id").in(categoryIds);
		};
	}

	/**
	 * Filtert nach Mindestpreis.
	 */
	public static Specification<Course> minPrice(Float price) {
		return (root, query, cb) -> {
			return cb.greaterThanOrEqualTo(root.get("price"), price);
		};
	}

	/**
	 * Filtert nach Maximalpreis.
	 */
	public static Specification<Course> maxPrice(Float price) {
		return (root, query, cb) -> {
			return cb.lessThanOrEqualTo(root.get("price"), price);
		};
	}

	/**
	 * Filtert nach Mindestbewertung.
	 */
	public static Specification<Course> minRating(Float rating) {
		return (root, query, cb) -> {
			return cb.greaterThanOrEqualTo(root.get("rating"), rating);
		};
	}

	/**
	 * Filtert Kurse, deren Ersteller den angegebenen Namen im Username enthaelt.
	 */
	public static Specification<Course> tutorNameContains(String tutorName) {
		return (root, query, cb) -> {
			Subquery<Long> userSubquery = query.subquery(Long.class);
			Root<User> userRoot = userSubquery.from(User.class);
			userSubquery.select(userRoot.get("id"))
					.where(cb.like(cb.lower(userRoot.get("username")), "%" + tutorName.toLowerCase() + "%"));

			return cb.in(root.get("ownerId")).value(userSubquery);
		};
	}

	/**
	 * Filtert nach Verfuegbarkeit (Tutor hat mindestens einen freien, zukuenftigen
	 * TimeSlot).
	 */
	public static Specification<Course> hasAvailableTimeSlots() {
		return (root, query, cb) -> {
			Subquery<Long> timeSlotSubquery = query.subquery(Long.class);
			Root<TimeSlot> timeSlotRoot = timeSlotSubquery.from(TimeSlot.class);
			timeSlotSubquery.select(timeSlotRoot.get("tutor").get("id"))
					.where(cb.and(cb.equal(timeSlotRoot.get("available"), true),
							cb.greaterThanOrEqualTo(timeSlotRoot.get("date"), LocalDate.now())));

			return cb.in(root.get("ownerId")).value(timeSlotSubquery);
		};
	}
	
	/**
	 * Filtert nach Hashtag-Name.
	 */
	public static Specification<Course> hasHashtag(String hashtag) {
		return (root, query, cb) -> {
			Join<Course, Hashtag> hashtags = root.join("hashtags", JoinType.INNER);
			return cb.equal(cb.lower(hashtags.get("name")), hashtag.toLowerCase());
		};
	}
}
