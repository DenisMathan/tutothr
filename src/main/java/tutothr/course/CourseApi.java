package tutothr.course;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import tutothr.auth.config.AppPrincipal;
@RestController
@RequestMapping("/api/courses")
@Tag(name = "Courses", description = "Endpoints for managing courses")
public class CourseApi {

    private final CourseService courseService;

    public CourseApi(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    @Operation(summary = "Get all courses", description = "Get a list of all courses")
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllDTOs());
    }

    @GetMapping("/filter")
    @Operation(summary = "Search courses", description = "Get a list of courses with optional filtering")
    public ResponseEntity<Page<CourseDTO>> search(CourseSearchDTO searchDTO) {
        return ResponseEntity.ok(courseService.search(searchDTO));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get course details", description = "Get a single course by ID")
    public ResponseEntity<CourseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.findDTOById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('TUTOR') or hasRole('ADMIN')")
    @Operation(summary = "Create a new course", description = "Creates a new course (Tutor only)")
    public ResponseEntity<CourseDTO> create(@RequestBody @Valid CourseDTO courseDTO, @AuthenticationPrincipal AppPrincipal user) {
        courseDTO.setId(null); // Sicherstellen, dass eine neue Entitaet erstellt wird
        Course courseEntity = courseService.mapToEntity(courseDTO);
        courseEntity.setOwner(user.getDbUser());
        System.out.println("Creating course for user ID: " + user.getId());
        courseService.save(courseEntity);
        courseDTO.setId(courseEntity.getId());
        // courseService.saveDTO(courseDTO);
        return ResponseEntity.ok(courseDTO);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@coursePermissionService.isTutorAndOwnerOrAdmin(#id)")
    @Operation(summary = "Update a course", description = "Updates an existing course (Owner or Admin only)")
    public ResponseEntity<CourseDTO> update(@PathVariable Long id, @RequestBody Map<String, Object> fields) {
        CourseDTO courseDTO = courseService.patch(id, fields);
        return ResponseEntity.ok(courseDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@coursePermissionService.isTutorAndOwnerOrAdmin(#id)")
    @Operation(summary = "Delete a course", description = "Deletes a course (Owner or Admin only)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        courseService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}

