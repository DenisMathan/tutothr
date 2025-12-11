package tutothr.category;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tutothr.common.MyBaseRepository;

public interface CategoryRepositoryI extends MyBaseRepository<Category, Long>, JpaRepository<Category, Long>   {
    
    Optional<Category> findByTitle(String title);
}
