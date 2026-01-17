package tutothr.role;

import java.util.Collection;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import tutothr.common.utils.enums.RolesEnum;
import tutothr.user.User;

@Entity
@Table(name="role")
public class Role {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String description;

	@ManyToMany(mappedBy = "roles")
    private Collection<User> users;

	@Enumerated(EnumType.STRING)
	private RolesEnum type;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setType(RolesEnum type) {
		this.type = type;
	}

	public RolesEnum getType() {
		return type;
	}

}


