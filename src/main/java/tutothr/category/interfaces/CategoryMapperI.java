package tutothr.category.interfaces;

import java.util.List;

import org.mapstruct.Mapper;

import tutothr.category.Category;
import tutothr.category.CategoryDTO;

// @Mapper(componentModel = "spring", uses = { CourseMapper.class })
@Mapper(componentModel = "spring")
public interface CategoryMapperI {
    CategoryDTO toDTO(Category category);

    Category toEntity(CategoryDTO dto);

    List<CategoryDTO> toCategoryDTOs(List<Category> categories);

    List<Category> toCategories(List<CategoryDTO> dtos);
}
