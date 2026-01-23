package tutothr.hashtag;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import tutothr.common.BaseEntity;

@Entity
public class Hashtag extends BaseEntity {
	@Column(nullable = false, unique = true)
	private String name;
	
	// Getter und Setter
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
