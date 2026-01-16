package tutothr.common;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;


@NoRepositoryBean
public interface MyBaseRepository <T, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
	
	// <S extends T> S save(S entity);
    
	// Optional<T> findById(ID id);

	// List<T> findAll();
	   
	// void delete(T entity);

	// void deleteById(ID id);
	
}
