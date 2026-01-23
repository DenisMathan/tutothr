package tutothr.hashtag;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tutothr.course.Course;
import tutothr.course.interfaces.CourseRepositoryI;
import tutothr.user.User;

@Service
public class HashtagService {
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
		Course course = courseRepository.findById(courseId)
				.orElseThrow(() -> new RuntimeException("Kurs nicht gefunden"));

		Hashtag hashtag = hashtagRepository.findById(hashtagId)
				.orElseThrow(() -> new RuntimeException("Hashtag nicht gefunden"));
		
		CourseHashtagLink link = linkRepository.findByCourseAndHashtag(course, hashtag)
				.orElseThrow(() -> new RuntimeException("Hashtag nicht mit diesem Kurs verknuepft"));
		
		if (!canUserRemoveHashtag(link, course, currentUserId, isAdmin)) {
			throw new RuntimeException("Keine Berechtigung");
		}
		
		linkRepository.delete(link);
	}

	/**
	 * Prueft ob ein User ein Hashtag von einem Kurs entfernen darf.
	 */
	private boolean canUserRemoveHashtag(CourseHashtagLink link, Course course, Long userId, boolean isAdmin) {
		if (isAdmin) return true;
		if (course.getOwnerId().equals(userId)) return true;
		if (link.getAddedBy() != null && link.getAddedBy().getId().equals(userId)) return true;
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
	 */
	@Transactional
	public void releaseLinksFromUser(User user) {
		List<CourseHashtagLink> links = linkRepository.findByAddedBy(user);
		for (CourseHashtagLink link : links) {
			link.setAddedBy(null);
			linkRepository.save(link);
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
