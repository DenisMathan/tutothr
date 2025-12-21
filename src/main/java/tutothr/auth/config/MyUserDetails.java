package tutothr.auth.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import tutothr.role.Role;
import tutothr.user.User;

public class MyUserDetails implements UserDetails, AppPrincipal {

	private static final long serialVersionUID = 1L;

	private User user;
	private String userName;
	private String password;
	private String email;
	private Long id;
	private boolean active;
	private List<GrantedAuthority> authorities;
	private Collection<Role> roles;


	public MyUserDetails(User user) {
		this.user = user;
		this.userName= user.getUsername();
		this.password= user.getPassword();
		this.active = user.isActive();
		this.email = user.getEmail();
		this.id = user.getId();
		
		this.roles = user.getRoles();
		this.authorities = new ArrayList<>();

		for (Role role : roles) {
			if (role.getType() != null) {
				this.authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getType().name()));
			}
		}
	}

	@Override
	public User getDbUser() {
		return this.user;
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
