package tutothr.common.interfaces;
import tutothr.common.BaseDTO;
import tutothr.common.BaseEntity;

import java.util.List;

import org.springframework.validation.FieldError;

public interface ServiceI<DTO extends BaseDTO, Entity extends BaseEntity> {
    Entity findById(Long id);
    void deleteById(Long id);
    DTO update(DTO obj);
    void save(Entity obj);
    void saveDTO(DTO obj);
    DTO findDTOById(Long id);
    List<DTO> getAllDTOs();
    DTO mapToDTO(Entity entity);
    Entity mapToEntity(DTO dto);
    DTO handleValidationErrors(DTO dto, List<FieldError> fieldErrors);
}
