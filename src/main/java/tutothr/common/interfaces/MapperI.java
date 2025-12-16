package tutothr.common.interfaces;

import java.util.List;

import org.mapstruct.Mapper;

import tutothr.category.Category;
import tutothr.category.CategoryDTO;
import tutothr.course.Course;

@Mapper(componentModel = "spring")
public interface MapperI {

    CategoryDTO toDTO(Category category);
    Category toEntity(CategoryDTO dto);
    List<CategoryDTO> toCategoryDTOs(List<Category> categories);
    List<Category> toCategories(List<CategoryDTO> dtos);

    // CourseDTO toDTO(Course course);
    // Course toEntity(CourseDTO dto);

    // List<CourseDTO> toCourseDTOs(List<Course> courses);
    // List<Course> toCourses(List<CourseDTO> dtos);
    
} 
