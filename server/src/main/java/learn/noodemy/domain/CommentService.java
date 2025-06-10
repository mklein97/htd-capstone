package learn.noodemy.domain;

import learn.noodemy.data.CommentRepository;
import learn.noodemy.data.EnrollmentRepository;
import learn.noodemy.model.Comment;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final EnrollmentRepository enrollmentRepository;
    public CommentService(CommentRepository commentRepository,
                          EnrollmentRepository enrollmentRepository) {
        this.commentRepository = commentRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public List<Comment> findAllByUserId(int userId) {
        return commentRepository.findByUserId(userId);
    }

    public List<Comment> findAllByCourseId(int courseId) {
        return commentRepository.findByCourseId(courseId);
    }

    public Result<Comment> add(Comment comment) {
        Result<Comment> result = validateComment(comment);
        if (!result.isSuccess()) {
            return result;
        }

        comment = commentRepository.add(comment);
        result.setPayload(comment);
        return result;
    }

    public Result<Comment> deleteById(int id) {

        Result<Comment> result = new Result<>();

        if (!commentRepository.deleteById(id)) {
            result.addMessage("Could not find the comment id.", ResultType.NOT_FOUND);
            return result;
        }

        return result;
    }

    public Result<Comment> update(Comment comment) {
        Result<Comment> result = validateComment(comment);
        if (!result.isSuccess()) return result;

        if (!commentRepository.update(comment)) {
            result.addMessage("Could not find the comment id.", ResultType.NOT_FOUND);
            return result;
        }

        return result;
    }

    private Result<Comment> validateComment(Comment comment) {
        Result<Comment> result = new Result<>();

        if (comment == null) {
            result.addMessage("Comment cannot be null", ResultType.INVALID);
            return result;
        }

        if (Validations.isNullOrBlank(comment.getComment())) {
            result.addMessage("Comment cannot be null or blank", ResultType.INVALID);
        }

        if (!enrollmentRepository.existsById(comment.getEnrollmentId())) {
            result.addMessage("Enrollment does not exist", ResultType.INVALID);
        }

        if (comment.getCreatedAt().isAfter(LocalDate.now())) {
            result.addMessage("Comment date must be in the past", ResultType.INVALID);
        }

        return result;
    }
}
