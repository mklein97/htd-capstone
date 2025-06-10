package learn.noodemy.domain;

import learn.noodemy.data.CategoryRepository;
import learn.noodemy.data.CourseRepository;
import learn.noodemy.model.Category;
import learn.noodemy.model.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
class CourseServiceTest {

    @Autowired
    CourseService service;

    @MockitoBean
    CourseRepository repository;

    @MockitoBean
    CategoryRepository categoryRepository;

    @Test
    void shouldFail_whenIdNotExist() {
        Result<Course> result = service.findById(1);

        when(repository.findById(anyInt())).thenReturn(null);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.NOT_FOUND, result.getType());
        assertTrue(result.getMessages().contains("Could not find the course with this id"));
    }

    @Test
    void shouldFail_whenCourseIsNull() {
        Result<Course> result = service.add(null);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldFail_whenNameIsBlank() {
        Course course = makeCourse();
        course.setCourseName("");

        when(categoryRepository.findById(anyInt())).thenReturn(new Category());

        Result<Course> result = service.add(course);

        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Course name cannot be blank"));
    }

    @Test
    void shouldFail_whenDescriptionIsBlank() {
        Course course = makeCourse();
        course.setCourseDescription("");

        when(categoryRepository.findById(anyInt())).thenReturn(new Category());

        Result<Course> result = service.add(course);

        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Description cannot be empty"));
    }

    @Test
    void shouldFail_whenPriceIsNotWithinBound() {
        Course course = makeCourse();
        course.setPrice(BigDecimal.valueOf(10_000));

        when(categoryRepository.findById(anyInt())).thenReturn(new Category());

        Result<Course> result = service.add(course);

        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Price must be between 1 and 1000"));

        course.setPrice(BigDecimal.ZERO);
        result = service.add(course);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Price must be between 1 and 1000"));
    }

    @Test
    void shouldFail_whenHourIsNotWithinBound() {
        Course course = makeCourse();
        course.setEstimateDuration(500);
        Result<Course> result = service.add(course);

        when(categoryRepository.findById(anyInt())).thenReturn(new Category());

        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Duration must be between 1 and 100 hours."));

        course.setEstimateDuration(0);
        result = service.add(course);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Duration must be between 1 and 100 hours."));
    }

    @Test
    void shouldFail_whenCategoryIsNotValid() {
        Course course = makeCourse();

        Category category = new Category();
        category.setCategoryId(9999);

        course.setCategory(category);

        when(categoryRepository.findById(anyInt())).thenReturn(null);

        Result<Course> result = service.add(course);

        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Could not find this course category"));

    }

    @Test
    void shouldFail_whenMissingCourseId() {
        Course course = makeCourse();
        course.setCourseId(0);

        when(categoryRepository.findById(anyInt())).thenReturn(new Category());

        Result<Course> result = service.update(course);
        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());
        assertTrue(result.getMessages().contains("Course Id is missing"));
    }

    @Test
    void shouldFail_whenCourseDoesNotExist() {
        Course course = makeCourse();
        course.setCourseId(9999);

        when(categoryRepository.findById(anyInt())).thenReturn(new Category());
        when(repository.update(any())).thenReturn(false);

        Result<Course> result = service.update(course);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.NOT_FOUND, result.getType());
        assertTrue(result.getMessages().contains("Could not locate the course to be updated."));
    }

    @Test
    void shouldFail_whenDeleteBadId() {
        when(repository.delete(anyInt())).thenReturn(false);

        Result<Course> result = service.delete(9999);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.NOT_FOUND, result.getType());
        assertTrue(result.getMessages().contains("Could not find the course id."));
    }

    private Course makeCourse() {

        Category category = new Category();
        category.setCategoryId(1);

        return new Course(
                1,
                "Name",
                "Description",
                BigDecimal.valueOf(50),
                50,
                category
        );
    }
}