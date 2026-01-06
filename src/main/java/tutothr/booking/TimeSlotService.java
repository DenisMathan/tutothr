package tutothr.booking;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import tutothr.booking.interfaces.TimeSlotMapperI;
import tutothr.booking.interfaces.TimeSlotRepositoryI;
import tutothr.user.User;

@Service
public class TimeSlotService {
	private final TimeSlotRepositoryI repository;
	private final TimeSlotMapperI mapper;

	public TimeSlotService(TimeSlotRepositoryI repository, TimeSlotMapperI mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}

	public TimeSlotDTO save(TimeSlotDTO dto) {
		TimeSlot entity = mapper.toEntity(dto);
		TimeSlot saved = repository.save(entity);
		return mapper.toDTO(saved);
	}

	public TimeSlotDTO findById(Long id) {
		TimeSlot entity = repository.findById(id).orElse(null);
		return entity != null ? mapper.toDTO(entity) : null;
	}

	public List<TimeSlotDTO> findByTutor(User tutor) {
		return repository.findByTutor(tutor)
				.stream()
				.map(mapper::toDTO)
				.collect(Collectors.toList());
	}
	
	public List<TimeSlotDTO> findAvailableByTutor(User tutor) {
		return repository.findByTutorAndAvailableTrue(tutor)
				.stream()
				.map(mapper::toDTO)
				.collect(Collectors.toList());
	}
	
	public void deleteById(Long id) {
		repository.deleteById(id);
	}
}
