package tutothr.course;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import tutothr.category.Category;
import tutothr.rating.Rating;

@Entity
public class Course {
    @Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    String title;
    String description;
    float price;
    float rating;

    @ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name="course_categories",
			joinColumns = @JoinColumn(name="id_course"),
			inverseJoinColumns = @JoinColumn(name="id_category")
			)
    private List<Category> categories;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings = new ArrayList<>();

    public Long getId() {
        return id;
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
    public void setRating(float rating) {this.rating = rating;};
    public float getRating() {return rating;};
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