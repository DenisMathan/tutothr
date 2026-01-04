package tutothr.rating.interfaces;

import org.mapstruct.Mapper;
import tutothr.rating.Rating;
import tutothr.rating.RatingDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RatingMapperI {

        RatingDTO toDTO(Rating rating);

        Rating toEntity(RatingDTO dto);

        List<RatingDTO> toChapterDTOs(List<Rating> chapters);

        List<Rating> toEntities(List<RatingDTO> chapterDTOs);

}
