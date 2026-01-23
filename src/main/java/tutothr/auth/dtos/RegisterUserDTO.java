package tutothr.auth.dtos;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import tutothr.common.BaseDTO;
import tutothr.common.models.Field;

public class RegisterUserDTO extends BaseDTO {
	private static final long serialVersionUID = 1L;
	@NotBlank(message = "Username is mandatory")
	@Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters.")
	private String username;
	@NotBlank(message = "Password is mandatory")
	@Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters.")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,20}$", message = "Password must contain at least one uppercase letter, one lowercase letter, and one number.")
    private String password;
	@NotBlank(message = "Email is mandatory")
	@Email(message = "Email should be valid")
	private String email;
	private String confirmPassword;

	@Override
	public void initFields() {
		formFields = List.of(
				new Field("username", "field.username", "text"),
				new Field("email", "field.email", "text"),
				new Field("password", "field.password", "password"),
				new Field("confirmPassword", "field.confirmPassword", "password")
		);
		setSubmitLabel("register.submit");
	}

	public String getUsername() {
		return this.username;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

}
