package tutothr.repository.impl;

import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import tutothr.model.Student;
import tutothr.repository.StudentRepositoryI;

@Repository
public interface StudentRepositoryImp extends StudentRepositoryI, PagingAndSortingRepository<Student, Long> {
    List<Student> findByNameContainingIgnoreCase(String name);

    Page<Student> findAll(Pageable pageable);

    Page<Student> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
