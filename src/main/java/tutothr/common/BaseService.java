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
        for (Field field : obj.getFormFields()) {
            try {
                java.lang.reflect.Field dtoField = obj.getClass().getDeclaredField(field.getName());
                dtoField.setAccessible(true);
                Object newValue = dtoField.get(obj);

                java.lang.reflect.Field entityField = existingObj.getClass().getDeclaredField(field.getName());
                entityField.setAccessible(true);
                entityField.set(existingObj, newValue);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        save(existingObj);
        return mapToDTO(existingObj);
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
