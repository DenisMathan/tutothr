package tutothr.common;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.validation.FieldError;

import tutothr.common.interfaces.ServiceI;
import tutothr.common.models.Field;

public abstract class BaseService<DTO extends BaseDTO, Entity extends BaseEntity> implements ServiceI<DTO, Entity> {
    
    protected MyBaseRepository<Entity, Long> repository;
    protected List<Field> fields;

    public BaseService(MyBaseRepository<Entity, Long> repository) {
        this.repository = repository;
    }

    @Override
    public DTO update(DTO obj) {
        Entity existingObj = findById(obj.getId());
        updateFieldsRecursive(obj, existingObj, obj.getFormFields());
        save(existingObj);
        return mapToDTO(existingObj);
    }

    private void updateFieldsRecursive(DTO obj, Entity existingObj, List<Field> fields) {
        for (Field field : fields) {
            String type = field.getType();
            if ("group".equals(type) && field.getSubFields() != null) {
                 updateFieldsRecursive(obj, existingObj, field.getSubFields());
            } else if (!"group".equals(type) && !"hidden".equals(type)) { // Skip structural fields
                try {
                    transferField(obj, existingObj, field.getName());
                } catch (NoSuchFieldException e) {
                    // It is normal for DTOs to have fields that Entities do not have (e.g. view helpers, mapped fields)
                    // We just ignore them in this generic auto-mapper.
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void transferField(Object source, Object target, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        java.lang.reflect.Field sourceField = source.getClass().getDeclaredField(fieldName);
        sourceField.setAccessible(true);
        Object newValue = sourceField.get(source);

        java.lang.reflect.Field targetField = target.getClass().getDeclaredField(fieldName);
        targetField.setAccessible(true);
        targetField.set(target, newValue);
    }

    @Override
    public DTO findDTOById(Long id) {
        return mapToDTO(findById(id));
    }

    @Override
    public Entity findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void save(Entity obj) {
        repository.save(obj);
    }
   
    @Override
    public void saveDTO(DTO obj) {
        repository.save(mapToEntity(obj));
    }

    @Override
    public List<DTO> getAllDTOs() {
        return repository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DTO handleValidationErrors(DTO dto, List<FieldError> fieldErrors) {
        for (FieldError error : fieldErrors) {
            dto.addValidationError(error.getField(), error.getDefaultMessage());
        }
        return dto;
    }
}
