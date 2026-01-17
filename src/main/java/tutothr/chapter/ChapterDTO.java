package tutothr.chapter;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import tutothr.common.BaseDTO;

import tutothr.common.models.Field;

public class ChapterDTO extends BaseDTO {
    @NotBlank(message = "Title cannot be empty.")
    @Size(min = 3, max = 20, message = "Title must be between 3 and 20 characters.")
    private String title;
    private String description;
    private int position;
    private boolean paywalled;
    private Long courseId;

    @Override
    public void initFields() {
       formFields = List.of(
            new Field("courseId", "Kurs ID", "hidden"),
            new Field("title", "Titel", "text"),
            new Field("description", "Beschreibung", "textarea"),
            new Field("paywalled", "Paywalled", "checkbox")
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
    public int getPosition() {
        return position;
    }
    public void setPosition(int position) {
        this.position = position;
    }
    public boolean isPaywalled() {
        return paywalled;
    }
    public void setPaywalled(boolean paywalled) {
        this.paywalled = paywalled;
    }
    public Long getCourseId() {
        return courseId;
    }
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}
