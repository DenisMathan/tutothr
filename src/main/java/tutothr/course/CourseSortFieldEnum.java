package tutothr.course;

/**
 * Sortierkriterien fuer die Kurs-Suche.
 */
public enum CourseSortFieldEnum {
	CREATED_AT("createdAt"),
	PRICE("price"),
	RATING("rating"),
	TITLE("title");
	
	private final String fieldName;
	
	private CourseSortFieldEnum(String fieldName) {
		this.fieldName = fieldName;
	}
	
	public String getFieldName() {
		return fieldName;
	}
}
