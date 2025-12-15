package tutothr.common.interfaces;
import tutothr.common.models.Field;

import java.util.List;

public interface ServiceI {
    void init();
    List<Field> getFields();
    
    Object findById(Long id);
    void deleteById(Long id);
    Object update(Object obj);
    void save(Object obj);
}
