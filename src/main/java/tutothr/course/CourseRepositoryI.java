package tutothr.course;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tutothr.category.Category;
import tutothr.common.MyBaseRepository;

public interface CourseRepositoryI extends MyBaseRepository<Course, Long>, JpaRepository<Course, Long>   {
    List<Course> findByCategoriesContaining(Category category);
}
