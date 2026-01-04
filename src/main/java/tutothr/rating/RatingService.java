package tutothr.rating;

import org.springframework.stereotype.Service;
import tutothr.common.BaseService;
import tutothr.course.CourseDTO;
import tutothr.course.CourseService;
import tutothr.rating.interfaces.RatingMapperI;
import tutothr.rating.interfaces.RatingRepositoryI;

@Service
public class RatingService extends BaseService<RatingDTO, Rating> {
    private RatingMapperI mapper;
    private CourseService courseService;

    public RatingService(RatingRepositoryI ratingRepository, RatingMapperI mapper, CourseService courseService) {
        super(ratingRepository);
        this.mapper = mapper;
        this.courseService = courseService;
    }

    @Override
    public RatingDTO mapToDTO(Rating entity) {
        return mapper.toDTO(entity);
    }

    @Override
    public Rating mapToEntity(RatingDTO dto) {
        return mapper.toEntity(dto);
    }

    @Override
    public void saveDTO(RatingDTO ratingDTO) {
        Rating rating = mapToEntity(ratingDTO);
        rating.setCourse(this.courseService.findById(ratingDTO.getCourseId()));
        repository.save(rating);
    }

    public CourseDTO findCourseDTOById(Long id) {
        return this.courseService.findDTOById(id);
    }
}
