package tutothr.course;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import tutothr.category.Category;
import tutothr.chapter.Chapter;
import tutothr.common.BaseEntity;
import tutothr.hashtag.Hashtag;
import tutothr.rating.Rating;

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

    @OneToMany(mappedBy = "course", cascade = CascadeType.PERSIST)
    private List<Rating> ratings = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chapter> chapters = new ArrayList<>();
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
    	name = "courses_hashtags",
    	joinColumns = @JoinColumn(name = "course_id"),
    	inverseJoinColumns = @JoinColumn(name = "hashtag_id")
    )
    private List<Hashtag> hashtags = new ArrayList<>();
    
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

    public List<Hashtag> getHashtags() {
    	return hashtags;
    }
    
    public void setHashtags(List<Hashtag> hashtags) {
    	this.hashtags = hashtags;
    }
    
    public double avgRating() {
        return ratings.stream()
                .mapToInt(Rating::getStars)
                .average()
                .orElse(0.0);
    }
}