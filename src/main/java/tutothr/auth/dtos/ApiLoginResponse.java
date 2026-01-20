package tutothr.auth.dtos;

public class ApiLoginResponse {
    private String token;

    public ApiLoginResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
