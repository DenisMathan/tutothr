package tutothr.hashtag;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import tutothr.common.BaseEntity;
import tutothr.user.User;

@Entity
public class Hashtag extends BaseEntity {
	@Column(nullable = false, unique = true)
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "creator_id")
	private User creator;
	
	// Getter und Setter
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public User getCreator() {
		return creator;
	}
	
	public void setCreator(User creator) {
		this.creator = creator;
	}
}
