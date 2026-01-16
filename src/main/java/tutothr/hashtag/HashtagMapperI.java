package tutothr.hashtag;

import java.util.List;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HashtagMapperI {
	HashtagDTO toDTO(Hashtag hashtag);
	Hashtag toEntity(HashtagDTO dto);
	List<HashtagDTO> toDTOs(List<Hashtag> hashtags);
}
