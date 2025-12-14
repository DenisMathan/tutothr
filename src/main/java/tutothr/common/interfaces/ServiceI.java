package tutothr.common.interfaces;
import tutothr.common.models.Field;

import java.util.List;

public interface ServiceI {
    void init();
    List<Field> getFields();
}
