package learn.noodemy.controllers;

import learn.noodemy.domain.CourseService;
import learn.noodemy.domain.Result;
import learn.noodemy.model.Course;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.findAll();
    }

    @GetMapping("/users/{userId}")
    public List<Course> getAllCoursesByUserId(@PathVariable int userId) {
        return courseService.findAllByUserId(userId);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Object> getCourseById(@PathVariable int courseId) {

        Result<Course> result = courseService.findById(courseId);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getPayload());
        }

        return ErrorResponse.build(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Object> createCourse(@RequestBody Course course) {
        Result<Course> result = courseService.add(course);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }

        return ErrorResponse.build(result);
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<Object> updateCourse(@PathVariable int courseId,
                                               @RequestBody Course course) {
        if (courseId != course.getCourseId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<Course> result = courseService.update(course);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Object> deleteCourse(@PathVariable int courseId) {
        Result<Course> result = courseService.delete(courseId);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }
}
