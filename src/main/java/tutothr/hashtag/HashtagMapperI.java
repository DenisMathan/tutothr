package tutothr.hashtag;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HashtagMapperI {
	HashtagDTO toDTO(Hashtag hashtag);
	Hashtag toEntity(HashtagDTO dto);
	List<HashtagDTO> toDTOs(List<Hashtag> hashtags);
}
