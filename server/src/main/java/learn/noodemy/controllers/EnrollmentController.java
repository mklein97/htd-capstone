package learn.noodemy.controllers;

import learn.noodemy.domain.EnrollmentService;
import learn.noodemy.domain.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService service;

    public EnrollmentController(EnrollmentService service) {
        this.service = service;
    }

    @PostMapping("/{courseId}/{userId}")
    public ResponseEntity<Object> addEnrollment(@PathVariable int courseId,
                                                @PathVariable int userId) {
        Result<Boolean> result = service.add(courseId, userId);

        if (result.isSuccess()) {
            return ResponseEntity.noContent().build();
        }

        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{enrollmentId}")
    public ResponseEntity<Object> deleteEnrollment(@PathVariable int enrollmentId) {
        Result<Boolean> result = service.deleteById(enrollmentId);

        if (result.isSuccess()) {
            return ResponseEntity.noContent().build();
        }

        return ErrorResponse.build(result);
    }

}
