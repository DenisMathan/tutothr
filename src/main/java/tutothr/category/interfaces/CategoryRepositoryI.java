package tutothr.category.interfaces;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tutothr.category.Category;
import tutothr.common.MyBaseRepository;

public interface CategoryRepositoryI extends MyBaseRepository<Category, Long>, JpaRepository<Category, Long>   {
    
    Optional<Category> findByTitle(String title);
}
