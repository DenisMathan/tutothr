package tutothr.auth.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

public class ApiLoginRequest {
    @Schema(example = "test@test.de", description = "User email address")
    private String email;

    @Schema(example = "test", description = "User password")
    private String password;

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
