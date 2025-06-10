package learn.noodemy.data;

import learn.noodemy.model.EnrollmentCourse;
import learn.noodemy.model.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class EnrollmentJdbcTemplateRepositoryTest {

    @Autowired
    private EnrollmentJdbcTemplateRepository repository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private KnownGoodState knownGoodState;

    @BeforeEach
    public void setUp() {
        knownGoodState.set();
    }

    @Test
    void shouldReturnTrueIfEnrollmentExists() {
        // Arrange
        int existingId = 1;
        int nonExistingId = 99;

        // Act
        boolean shouldExist = repository.existsById(existingId);
        boolean shouldNotExist = repository.existsById(nonExistingId);

        // Assert
        assertTrue(shouldExist);
        assertFalse(shouldNotExist);
    }

    @Test
    void shouldAdd() {
        // Arrange
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(1);

        EnrollmentCourse enrollmentCourse = new EnrollmentCourse(
                1,
                userProfile);

        // Act
        boolean result = repository.add(enrollmentCourse);

        // Assert
        assertTrue(result);
    }

    @Test
    void shouldDeleteEnrollmentById() {
        // Arrange
        int deleteId = 3;

        // Act
        boolean firstDelete = repository.deleteById(deleteId);
        boolean secondDelete = repository.deleteById(deleteId);

        // Assert
        assertTrue(firstDelete);
        assertFalse(secondDelete);
    }
}
