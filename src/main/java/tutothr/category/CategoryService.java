package tutothr.category;

import java.util.List;

import org.springframework.stereotype.Service;

import tutothr.category.interfaces.CategoryMapperI;
import tutothr.category.interfaces.CategoryRepositoryI;
import tutothr.common.BaseService;
import tutothr.course.Course;
import tutothr.course.interfaces.CourseRepositoryI;

@Service
public class CategoryService extends BaseService<CategoryDTO, Category> {

    private CourseRepositoryI courseRepository;
    protected CategoryMapperI mapper;

    public CategoryService(CategoryRepositoryI categoryRepository, CourseRepositoryI courseRepository, CategoryMapperI mapper) {
        super(categoryRepository);
        this.courseRepository = courseRepository;
        this.mapper = mapper;
    }

    @Override
    public void deleteById(Long id) {
        repository.findById(id).ifPresent(category -> {
            // Remove category from all courses that reference it
            List<Course> courses = category.getCourses();
            if (courses != null) {
                for (Course course : courses) {
                    course.getCategories().remove(category);
                    courseRepository.save(course);
                }
            }
            // Now delete the category
            repository.delete(category);
        });
    }

    @Override
    public CategoryDTO mapToDTO(Category entity) {
        return mapper.toDTO(entity);
    }

    @Override
    public Category mapToEntity(CategoryDTO dto) {
        return mapper.toEntity(dto);
    }

    public Category findByTitle(String title) {
        return ((CategoryRepositoryI) repository).findByTitle(title).orElse(null);
    }
}
