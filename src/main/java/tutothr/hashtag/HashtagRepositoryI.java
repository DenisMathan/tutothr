package tutothr.hashtag;

import java.util.Optional;

import tutothr.common.MyBaseRepository;

public interface HashtagRepositoryI extends MyBaseRepository<Hashtag, Long> {
	Optional<Hashtag> findByName(String name);
}
