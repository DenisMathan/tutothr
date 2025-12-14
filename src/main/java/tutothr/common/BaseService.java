package tutothr.common;

import java.util.List;

import tutothr.common.interfaces.ServiceI;
import tutothr.common.models.Field;

public abstract class BaseService implements ServiceI {
    protected List<Field> fields;
    public BaseService() {
        init();
    }

    @Override
    public List<Field> getFields() {
        return fields;
    } 
}
