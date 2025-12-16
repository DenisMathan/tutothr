package tutothr.course;

import java.util.List;

import org.springframework.stereotype.Service;

import tutothr.common.BaseService;
import tutothr.common.models.Field;

// @Service
// public class CourseService extends BaseService<BaseDTO, Course> {

//     private CoursePermissionService coursePermissionService;

//     public CourseService(CourseRepositoryI courseRepository, CoursePermissionService coursePermissionService) {
//         super(courseRepository);
//         this.coursePermissionService = coursePermissionService;
//     }

//     @Override
//     public void init() {
//         fields = List.of(
//                 new Field("title", "Titel", "text"),
//                 new Field("description", "Beschreibung", "textarea"),
//                 new Field("price", "Preis", "number"));
//     }

//     public List<Course> getAllCourses() {
//         List<Course> courses = repository.findAll();
//         courses = coursePermissionService.setOwner(courses);
//         return courses;
//     }

//     @Override
//     public Course findById(Long id) {
//         Course course = repository.findById(id).orElse(null);
//         if (course != null) {
//             course.setIsOwner(coursePermissionService.isCurrentUserOwner(course.getOwnerId()));
//         }
//         return repository.findById(id).orElse(null);
//     }

//     public void deleteById(Long id) {
//         repository.findById(id).ifPresent(course -> {
//             repository.delete(course);
//         });
//     }

//     @Override
//     public List<BaseDTO> getAllDTOs() {
//         throw new UnsupportedOperationException("Not implemented yet");
//     }

//     @Override
//     public BaseDTO mapToDTO(Course entity) {
//         throw new UnsupportedOperationException("Not implemented yet");
//     }

//     @Override
//     public Course mapToEntity(BaseDTO dto) {
//         throw new UnsupportedOperationException("Not implemented yet");
//     }
// }
