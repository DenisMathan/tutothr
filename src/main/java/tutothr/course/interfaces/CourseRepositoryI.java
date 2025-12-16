package tutothr.course.interfaces;

import java.util.List;


import tutothr.category.Category;
import tutothr.common.MyBaseRepository;
import tutothr.course.Course;

public interface CourseRepositoryI extends MyBaseRepository<Course, Long> {
    List<Course> findByCategoriesContaining(Category category);
}
