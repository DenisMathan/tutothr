package tutothr.category.interfaces;

import java.util.Optional;



import tutothr.category.Category;
import tutothr.common.MyBaseRepository;

public interface CategoryRepositoryI extends MyBaseRepository<Category, Long>  {
    
    Optional<Category> findByTitle(String title);
}
