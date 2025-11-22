package tutothr.service.impl;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import tutothr.model.Student;
import tutothr.repository.CourseRepositoryI;
import tutothr.repository.StudentRepositoryI;
import tutothr.service.StudentServiceI;

@Service
public class StudentServiceImpl implements StudentServiceI {
    private StudentRepositoryI studentRepository;
    private CourseRepositoryI courseRepository;

    public StudentServiceImpl(StudentRepositoryI studentRepository, CourseRepositoryI courseRepository) {
        super();
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public Page<Student> getAllStudents(String name, Pageable pageable) {
        // TODO Auto-generated method stub
        Page<Student> pageStudents;
        if (name == null) {
            pageStudents = studentRepository.findAll(pageable);
        } else {
            pageStudents = studentRepository.findByNameContainingIgnoreCase(name, pageable);

        }
        return pageStudents;
    }

    @Override
    public Student saveStudent(Student student) {
        // TODO Auto-generated method stub
        return studentRepository.save(student);
    }

    @Override
    public Student getStudentById(Long id) {
        // TODO Auto-generated method stub
        return studentRepository.findById(id).get();
    }

    @Override
    public Student updateStudent(Student student) {
        // TODO Auto-generated method stub
        System.out.println(student.getGender() + "***");
        return studentRepository.save(student);
    }

    @Override
    public void delete(Student student) {
        // TODO Auto-generated method stub
        studentRepository.delete(student);
    }

    @Override
    public List<Student> findStudentsByName(String name) {
        // TODO Auto-generated method stub
        return studentRepository.findByNameContainingIgnoreCase(name);
    }
}
