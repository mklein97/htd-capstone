package learn.noodemy.domain;

import learn.noodemy.data.EnrollmentRepository;
import learn.noodemy.data.UserProfileRepository;
import learn.noodemy.model.Course;
import learn.noodemy.model.EnrollmentCourse;
import learn.noodemy.model.EnrollmentUserProfile;
import learn.noodemy.model.UserProfile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
class EnrollmentServiceTest {

    @MockitoBean
    EnrollmentRepository repository;

    @MockitoBean
    UserProfileRepository userProfileRepository;

    @Autowired
    EnrollmentService service;

    @Test
    void shouldFail_whenIdDoesNotExist() {
        when(repository.existsById(anyInt())).thenReturn(false);

        Result<Boolean> result = service.existsById(999);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.NOT_FOUND, result.getType());
    }

    @Test
    void shouldFail_whenAddEnrolledCourse() {
        when(userProfileRepository.findById(anyInt())).thenReturn(makeUserProfile());
        Result<Boolean> result = service.add(1,1);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());
        assertTrue(result.getMessages().contains("The user is already enrolled in this course"));
    }

    @Test
    void shouldFail_whenDeleteNonExistingId() {
        when(repository.deleteById(anyInt())).thenReturn(false);

        Result<Boolean> result = service.deleteById(999);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.NOT_FOUND, result.getType());
    }

    UserProfile makeUserProfile() {
        Course course = new Course();
        course.setCourseId(1);

        EnrollmentUserProfile enrollmentUserProfile = new EnrollmentUserProfile(1, course);

        UserProfile userProfile = new UserProfile();
        userProfile.setEnrollmentList(List.of(enrollmentUserProfile));
        userProfile.setUserId(1);

        return userProfile; // user 1 has enrolled in course 1
    }
}