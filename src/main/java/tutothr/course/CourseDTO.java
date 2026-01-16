package tutothr.course;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import tutothr.category.CategoryDTO;
import tutothr.chapter.ChapterDTO;
import tutothr.common.BaseDTO;
import tutothr.hashtag.HashtagDTO;
import tutothr.rating.Rating;

public class CourseDTO extends BaseDTO {
    @NotBlank(message = "Title cannot be empty.")
    @Size(min = 3, max = 20, message = "Title must be between 3 and 20 characters.")
    String title;
    String description;

    float price;
    private Long ownerId;
    private List<CategoryDTO> categories;
    
    private List<ChapterDTO> chapters;

    private boolean isOwner = false;

    private ChapterDTO addChapter;

    float rating;
    private double avgRating;
    private List<Rating> ratings = new ArrayList<>();

    private List<HashtagDTO> hashtags = new ArrayList<>();
    
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
    public Long getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
    public double getAvgRating() {
        return avgRating;
    }
    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }

    public List<Rating> getRatings() {
        return ratings;
    }
    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
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

    public List<ChapterDTO> getChapters() {
        return chapters;
    }
    public void setChapters(List<ChapterDTO> chapters) {
        this.chapters = chapters;
    }

    public void setIsOwner(boolean isOwner) {
        this.isOwner = isOwner;
    }

    public ChapterDTO getAddChapter() {
        return addChapter;
    }
    public void setAddChapter(ChapterDTO addChapter) {
        this.addChapter = addChapter;
    }
    
    public List<HashtagDTO> getHashtags() {
    	return hashtags;
    }
    
    public void setHashtags(List<HashtagDTO> hashtags) {
    	this.hashtags = hashtags;
    }
}
