package learn.noodemy.controllers;

import learn.noodemy.domain.CommentService;
import learn.noodemy.domain.Result;
import learn.noodemy.model.AppUser;
import learn.noodemy.model.Comment;
import learn.noodemy.model.Course;
import learn.noodemy.model.EnrollmentUserProfile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
public class CommentController {

    private final CommentService commentService;
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/api/courses/{courseId}/comments")
    public List<Comment> getCommentsByCourse(@PathVariable int courseId) {
        return commentService.findAllByCourseId(courseId);
    }

    @GetMapping("/api/users/{userId}/comments")
    public List<Comment> getCommentsByUser(@PathVariable int userId) {
        return commentService.findAllByUserId(userId);
    }

    @PostMapping("/api/comments")
    public ResponseEntity<Object> createComment(@RequestBody Comment comment) {
        Result<Comment> result = commentService.add(comment);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }

        return ErrorResponse.build(result);
    }

    @DeleteMapping("/api/comments/{commentId}")
    public ResponseEntity<Object> deleteComment(@PathVariable int commentId) {
        Result<Comment> result = commentService.deleteById(commentId);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }

    @PutMapping("/api/comments/{commentId}")
    public ResponseEntity<Object> updateComment(@PathVariable int commentId,
                                                @RequestBody Comment comment) {
        if (commentId != comment.getCommentId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<Comment> result = commentService.update(comment);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }
}
