package tutothr.course;

import org.springframework.data.jpa.repository.JpaRepository;

import tutothr.common.MyBaseRepository;

public interface CourseRepositoryI extends MyBaseRepository<Course, Long>, JpaRepository<Course, Long>   {}
