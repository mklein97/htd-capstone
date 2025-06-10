package learn.noodemy.data;

import learn.noodemy.model.AppUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AppUserJdbcTemplateRepositoryTest {

    @Autowired
    private AppUserJdbcTemplateRepository repository;
    @Autowired
    private KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldAdd() {
        // Arrange
        AppUser appUser = new AppUser(
                0,
                "username",
                "password",
                false,
                new ArrayList<>()
        );
        appUser.setRoleId(1);

        // Act
        AppUser actual = repository.add(appUser);

        // Assert
        assertNotNull(actual);
        assertEquals(6, actual.getAppUserId());
    }


    @Test
    void shouldFindById() {
        // Arrange
        int appUserId = 1;

        // Act
        AppUser actual = repository.findById(appUserId);

        // Assert
        assertNotNull(actual);
        assertEquals(1, actual.getAppUserId());
        assertEquals("noodemyAdmin", actual.getUsername());
        assertEquals("admin", actual.getPassword());
        assertEquals(false, actual.isDisabled());
        assertEquals(1, actual.getAppUserId());
    }

    @Test
    void shouldFindByUsername() {
        // Arrange
        String username = "noodemyAdmin";

        // Act
        AppUser actual = repository.findByUsername(username);

        // Assert
        assertNotNull(actual);
        assertEquals(1, actual.getAppUserId());
        assertEquals("noodemyAdmin", actual.getUsername());
        assertEquals("admin", actual.getPassword());
        assertEquals(false, actual.isDisabled());
        assertEquals(1, actual.getAppUserId());
    }

    @Test
    void deleteById_ShouldSetDisabledToTrue() {
        // Arrange
        int id = 4;

        // Act
        boolean firstDelete = repository.deleteById(id);
        boolean secondDelete = repository.deleteById(id);

        // Assert
        assertTrue(firstDelete);
        assertFalse(secondDelete);
    }

    @Test
    void shouldReturnTrueIfAppUserEnabled() {
        // Arrange
        int enabledId = 1;
        int disabledId = 5;

        // Act
        boolean shouldTrue = repository.isEnabled(enabledId);
        boolean shouldFalse = repository.isEnabled(disabledId);

        // Assert
        assertTrue(shouldTrue);
        assertFalse(shouldFalse);
    }
}
