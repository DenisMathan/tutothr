package tutothr.course;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import tutothr.category.Category;
import tutothr.chapter.Chapter;
import tutothr.common.BaseEntity;
import tutothr.hashtag.CourseHashtagLink;
import tutothr.hashtag.Hashtag;
import tutothr.rating.Rating;
import tutothr.user.User;

@Entity
public class Course extends BaseEntity {
    String title;
    String description;
    float price;
    float rating;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "course_categories", joinColumns = @JoinColumn(name = "id_course"), inverseJoinColumns = @JoinColumn(name = "id_category"))
    private List<Category> categories;

    @OneToMany(mappedBy = "course", cascade = CascadeType.PERSIST)
    private List<Rating> ratings = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chapter> chapters = new ArrayList<>();
    
    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseHashtagLink> hashtagLinks = new ArrayList<>();
    
    public List<Chapter> getChapters() {
        return chapters;
    }
    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    // Convenience-Methode fuer Rueckwaertskompatibilitaet
    public Long getOwnerId() {
        return owner != null ? owner.getId() : null;
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

    public List<CourseHashtagLink> getHashtagLinks() {
    	return hashtagLinks;
    }
    
    public void setHashtagLinks(List<CourseHashtagLink> hashtagLinks) {
    	this.hashtagLinks = hashtagLinks;
    }

    /**
     * Convenience-Methode: Gibt alle Hashtags dieses Kurses zurueck.
     */
    public List<Hashtag> getHashtags() {
    	return hashtagLinks.stream()
    			.map(CourseHashtagLink::getHashtag)
    			.collect(Collectors.toList());
    }
    
    public double avgRating() {
        return ratings.stream()
                .mapToInt(Rating::getStars)
                .average()
                .orElse(0.0);
    }
}