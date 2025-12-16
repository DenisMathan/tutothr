package tutothr.common;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;



public interface MyBaseRepository <T, ID extends Serializable> {
	
	<S extends T> S save(S entity);
    
	Optional<T> findById(ID id);

	List<T> findAll();
	   
	void delete(T entity);

	void deleteById(ID id);
	
}
