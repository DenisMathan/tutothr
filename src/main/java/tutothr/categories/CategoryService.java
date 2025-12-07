package tutothr.categories;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private CategoryRepositoryI categoryRepository;
    
    public CategoryService (CategoryRepositoryI categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
