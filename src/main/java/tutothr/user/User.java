package tutothr.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import tutothr.auth.AuthProvider;
import tutothr.common.BaseEntity;
import tutothr.common.utils.enums.RolesEnum;

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
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
	@Enumerated(EnumType.STRING)
	@Column(name = "role")
	private Set<RolesEnum> roles = new HashSet<>();

	private boolean verified;

	private int strikes = 0;
	private boolean accountNonLocked = true;


	public Set<RolesEnum> getRoles() {
		return roles;
	}

	public void setRoles(Set<RolesEnum> roles) {
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
