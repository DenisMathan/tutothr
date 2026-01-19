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
    @Size(min = 3, max = 50, message = "Title must be between 3 and 50 characters.")
    String title;
    String description;

    float price;
    private Long ownerId;
    private List<CategoryDTO> categories;
    private List<Long> categoryIds = new ArrayList<>();
    
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
        if (categories != null) {
            this.categoryIds = categories.stream().map(CategoryDTO::getId).collect(java.util.stream.Collectors.toList());
        }
    }

    public List<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public void updateCategoryField(List<CategoryDTO> allCategories) {
        List<tutothr.common.models.SelectOption> options = new ArrayList<>();
        for (CategoryDTO cat : allCategories) {
            options.add(new tutothr.common.models.SelectOption(cat.getTitle(), cat.getId()));
        }
        // Ensure it's mutable
        this.formFields = new ArrayList<>(this.formFields);
        
        formFields.removeIf(f -> f.getName().equals("categoryIds"));
        
        formFields.add(tutothr.common.models.Field.withOptions("categoryIds", "Kategorien", "checkbox-group", options));
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
