package tutothr.booking.timeslot;

import java.util.List;

import tutothr.common.MyBaseRepository;
import tutothr.user.User;

public interface TimeSlotRepositoryI extends MyBaseRepository<TimeSlot, Long> {
	List<TimeSlot> findByTutor(User tutor);
	
	List<TimeSlot> findByTutorAndAvailableTrue(User tutor);
	
	void deleteByTutor(User tutor);
}
