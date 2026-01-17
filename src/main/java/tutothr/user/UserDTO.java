package tutothr.user;

import java.util.List;

import tutothr.common.BaseDTO;

public class UserDTO extends BaseDTO {
    private String username;
    private String email;
    private boolean admin;
    private boolean tutor;
    private boolean student;
    private int strikes;
    private boolean verified;
    private boolean accountNonLocked;

    @Override
    public void initFields() {
        formFields = List.of(
            new tutothr.common.models.Field("username", "Benutzername", "text"),
            new tutothr.common.models.Field("email", "E-Mail", "email"),
            new tutothr.common.models.Field("admin", "Administrator", "checkbox"),
            new tutothr.common.models.Field("tutor", "Tutor", "checkbox"),
            new tutothr.common.models.Field("student", "Student", "checkbox")
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

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isTutor() {
        return tutor;
    }

    public void setTutor(boolean tutor) {
        this.tutor = tutor;
    }

    public boolean isStudent() {
        return student;
    }

    public void setStudent(boolean student) {
        this.student = student;
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
