package tutothr.rating.interfaces;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tutothr.course.Course;
import tutothr.rating.Rating;
import tutothr.rating.RatingDTO;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RatingMapperI {

    @Mapping(source = "course.id", target = "courseId")
    RatingDTO toDTO(Rating rating);

    @Mapping(source = "courseId", target = "course")
    Rating toEntity(RatingDTO dto);

    List<RatingDTO> toRatingDTOs(List<Rating> ratings);

    List<Rating> toEntities(List<RatingDTO> ratingDTOs);

    default Course map(Long id) {
        if (id == null) {
            return null;
        }
        Course c = new Course();
        c.setId(id);
        return c;
    }
}
