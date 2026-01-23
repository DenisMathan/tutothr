package tutothr.category;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import tutothr.common.BaseDTO;

public class CategoryDTO extends BaseDTO {
    @NotBlank(message = "Title cannot be empty.")
    @Size(min = 3, max = 20, message = "Title must be between 3 and 20 characters.")
    String title;
    String description;

    @Override
    public void initFields() {
        // Initialize any Category-specific fields or validation here
        formFields = List.of(
                new tutothr.common.models.Field("title", "field.title", "text"),
                new tutothr.common.models.Field("description", "field.description", "textarea"));
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
