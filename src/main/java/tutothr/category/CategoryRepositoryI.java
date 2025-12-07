package tutothr.category;

import org.springframework.data.jpa.repository.JpaRepository;

import tutothr.common.MyBaseRepository;

public interface CategoryRepositoryI extends MyBaseRepository<Category, Long>, JpaRepository<Category, Long>   {}
