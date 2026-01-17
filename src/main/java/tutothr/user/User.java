package tutothr.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import tutothr.auth.AuthProvider;
import tutothr.common.BaseEntity;
import tutothr.role.Role;
import jakarta.persistence.JoinColumn;


import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

@Entity
@Table(name="user")
public class User extends BaseEntity implements Serializable {
	ArrayList<Long> myCourses;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@Enumerated(EnumType.STRING)
	private AuthProvider authProvider;

	private String username;
	

	private String password;
		
	@NotBlank(message = "Email is mandatory")
	private String email;

	
	private boolean active = true;

	private boolean twoFactorEnabled = false;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name="userrole",
			joinColumns = @JoinColumn(name="iduser"),
			inverseJoinColumns = @JoinColumn(name="idrole")
			)
	private Set<Role> roles = new HashSet<>();

	private boolean verified;

	private int strikes = 0;
	private boolean accountNonLocked = true;


	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
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

	public AuthProvider getAuthProvider() {
		return authProvider;
	}

	public void setAuthProvider(AuthProvider authProvider) {
		this.authProvider = authProvider;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public boolean isTwoFactorEnabled() {
		return twoFactorEnabled;
	}
	public void setTwoFactorEnabled(boolean twoFactorEnabled) {
		this.twoFactorEnabled = twoFactorEnabled;
	}

	public int getStrikes() { return strikes; }
	public void setStrikes(int strikes) { this.strikes = strikes; }

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}
	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}
}
