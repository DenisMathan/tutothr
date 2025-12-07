package tutothr.course;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import tutothr.category.Category;

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
    public float getRating() {
        return rating;
    }
    public void setRating(float rating) {
        this.rating = rating;
    }
    public List<Category> getCategories() {
        return categories;
    }
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}