package tutothr.auth.dtos;

import java.util.List;

import tutothr.common.BaseDTO;
import tutothr.common.models.Field;

public class LoginUserDTO extends BaseDTO {
    private String password;
	private String email;

	@Override
	public void initFields() {
		formFields = List.of(
				new Field("email", "field.email", "text"),
				new Field("password", "field.password", "password")
		);
		setSubmitLabel("login.submit");
	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
