package tutothr.repository;

import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import tutothr.model.Student;

public interface StudentRepositoryI extends MyBaseRepository<Student, Long> {
    List<Student> findByNameContainingIgnoreCase(String name);

    Page<Student> findAll(Pageable pageable);

    Page<Student> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
