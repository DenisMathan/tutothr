package tutothr.hashtag;

import java.util.List;

import org.springframework.stereotype.Service;

import tutothr.course.Course;
import tutothr.course.interfaces.CourseRepositoryI;
import tutothr.user.User;

@Service
public class HashtagService {
	private final HashtagRepositoryI hashtagRepository;
	private final CourseRepositoryI courseRepository;

	public HashtagService(HashtagRepositoryI hashtagRepository, CourseRepositoryI courseRepository) {
		this.hashtagRepository = hashtagRepository;
		this.courseRepository = courseRepository;
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
	 * existieren.
	 */
	public void addHashtagsToCourse(Long courseId, String hashtagsInput, User currentUser) {
		Course course = courseRepository.findById(courseId)
				.orElseThrow(() -> new RuntimeException("Kurs nicht gefunden"));

		String[] names = hashtagsInput.split(",");

		for (String name : names) {
			String trimmedName = name.trim();
			if (trimmedName.isEmpty()) {
				continue;
			}

			// # entfernen falls vorhanden
			if (trimmedName.startsWith("#")) {
				trimmedName.substring(1);
			}

			// Hashtag suchen oder neu erstellen
			Hashtag hashtag = hashtagRepository.findByName(trimmedName).orElse(null);

			if (hashtag == null) {
				hashtag = new Hashtag();
				hashtag.setName(trimmedName);
				hashtag.setCreator(currentUser);
				hashtag = hashtagRepository.save(hashtag);
			}

			// Nur hinzufuegen wenn nicht schon vorhanden
			if (!course.getHashtags().contains(hashtag)) {
				course.getHashtags().add(hashtag);
			}
		}
		courseRepository.save(course);
	}

	/**
	 * Entfernt ein Hashtag von einem Kurs. Nur Admin, Kurs-Owner oder
	 * Hashtag-Creator duerfen das.
	 */
	public void removeHashtagFromCourse(Long courseId, Long hashtagId, Long currentUserId, boolean isAdmin) {
		Course course = courseRepository.findById(courseId)
				.orElseThrow(() -> new RuntimeException("Kurs nicht gefunden"));

		Hashtag hashtag = hashtagRepository.findById(hashtagId)
				.orElseThrow(() -> new RuntimeException("Hashtag nicht gefunden"));
		
		boolean isCourseOwner = course.getOwnerId().equals(currentUserId);
		boolean isHashtagCreator = hashtag.getCreator().getId().equals(currentUserId);
		
		if (!isAdmin && !isCourseOwner && !isHashtagCreator) {
			throw new RuntimeException("Keine Berechtigung");
		}
		
		course.getHashtags().remove(hashtag);
		courseRepository.save(course);
	}

	public void releaseHashtagsFromCreator(User user) {
		List<Hashtag> hashtags = hashtagRepository.findByCreator(user);
		for (Hashtag hashtag : hashtags) {
			hashtag.setCreator(null);
			hashtagRepository.save(hashtag);
		}
	}
}
