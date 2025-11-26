package tutothr.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import tutothr.model.Authority;
import tutothr.model.Role;
import tutothr.model.User;

public class MyUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;

	private String userName;
	private String password;
	private String email;
	private boolean active;
	private List<GrantedAuthority> authorities;
	private List <Role> roles;


	public MyUserDetails(User user) {
		this.userName= user.getUsername();
		this.password= user.getPassword();
		this.active = user.isActive();
		System.out.println(user);

		//getting roles from the DB
		List<Role> myRoles = (List<Role>) user.getRoles();
		System.out.println("the user "+  user.getUsername() +" has "+
				myRoles.size() +" roles");

		this.email = user.getEmail();
		System.out.println(this.email);
		//authorities is required by Userdetails from Spring Security
		this.roles = myRoles;
		authorities = new ArrayList<>();

		// passing the authorities of each Profile from the DB to the Spring Security collection UserDetails.authorities
		for (int i = 0; i < myRoles.size(); i++) {
			// add role itself as a granted authority using the ROLE_ prefix so hasRole("ADMIN") works
			String roleName = myRoles.get(i).getDescription();
			if (roleName != null && !roleName.isBlank()) {
				authorities.add(new SimpleGrantedAuthority(("ROLE_" + roleName).toUpperCase()));
				System.out.println("added role authority ROLE_" + roleName + " for user " + user.getUsername());
			}

			List<Authority> myAuthsProfile = (List<Authority>) myRoles.get(i).getAuthorities();
			for (Authority auth : myAuthsProfile) {
				authorities.add(new SimpleGrantedAuthority(auth.getDescription().toUpperCase()));
				System.out.println(
					"the authority" + i + " of the profile " + myRoles.get(i).getDescription() + " of the user "
					+ user.getUsername() + " is " + auth.getDescription());
			}

		}

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
