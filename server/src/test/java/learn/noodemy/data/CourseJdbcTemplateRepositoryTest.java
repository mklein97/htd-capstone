package learn.noodemy.data;

import learn.noodemy.model.Category;
import learn.noodemy.model.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CourseJdbcTemplateRepositoryTest {

    private final int NEXT_COURSE_ID = 4;

    @Autowired
    CourseJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setUp() { knownGoodState.set(); }

    @Test
    void findAll() {
        List<Course> actual = repository.findAll();
        assertTrue(actual.size() >= 2);
    }

    @Test
    void findById() {
        Course actual = repository.findById(1);

        assertEquals(1, actual.getCourseId());
        assertTrue(actual.getEnrollmentList().size() >= 2);
    }

    @Test
    void shouldFindAllByUserId() {
        // Arrange
        int userId = 2;

        // Act
        List<Course> actual = repository.findAllByUserId(userId);

        // Assert
        assertTrue(actual.size() >= 1 && actual.size() <=3);
    }

    @Test
    void add() {
        Course course = makeCourse();

        Course actual = repository.add(course);

        assertEquals(NEXT_COURSE_ID, actual.getCourseId());
        assertEquals("Automata", actual.getCourseName());

    }

    @Test
    void update() {
        Course course = makeCourse();
        course.setCourseId(1);
        course.setCourseName("New name");

        repository.update(course);

        Course actual = repository.findById(course.getCourseId());

        assertEquals(1, actual.getCourseId());
        assertEquals("New name", actual.getCourseName());

    }

    @Test
    void delete() {
        assertTrue(repository.delete(3));
        assertFalse(repository.delete(3));
    }

    Course makeCourse() {
        Category category = new Category();
        category.setCategoryId(1);

        Course course = new Course();
        course.setCourseId(0);
        course.setCourseName("Automata");
        course.setCourseDescription("Learn about states and algorithm");
        course.setPrice(BigDecimal.TEN);
        course.setEstimateDuration(50);
        course.setCategory(category);
        return course;
    }
}