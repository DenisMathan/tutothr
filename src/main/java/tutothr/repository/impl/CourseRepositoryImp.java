package tutothr.repository.impl;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import tutothr.model.Course;
import tutothr.repository.CourseRepositoryI;


public interface CourseRepositoryImp extends  CourseRepositoryI, CrudRepository<Course, Long>{
	
	List<Course> findByDescriptionContainingIgnoreCase (String description);
	
}
