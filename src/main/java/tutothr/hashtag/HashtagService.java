package tutothr.hashtag;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tutothr.course.Course;
import tutothr.course.interfaces.CourseRepositoryI;
import tutothr.user.User;

@Service
public class HashtagService {
	private static final Logger logger = LoggerFactory.getLogger(HashtagService.class);
	
	private final HashtagRepositoryI hashtagRepository;
	private final CourseRepositoryI courseRepository;
	private final CourseHashtagLinkRepositoryI linkRepository;

	public HashtagService(HashtagRepositoryI hashtagRepository, CourseRepositoryI courseRepository,
			CourseHashtagLinkRepositoryI linkRepository) {
		this.hashtagRepository = hashtagRepository;
		this.courseRepository = courseRepository;
		this.linkRepository = linkRepository;
	}

	public List<Hashtag> findAll() {
		return hashtagRepository.findAll();
	}

	public Hashtag findById(Long id) {
		return hashtagRepository.findById(id).orElse(null);
	}

	public Hashtag findByName(String name) {
		return hashtagRepository.findByName(name).orElse(null);
	}

	public List<Hashtag> findAllEntitiesByIds(List<Long> ids) {
		if (ids == null || ids.isEmpty()) return List.of();
		return hashtagRepository.findAllById(ids);
	}

	public Hashtag save(Hashtag hashtag) {
		return hashtagRepository.save(hashtag);
	}

	/**
	 * Fuegt Hashtags zu einem Kurs hinzu. Erstellt neue Hashtags falls sie nicht
	 * existieren. Speichert wer das Tag hinzugefuegt hat.
	 */
	@Transactional
	public void addHashtagsToCourse(Long courseId, String hashtagsInput, User currentUser) {
		Course course = courseRepository.findById(courseId)
				.orElseThrow(() -> new RuntimeException("Kurs nicht gefunden"));

		String[] names = hashtagsInput.split(",");

		for (String name : names) {
			String normalizedName = normalizeName(name);
			if (normalizedName.isEmpty()) {
				continue;
			}

			// Hashtag suchen oder neu erstellen
			Hashtag hashtag = hashtagRepository.findByName(normalizedName).orElse(null);

			if (hashtag == null) {
				hashtag = new Hashtag();
				hashtag.setName(normalizedName);
				hashtag = hashtagRepository.save(hashtag);
			}

			// Nur hinzufuegen wenn nicht schon vorhanden
			if (!linkRepository.existsByCourseAndHashtag(course, hashtag)) {
				CourseHashtagLink link = new CourseHashtagLink(course, hashtag, currentUser);
				linkRepository.save(link);
			}
		}
	}

	/**
	 * Entfernt ein Hashtag von einem Kurs. 
	 * Berechtigt sind: Admin, Kurs-Owner, oder der User der das Tag hinzugefuegt hat.
	 */
	@Transactional
	public void removeHashtagFromCourse(Long courseId, Long hashtagId, Long currentUserId, boolean isAdmin) {
		logger.info("removeHashtagFromCourse aufgerufen: courseId={}, hashtagId={}, userId={}, isAdmin={}", 
				courseId, hashtagId, currentUserId, isAdmin);
		
		Course course = courseRepository.findById(courseId)
				.orElseThrow(() -> new RuntimeException("Kurs nicht gefunden"));
		logger.info("Kurs gefunden: {}", course.getTitle());

		// FIX: Verwende ID-basierte Suche statt Entity-basierte Suche
		CourseHashtagLink link = linkRepository.findByCourseIdAndHashtagId(courseId, hashtagId)
				.orElseThrow(() -> {
					logger.error("Link nicht gefunden für courseId={}, hashtagId={}", courseId, hashtagId);
					return new RuntimeException("Hashtag nicht mit diesem Kurs verknuepft");
				});
		logger.info("Link gefunden: id={}, addedBy={}", link.getId(), 
				link.getAddedBy() != null ? link.getAddedBy().getId() : "null");
		
		if (!canUserRemoveHashtag(link, course, currentUserId, isAdmin)) {
			logger.warn("Keine Berechtigung: userId={}, courseOwnerId={}, linkAddedBy={}", 
					currentUserId, course.getOwnerId(), 
					link.getAddedBy() != null ? link.getAddedBy().getId() : "null");
			throw new RuntimeException("Keine Berechtigung");
		}
		
		logger.info("Berechtigung OK, lösche Link...");
		
		// FIX: Link aus der Course-Liste entfernen (wegen CascadeType.ALL + orphanRemoval)
		// Direktes linkRepository.delete() funktioniert nicht, da Course noch eine Referenz hält
		// Verwende removeIf mit ID-Vergleich, da equals() möglicherweise nicht korrekt ist
		final Long linkId = link.getId();
		boolean removed = course.getHashtagLinks().removeIf(l -> l.getId().equals(linkId));
		logger.info("Link aus Liste entfernt: {}", removed);
		courseRepository.save(course);
		
		logger.info("Link gelöscht");
	}

	/**
	 * Prueft ob ein User ein Hashtag von einem Kurs entfernen darf.
	 */
	private boolean canUserRemoveHashtag(CourseHashtagLink link, Course course, Long userId, boolean isAdmin) {
		if (isAdmin) return true;
		// FIX: Null-sichere Vergleiche - userId.equals() statt getOwnerId().equals() verhindert NPE
		if (userId != null && userId.equals(course.getOwnerId())) return true;
		if (link.getAddedBy() != null && userId != null && userId.equals(link.getAddedBy().getId())) return true;
		return false;
	}

	/**
	 * Gibt die IDs aller Hashtags zurueck, die ein User von einem Kurs entfernen darf.
	 */
	public Set<Long> getRemovableHashtagIds(Long courseId, Long userId, boolean isAdmin) {
		Set<Long> removableIds = new HashSet<>();
		Course course = courseRepository.findById(courseId).orElse(null);
		if (course == null) return removableIds;

		List<CourseHashtagLink> links = linkRepository.findByCourseId(courseId);
		for (CourseHashtagLink link : links) {
			if (canUserRemoveHashtag(link, course, userId, isAdmin)) {
				removableIds.add(link.getHashtag().getId());
			}
		}
		return removableIds;
	}

	/**
	 * Setzt addedBy auf null fuer alle Links eines Users (wenn User geloescht wird).
	 * Ignoriert Links zu Kursen die dem User selbst gehoeren (diese werden durch Cascade geloescht).
	 */
	@Transactional
	public void releaseLinksFromUser(User user) {
		List<CourseHashtagLink> links = linkRepository.findByAddedBy(user);
		for (CourseHashtagLink link : links) {
			// Nur Links bearbeiten deren Kurs nicht dem User gehoert
			// (Links zu eigenen Kursen werden durch CascadeType.ALL automatisch geloescht)
			if (link.getCourse() != null && !link.getCourse().getOwner().getId().equals(user.getId())) {
				link.setAddedBy(null);
				linkRepository.save(link);
			}
		}
	}

	/**
	 * Normalisiert einen Hashtag-Namen: trimmen, lowercase, # entfernen.
	 */
	private String normalizeName(String name) {
		String trimmed = name.trim().toLowerCase();
		if (trimmed.startsWith("#")) {
			trimmed = trimmed.substring(1);
		}
		return trimmed;
	}
}
