package learn.noodemy.data;

import learn.noodemy.model.Comment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CommentJdbcTemplateRepositoryTest {

    @Autowired
    private CommentJdbcTemplateRepository repository;
    @Autowired
    private KnownGoodState knownGoodState;

    @BeforeEach
    void setUp() {
        knownGoodState.set();
    }

    @Test
    void shouldFindAll() {
        // Arrange

        // Act
        List<Comment> comments = repository.findAll();

        // Assert
        assertTrue(comments.size() >= 2 && comments.size() <= 3);
    }

    @Test
    void shouldFindById() {
        // Arrange
        int commentId = 1;

        // Act
        Comment actual = repository.findById(commentId);

        // Assert
        assertNotNull(actual);
        assertEquals(commentId, actual.getCommentId());
        assertEquals("2025-06-02", actual.getCreatedAt().toString());
        assertEquals("This class is so hard!", actual.getComment());
        assertEquals(1, actual.getEnrollmentId());
    }

    @Test
    void shouldFindByCourseId() {
        // Arrange
        int courseId = 2;
        List<Comment> expected = List.of(
                new Comment(
                        1,
                        "This class is so hard!",
                        LocalDate.of(2025, 6, 2),
                        1)
        );
        Comment expectedComment = expected.get(0);

        // Act
        List<Comment> actual = repository.findByCourseId(courseId);
        Comment actualComment = actual.get(0);

        // Assert
        assertEquals(expected.size(), actual.size());
        assertEquals(expectedComment.getCommentId(), actualComment.getCommentId());
        assertEquals(expectedComment.getComment(), actualComment.getComment());
        assertEquals(expectedComment.getCreatedAt(), actualComment.getCreatedAt());
        assertEquals(expectedComment.getEnrollmentId(), actualComment.getEnrollmentId());
    }

    @Test
    void shouldFindByUserId() {
        // Arrange
        int userId = 2;
        List<Comment> expected = List.of(
                new Comment(
                        1,
                        "This class is so hard!",
                        LocalDate.of(2025, 6, 2),
                        1)
        );
        Comment expectedComment = expected.get(0);

        // Act
        List<Comment> actual = repository.findByUserId(userId);
        Comment actualComment = actual.get(0);

        // Assert
        assertEquals(expected.size(), actual.size());
        assertEquals(expectedComment.getCommentId(), actualComment.getCommentId());
        assertEquals(expectedComment.getComment(), actualComment.getComment());
        assertEquals(expectedComment.getCreatedAt(), actualComment.getCreatedAt());
        assertEquals(expectedComment.getEnrollmentId(), actualComment.getEnrollmentId());
    }


    @Test
    void shouldAdd() {
        // Arrange
        Comment comment = new Comment(
                0,
                "Test comment",
                LocalDate.of(2025, 6, 2),
                2
        );

        // Act
        Comment actual = repository.add(comment);

        // Assert
        assertNotNull(actual);
        assertEquals(3, actual.getCommentId());
    }

    @Test
    void shouldUpdate() {
        // Arrange
        int updatingId = 2;
        Comment comment = new Comment(
                updatingId,
                "Updated test comment",
                LocalDate.of(2025, 6, 2),
                1
        );

        // Act
        boolean actual = repository.update(comment);
        Comment updatedComment = repository.findById(updatingId);

        // Assert
        assertTrue(actual);
        assertEquals("Updated test comment", updatedComment.getComment());
    }

    @Test
    void shouldDeleteById() {
        // Arrange
        int deletingId = 2;

        // Act
        boolean firstDelete = repository.deleteById(deletingId);
        boolean secondDelete = repository.deleteById(deletingId);

        // Assert
        assertTrue(firstDelete);
        assertFalse(secondDelete);

    }
}
