package tutothr.hashtag;

import java.util.Optional;

import tutothr.common.MyBaseRepository;

import tutothr.user.User;
import java.util.List;

public interface HashtagRepositoryI extends MyBaseRepository<Hashtag, Long> {
	Optional<Hashtag> findByName(String name);
	List<Hashtag> findByCreator(User creator);
}
