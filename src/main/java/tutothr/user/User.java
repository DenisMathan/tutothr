package tutothr.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import groovyjarjarantlr4.v4.parse.ANTLRParser.id_return;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import tutothr.role.Role;
import jakarta.persistence.JoinColumn;

// @Entity
// class EnrolledCourses {
// @Table(name="")
// }

@Entity
@Table(name="user")
@Inheritance(strategy=InheritanceType.JOINED)
//more about: https://stackabuse.com/guide-to-jpa-with-hibernate-inheritance-mapping/
public class User implements Serializable{
	// Long id;
	// String email;
	String firstName;
	String lastName;
	ArrayList<Long> myCourses;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	Long id;
	
	@NotBlank(message = "username is mandatory")
	private String username;
	
	@NotBlank(message = "password is mandatory")
	private String password;
		
	@NotBlank(message = "Email is mandatory")
	private String email;

	
	private boolean active = true;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name="userrole",
			joinColumns = @JoinColumn(name="iduser"),
			inverseJoinColumns = @JoinColumn(name="idrole")
			)
	private List<Role> roles = new ArrayList<Role>();

	
	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
