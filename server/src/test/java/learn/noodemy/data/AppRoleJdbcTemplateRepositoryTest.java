package learn.noodemy.data;

import learn.noodemy.model.AppRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AppRoleJdbcTemplateRepositoryTest {

    @Autowired
    private AppRoleJdbcTemplateRepository repository;
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
        List<AppRole> actual = repository.findAll();

        // Assert
        assertNotNull(actual);
        assertTrue(actual.size() >= 4 && actual.size() <= 6);
    }

    @Test
    void shouldFindByRoleName() {
        // Arrange
        String roleName = "ADMIN";

        // Act
        AppRole actual = repository.findByRoleName(roleName);

        // Assert
        assertNotNull(actual);
        assertEquals(1, actual.getRoleId());
        assertEquals("ADMIN", actual.getRoleName());
        assertEquals("Instructor", actual.getRoleDescription());
    }

    @Test
    void shouldAdd() {
        // Arrange
        AppRole appRole = new AppRole(0, "TEST", "Test role");

        // Act
        AppRole actual = repository.add(appRole);

        // Assert
        assertNotNull(actual);
        assertEquals(5, actual.getRoleId());
        assertEquals("TEST", actual.getRoleName());
        assertEquals("Test role", actual.getRoleDescription());
    }

    @Test
    void shouldDeleteById_WhenRoleNotUsed() {
        // Arrange
        int id = 4;

        // Act
        boolean firstDelete = repository.deleteById(id);
        boolean secondDelete = repository.deleteById(id);

        // Assert
        assertTrue(firstDelete);
        assertFalse(secondDelete);
    }
}
