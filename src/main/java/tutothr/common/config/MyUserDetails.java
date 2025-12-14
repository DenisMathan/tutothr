package tutothr.common.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import tutothr.role.Role;
import tutothr.user.User;

public class MyUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;

	private String userName;
	private String password;
	private String email;
	private Long id;
	private boolean active;
	private List<GrantedAuthority> authorities;
	private Collection<Role> roles;


	public MyUserDetails(User user) {
		this.userName= user.getUsername();
		this.password= user.getPassword();
		this.active = user.isActive();
		this.email = user.getEmail();
		this.id = user.getId();
		
		this.roles = user.getRoles();
		this.authorities = new ArrayList<>();

		for (Role role : roles) {
			// Nutze description oder type.name() für die Authority
			String roleName = role.getDescription(); 
			if (roleName != null && !roleName.isBlank()) {
				this.authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName.toUpperCase()));
			}
			// Falls du auch den Enum-Type nutzen willst:
			if (role.getType() != null) {
				this.authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getType().name()));
			}
		}
	}
	public String getEmail() {
		return email;
	}
	
	public Long getId() {
		return id;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.active;
	}

}
