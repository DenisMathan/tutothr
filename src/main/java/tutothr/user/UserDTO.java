package tutothr.user;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import tutothr.common.BaseDTO;

public class UserDTO extends BaseDTO {

    @NotBlank(message = "Username is mandatory")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters.")
    private String username;
    private String email;
    private Boolean admin;
    private Boolean tutor;
    private Boolean student;
    private Integer strikes;
    private Boolean verified;
    private Boolean accountNonLocked;

    @Override
    public void initFields() {
        // Default to safe fields for normal users
        setUserFields();
    }

    public void setAdminFields() {
        formFields = List.of(
                new tutothr.common.models.Field("username", "Benutzername", "text"),
                // new tutothr.common.models.Field("email", "E-Mail", "email"),
                new tutothr.common.models.Field("roles", "Rollen", "group", List.of(
                        new tutothr.common.models.Field("admin", "Administrator", "checkbox"),
                        new tutothr.common.models.Field("tutor", "Tutor", "checkbox"),
                        new tutothr.common.models.Field("student", "Student", "checkbox"))),
                new tutothr.common.models.Field("strikes", "Strikes", "number"),
                new tutothr.common.models.Field("verified", "Verifiziert", "checkbox"),
                new tutothr.common.models.Field("accountNonLocked", "Nicht gesperrt", "checkbox"));
    }

    public void setUserFields() {
        formFields = List.of(
            new tutothr.common.models.Field("username", "Benutzername", "text"),
            new tutothr.common.models.Field("roles", "Rollen", "group", List.of(
                new tutothr.common.models.Field("admin", "Administrator", "checkbox", true),
                new tutothr.common.models.Field("tutor", "Tutor", "checkbox", true),
                new tutothr.common.models.Field("student", "Student", "checkbox", true)
            )),
            new tutothr.common.models.Field("strikes", "Verwarnungen", "number", true),
            new tutothr.common.models.Field("verified", "Verifiziert", "checkbox", true)
        );
    }

    public void clearAdminFields() {
            this.setAdmin(null);
            this.setTutor(null);
            this.setStudent(null);
            this.setStrikes(null);
            this.setVerified(null);
            this.setAccountNonLocked(null);
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

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public Boolean getTutor() {
        return tutor;
    }

    public void setTutor(Boolean tutor) {
        this.tutor = tutor;
    }

    public Boolean getStudent() {
        return student;
    }

    public void setStudent(Boolean student) {
        this.student = student;
    }

    public Integer getStrikes() {
        return strikes;
    }

    public void setStrikes(Integer strikes) {
        this.strikes = strikes;
    }

    public Boolean isVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(Boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }
}
