package tutothr.user;

import java.util.Set;
import java.util.List;

import tutothr.common.BaseDTO;
import tutothr.role.Role;

public class UserDTO extends BaseDTO {
    private String username;
    private String email;
    private Set<Role> roles;
    private int strikes;
    private boolean verified;
    private boolean accountNonLocked;

    @Override
    public void initFields() {
        formFields = List.of(
            new tutothr.common.models.Field("username", "Benutzername", "text"),
            new tutothr.common.models.Field("email", "E-Mail", "email"),
            new tutothr.common.models.Field("roles", "Rollen", "dropdown-multiple")
        );
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public Set<Role> getRoles() {
        return roles;
    }
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    public int getStrikes() {
        return strikes;
    }
    public void setStrikes(int strikes) {
        this.strikes = strikes;
    }
    public boolean isVerified() {
        return verified;
    }
    public void setVerified(boolean verified) {
        this.verified = verified;
    }
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }
    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }
}
