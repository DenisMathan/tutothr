package tutothr.course;

import java.util.List;

import tutothr.common.utils.enums.SortDirectionEnum;

/**
 * DTO fuer die Suchparameter der Kurs-Suche. Alle Felder sind optional
 * (nullable).
 */
public class CourseSearchDTO {
	// Filteroptionen
	private String text;
	private List<Long> categoryIds;
	private Float minPrice;
	private Float maxPrice;
	private Float minRating;
	private String tutorName;
	private Boolean onlyAvailable;
	private String hashtag;

	// Sortierung
	private CourseSortFieldEnum sortBy; // createdAt, price, rating, title
	private SortDirectionEnum sortDirection; // ASC, DESC

	// Paging
	private Integer page; // Aktuelle Seite (0-basiert)
	private Integer size; // Eintraege pro Seite

	// Getter und Setter
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<Long> getCategoryIds() {
		return categoryIds;
	}

	public void setCategoryIds(List<Long> categoryIds) {
		this.categoryIds = categoryIds;
	}

	public Float getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(Float minPrice) {
		this.minPrice = minPrice;
	}

	public Float getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(Float maxPrice) {
		this.maxPrice = maxPrice;
	}

	public Float getMinRating() {
		return minRating;
	}

	public void setMinRating(Float minRating) {
		this.minRating = minRating;
	}

	public String getTutorName() {
		return tutorName;
	}

	public void setTutorName(String tutorName) {
		this.tutorName = tutorName;
	}

	public Boolean getOnlyAvailable() {
		return onlyAvailable;
	}

	public void setOnlyAvailable(Boolean onlyAvailable) {
		this.onlyAvailable = onlyAvailable;
	}

	public CourseSortFieldEnum getSortBy() {
		return sortBy;
	}

	public void setSortBy(CourseSortFieldEnum sortBy) {
		this.sortBy = sortBy;
	}

	public SortDirectionEnum getSortDirection() {
		return sortDirection;
	}

	public void setSortDirection(SortDirectionEnum sortDirection) {
		this.sortDirection = sortDirection;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}
	
	public String getHashtag() {
		return hashtag;
	}
	
	public void setHashtag(String hashtag) {
		this.hashtag = hashtag;
	}
}
