package learn.noodemy.data;

import learn.noodemy.model.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserProfileJdbcTemplateRepositoryTest {
    @Autowired
    private UserProfileJdbcTemplateRepository repository;
    @Autowired
    private KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindAll() {
        // Arrange

        // Act
        List<UserProfile> userProfiles = repository.findAll();

        // Assert
        assertTrue(userProfiles.size() >= 4 && userProfiles.size() <= 6);
    }

    @Test
    void shouldFindByEmail() {
        // Arrange
        String email = "admin@gmail.com";

        // Act
        UserProfile actual = repository.findByEmail(email);

        // Assert
        assertNotNull(actual);
        assertEquals("Quang", actual.getFirstName());
        assertEquals("Pham", actual.getLastName());
        assertEquals("1996-01-15", actual.getDob().toString());
        assertEquals("admin@gmail.com", actual.getEmail());
        assertEquals(1, actual.getAppUserId());
    }

    @Test
    void shouldFindByUsername() {
        // Arrange
        String username = "noodemyAdmin";

        // Act
        UserProfile actual = repository.findByUsername(username);

        // Assert
        assertNotNull(actual);
        assertEquals("Quang", actual.getFirstName());
        assertEquals("Pham", actual.getLastName());
        assertEquals("1996-01-15", actual.getDob().toString());
        assertEquals("admin@gmail.com", actual.getEmail());
        assertEquals(1, actual.getAppUserId());
    }

    @Test
    void shouldFindById() {
        // Arrange
        int id = 1;

        // Act
        UserProfile actual = repository.findById(id);

        // Assert
        assertNotNull(actual);
        assertEquals("Quang", actual.getFirstName());
        assertEquals("Pham", actual.getLastName());
        assertEquals("1996-01-15", actual.getDob().toString());
        assertEquals("admin@gmail.com", actual.getEmail());
        assertEquals(1, actual.getAppUserId());

        actual = repository.findById(2); // user with enrolled courses
        assertFalse(actual.getEnrollmentList().isEmpty());
    }

    @Test
    void shouldAdd() {
        // Arrange
        UserProfile userProfile = new UserProfile(
                0,
                "First",
                "Last",
                "email@gmail.com",
                LocalDate.of(2025, 6, 2),
                5
        );

        // Act
        UserProfile actual = repository.add(userProfile);

        // Assert
        assertNotNull(actual);
        assertEquals(5, actual.getAppUserId());
        assertEquals(userProfile.getFirstName(), actual.getFirstName());
        assertEquals(userProfile.getLastName(), actual.getLastName());
        assertEquals(userProfile.getEmail(), actual.getEmail());
        assertEquals(userProfile.getDob(), actual.getDob());
        assertEquals(userProfile.getAppUserId(), actual.getAppUserId());
    }

    @Test
    void shouldUpdate() {
        // Arrange
        int updatingId = 2;
        UserProfile userProfile = new UserProfile(
                2,
                "Updated First",
                "Update Last",
                "updatedEmail@gmail.com",
                LocalDate.of(1996, 1, 1),
                2
        );

        // Act
        boolean result = repository.update(userProfile);
        UserProfile updated = repository.findById(updatingId);

        // Assert
        assertTrue(result);
        assertNotNull(updated);
        assertEquals(updatingId, updated.getAppUserId());
        assertEquals("Updated First", updated.getFirstName());
        assertEquals("Update Last", updated.getLastName());
        assertEquals("updatedEmail@gmail.com", updated.getEmail());
        assertEquals("1996-01-01", updated.getDob().toString());
        assertEquals(2, updated.getAppUserId());
    }

    @Test
    void shouldDeleteById() {
        // Arrange
        int deletingId = 4;

        // Act
        boolean firstDelete = repository.deleteById(deletingId);
        boolean secondDelete = repository.deleteById(deletingId);

        // Assert
        assertTrue(firstDelete);
        assertFalse(secondDelete);
    }
}
