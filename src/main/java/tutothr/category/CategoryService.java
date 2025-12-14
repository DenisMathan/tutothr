package tutothr.category;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import tutothr.common.BaseService;
import tutothr.common.models.Field;
import tutothr.course.Course;
import tutothr.course.CourseRepositoryI;

@Service
public class CategoryService extends BaseService {

    private CategoryRepositoryI categoryRepository;
    private CourseRepositoryI courseRepository;
    
    public CategoryService (CategoryRepositoryI categoryRepository, CourseRepositoryI courseRepository) {
        super();
        this.categoryRepository = categoryRepository;
        this.courseRepository = courseRepository;

    }

    @Override
    public void init() {
        fields = List.of(
            new Field("title", "Titel", "text"),
            new Field("description", "Beschreibung", "textarea")
        );
        
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }
    public Optional<Category> getCategoryByTitle(String title) {
        return categoryRepository.findByTitle(title);
    }
    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }
    public void deleteCategoryById(Long id) {
        categoryRepository.findById(id).ifPresent(category -> {
            // Remove category from all courses that reference it
            List<Course> courses = category.getCourses();
            if (courses != null) {
                for (Course course : courses) {
                    course.getCategories().remove(category);
                    courseRepository.save(course);
                }
            }
            // Now delete the category
            categoryRepository.delete(category);
        });
    }
}
