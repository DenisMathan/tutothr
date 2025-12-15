package tutothr.common;

import java.util.List;

import tutothr.common.interfaces.ServiceI;
import tutothr.common.models.Field;

public abstract class BaseService implements ServiceI {
    protected List<Field> fields;
    public BaseService() {
        init();
    }

    public Object update(Object obj) {
        Object existingObj = findById((Long) ((BaseEntity) obj).getId());
        for (Field field : fields) {
            try {
                java.lang.reflect.Field _field = obj.getClass().getDeclaredField(field.getName());
                _field.setAccessible(true);
                Object newValue = _field.get(obj);
                _field.set(existingObj, newValue);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        save(existingObj);
        return existingObj;
    }

    @Override
    public List<Field> getFields() {
        return fields;
    } 
}
