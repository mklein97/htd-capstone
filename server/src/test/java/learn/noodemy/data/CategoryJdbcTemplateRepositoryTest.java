package learn.noodemy.data;

import learn.noodemy.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CategoryJdbcTemplateRepositoryTest {

    @Autowired
    private CategoryJdbcTemplateRepository repository;
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
        List<Category> categories = repository.findAll();

        // Assert
        assertEquals(6, categories.size());
    }

    @Test
    void shouldFindById() {
        // Arrange
        int categoryId = 1;

        // Act
        Category actual = repository.findById(categoryId);

        // Assert
        assertNotNull(actual);
        assertEquals("Computer Science", actual.getCategoryName());
        assertEquals("Covers programming and IT-related subjects", actual.getCategoryDescription());
        assertEquals("CS", actual.getCategoryCode());
    }
}
