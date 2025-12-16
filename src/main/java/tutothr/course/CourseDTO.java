package tutothr.course;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import tutothr.category.CategoryDTO;
import tutothr.common.BaseDTO;

public class CourseDTO extends BaseDTO {
    @NotBlank(message = "Title cannot be empty.")
    @Size(min = 3, max = 20, message = "Title must be between 3 and 20 characters.")
    String title;
    String description;

    float price;
    float rating;
    private List<CategoryDTO> categories;

    private boolean isOwner = false;

    @Override
    public void initFields() {
        formFields = List.of(
            new tutothr.common.models.Field("title", "Titel", "text"),
            new tutothr.common.models.Field("description", "Beschreibung", "textarea"),
            new tutothr.common.models.Field("price", "Preis", "number")
        );
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public List<CategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDTO> categories) {
        this.categories = categories;
    }

    public boolean getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(boolean isOwner) {
        this.isOwner = isOwner;
    }


}
