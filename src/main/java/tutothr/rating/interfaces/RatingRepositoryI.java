package tutothr.rating.interfaces;

import java.util.List;

import tutothr.common.MyBaseRepository;
import tutothr.rating.Rating;
import tutothr.user.User;

public interface RatingRepositoryI extends MyBaseRepository<Rating, Long> {
	List<Rating> findByAuthor(User author);
	void deleteByAuthor(User author);
}
