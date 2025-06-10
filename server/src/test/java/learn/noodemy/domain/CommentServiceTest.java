package learn.noodemy.domain;

import learn.noodemy.data.CommentRepository;
import learn.noodemy.data.EnrollmentRepository;
import learn.noodemy.model.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;
    @MockitoBean
    private CommentRepository commentRepository;
    @MockitoBean
    private EnrollmentRepository enrollmentRepository;

    @Test
    void shouldAdd() {
        // Arrange
        Comment comment = makeComment();
        Comment expected = makeComment();
        expected.setCommentId(1);

        when(commentRepository.add(comment))
                .thenReturn(expected);
        when(enrollmentRepository.existsById(comment.getEnrollmentId()))
                .thenReturn(true);

        // Act
        Result<Comment> actual = commentService.add(comment);

        // Assert
        assertNotNull(actual);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(expected, actual.getPayload());
    }
    @Test
    void shouldNotAddNullComment() {
        // Arrange

        // Act
        Result<Comment> actual = commentService.add(null);

        // Assert
        assertNotNull(actual);
        assertEquals(ResultType.INVALID, actual.getType());
    }
    @Test
    void shouldNotAddBlankComment() {
        // Arrange
        Comment comment = makeComment();
        comment.setComment("  ");

        when(enrollmentRepository.existsById(comment.getEnrollmentId()))
                .thenReturn(true);

        // Act
        Result<Comment> actual = commentService.add(comment);

        // Assert
        assertNotNull(actual);
        assertEquals(ResultType.INVALID, actual.getType());
        assertNull(actual.getPayload());
    }
    @Test
    void shouldNotAddInvalidEnrollment() {
        // Arrange
        Comment comment = makeComment();

        when(enrollmentRepository.existsById(comment.getEnrollmentId()))
                .thenReturn(false);

        // Act
        Result<Comment> actual = commentService.add(comment);

        // Assert
        assertNotNull(actual);
        assertEquals(ResultType.INVALID, actual.getType());
        assertNull(actual.getPayload());
    }
    @Test
    void shouldNotAddInvalidDate() {
        // Arrange
        Comment comment = makeComment();
        comment.setCreatedAt(LocalDate.now().plusDays(1));

        when(enrollmentRepository.existsById(comment.getEnrollmentId()))
                .thenReturn(true);

        // Act
        Result<Comment> actual = commentService.add(comment);

        // Assert
        assertNotNull(actual);
        assertEquals(ResultType.INVALID, actual.getType());
        assertNull(actual.getPayload());
    }

    @Test
    void deleteById_ShouldDeleteIfExists() {
        // Arrange
        int deletingId = 1;

        when(commentRepository.deleteById(1)).thenReturn(true);

        // Act
        Result<Comment> actual = commentService.deleteById(deletingId);

        // Assert
        assertNotNull(actual);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertNull(actual.getPayload());
    }
    @Test
    void deleteById_ShouldReturnErrorsIfNotFound() {
        // Arrange
        int deletingId = 1;

        when(commentRepository.deleteById(1)).thenReturn(false);

        // Act
        Result<Comment> actual = commentService.deleteById(deletingId);

        // Assert
        assertNotNull(actual);
        assertEquals(ResultType.NOT_FOUND, actual.getType());
        assertNull(actual.getPayload());
    }

    @Test
    void shouldUpdate() {
        // Arrange
        Comment comment = makeComment();

        when(enrollmentRepository.existsById(comment.getEnrollmentId()))
                .thenReturn(true);
        when(commentRepository.update(comment))
                .thenReturn(true);

        // Act
        Result<Comment> actual = commentService.update(comment);

        // Assert
        assertNotNull(actual);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertNull(actual.getPayload());
    }
    @Test
    void shouldNotUpdateNullComment() {
        // Arrange


        // Act
        Result<Comment> actual = commentService.update(null);

        // Assert
        assertNotNull(actual);
        assertEquals(ResultType.INVALID, actual.getType());
        assertNull(actual.getPayload());
    }
    @Test
    void shouldNotUpdateBlankComment() {
        // Arrange
        Comment comment = makeComment();
        comment.setComment("  ");

        // Act
        Result<Comment> actual = commentService.update(comment);

        // Assert
        assertNotNull(actual);
        assertEquals(ResultType.INVALID, actual.getType());
        assertNull(actual.getPayload());
    }
    @Test
    void shouldNotUpdateInvalidEnrollment() {
        // Arrange
        Comment comment = makeComment();
        comment.setEnrollmentId(1);

        when(enrollmentRepository.existsById(comment.getEnrollmentId())).thenReturn(false);

        // Act
        Result<Comment> actual = commentService.update(comment);

        // Assert
        assertNotNull(actual);
        assertEquals(ResultType.INVALID, actual.getType());
        assertNull(actual.getPayload());
    }
    @Test
    void shouldNotUpdateInvalidDate() {
        // Arrange
        Comment comment = makeComment();
        comment.setCreatedAt(LocalDate.now().plusDays(1));

        when(enrollmentRepository.existsById(comment.getEnrollmentId()))
                .thenReturn(true);

        // Act
        Result<Comment> actual = commentService.update(comment);

        // Assert
        assertNotNull(actual);
        assertEquals(ResultType.INVALID, actual.getType());
        assertNull(actual.getPayload());
    }

    private Comment makeComment() {
        Comment comment = new Comment();
        comment.setComment("Test comment");
        comment.setCreatedAt(LocalDate.now());
        return comment;
    }
}
