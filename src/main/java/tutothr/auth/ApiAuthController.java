package tutothr.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import tutothr.auth.dtos.ApiLoginRequest;
import tutothr.auth.dtos.ApiLoginResponse;
import tutothr.auth.jwt.TokenService;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication")
public class ApiAuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public ApiAuthController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    @Operation(summary = "Login to get a JWT token", description = "Authenticates user using email and password and returns a JWT token.")
    public ResponseEntity<?> authenticate(@RequestBody ApiLoginRequest request) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            // If authentication successful, generate token
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwtToken = tokenService.generateToken(userDetails);

            return ResponseEntity.ok(new ApiLoginResponse(jwtToken));
        } catch (org.springframework.security.core.AuthenticationException e) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }
}
