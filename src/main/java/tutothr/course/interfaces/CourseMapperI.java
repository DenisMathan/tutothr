package tutothr.course.interfaces;

import java.util.List;

import org.mapstruct.Mapper;
import tutothr.course.Course;
import tutothr.course.CourseDTO;

// @Mapper(componentModel = "spring", uses = { CourseMapper.class })
@Mapper(componentModel = "spring")
public interface CourseMapperI {
    CourseDTO toDTO(Course course);

    Course toEntity(CourseDTO dto);

    List<CourseDTO> toCourseDTOs(List<Course> courses);
    List<Course> toEntities(List<CourseDTO> dtos);
}