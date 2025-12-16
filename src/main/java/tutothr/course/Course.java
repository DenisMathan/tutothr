package tutothr.course;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import tutothr.category.Category;
import tutothr.chapter.Chapter;
import tutothr.rating.Rating;
import tutothr.common.BaseEntity;

@Entity
public class Course extends BaseEntity {
    String title;
    String description;
    float price;
    float rating;

    @Column(nullable = false)
    Long ownerId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "course_categories", joinColumns = @JoinColumn(name = "id_course"), inverseJoinColumns = @JoinColumn(name = "id_category"))
    private List<Category> categories;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings = new ArrayList<>();


    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chapter> chapters = new ArrayList<>();

    public List<Chapter> getChapters() {
        return chapters;
    }
    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
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

    public void setRating(float rating) {
        this.rating = rating;
    };

    public float getRating() {
        return rating;
    };

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public double avgRating() {
        return ratings.stream()
                .mapToInt(Rating::getStars)
                .average()
                .orElse(0.0);
    }
}