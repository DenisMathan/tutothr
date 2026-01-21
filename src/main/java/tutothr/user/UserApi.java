package tutothr.user;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Endpoints for managing users")
public class UserApi {

    private final UserService userService;

    public UserApi(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all users", description = "Get a paginated list of all users (Admin only)")
    public ResponseEntity<Page<UserDTO>> getAllUsers(
            @RequestParam(required = false) String keyword,
            @ParameterObject Pageable pageable) {

        Page<UserDTO> users = userService.getAllUsersDTO(keyword, pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/banned")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get banned users", description = "Get a list of all banned users (Admin only)")
    public ResponseEntity<List<User>> getBannedUsers() {
        return ResponseEntity.ok(userService.getBannedUsers());
    }

    @GetMapping("/with-strikes")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get users with strikes", description = "Get a list of all users with strikes (Admin only)")
    public ResponseEntity<List<User>> getUsersWithStrikes() {
        return ResponseEntity.ok(userService.getUsersWithStrikes());
    }

    @GetMapping("/{id}/banned")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Check if user is banned", description = "Check if a specific user is banned (Admin only)")
    public ResponseEntity<Boolean> isUserBanned(@PathVariable Long id) {
        return ResponseEntity.ok(userService.isUserBanned(id));
    }


}
