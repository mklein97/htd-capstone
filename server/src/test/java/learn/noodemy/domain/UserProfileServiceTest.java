package learn.noodemy.domain;

import learn.noodemy.data.EnrollmentRepository;
import learn.noodemy.data.UserProfileRepository;
import learn.noodemy.model.UserProfile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.ValueSources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserProfileServiceTest {


    @Autowired
    private UserProfileService userProfileService;
    @MockitoBean
    private UserProfileRepository userProfileRepository;

    @Test
    void shouldAdd() {
        // Arrange
        UserProfile userProfile = makeUserProfile();

        when(userProfileRepository.add(userProfile)).thenReturn(userProfile);

        // Act
        Result<UserProfile> result = userProfileService.add(userProfile);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getPayload());
        assertEquals(ResultType.SUCCESS, result.getType());
    }
    @Test
    void shouldNotAddNullUserProfile() {
        // Arrange

        // Act
        Result<UserProfile> result = userProfileService.add(null);

        // Assert
        assertNotNull(result);
        assertNull(result.getPayload());
        assertEquals(ResultType.INVALID, result.getType());
    }
    @ParameterizedTest
    @MethodSource("invalidFirstLastName")
    void shouldNotAddInvalidFirstLastName(String firstName, String lastName) {
        // Arrange
        UserProfile userProfile = makeUserProfile();
        userProfile.setFirstName(firstName);
        userProfile.setLastName(lastName);

        // Act
        Result<UserProfile> result = userProfileService.add(userProfile);

        // Assert
        assertNotNull(result);
        assertNull(result.getPayload());
        assertEquals(ResultType.INVALID, result.getType());
    }
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "notAnEmail", "@nnt.org", "email@.org", "email@test."})
    void shouldNotAddInvalidEmail(String email) {
        // Arrange
        UserProfile userProfile = makeUserProfile();
        userProfile.setEmail(email);

        // Act
        Result<UserProfile> result = userProfileService.add(userProfile);

        // Assert
        assertNotNull(result);
        assertNull(result.getPayload());
        assertEquals(ResultType.INVALID, result.getType());
    }
    @Test
    void shouldNotAddDuplicateEmail() {
        // Arrange
        UserProfile userProfile = makeUserProfile();

        when(userProfileRepository.findByEmail(userProfile.getEmail())).thenReturn(userProfile);

        // Act
        Result<UserProfile> result = userProfileService.add(userProfile);

        // Assert
        assertNotNull(result);
        assertNull(result.getPayload());
        assertEquals(ResultType.INVALID, result.getType());
    }
    @ParameterizedTest
    @ValueSource(ints = {13, 11, 5, 1, 0, -1, -2, -10})
    void shouldNotAddInvalidDob(int age) {
        // Arrange
        UserProfile userProfile = makeUserProfile();
        userProfile.setDob(LocalDate.now().minusYears(age).plusDays(1));

        // Act
        Result<UserProfile> result = userProfileService.add(userProfile);

        // Assert
        assertNotNull(result);
        assertNull(result.getPayload());
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldUpdate() {
        // Arrange
        UserProfile userProfile = makeUserProfile();

        when(userProfileRepository.update(userProfile)).thenReturn(true);

        // Act
        Result<UserProfile> result = userProfileService.update(userProfile);

        // Assert
        assertNotNull(result);
        assertNull(result.getPayload());
        assertEquals(ResultType.SUCCESS, result.getType());
    }
    @Test
    void shouldNotUpdateNullUserProfile() {
        // Arrange

        // Act
        Result<UserProfile> result = userProfileService.update(null);

        // Assert
        assertNotNull(result);
        assertNull(result.getPayload());
        assertEquals(ResultType.INVALID, result.getType());
    }
    @ParameterizedTest
    @MethodSource("invalidFirstLastName")
    void shouldNotUpdateInvalidFirstLastName(String firstName, String lastName) {
        // Arrange
        UserProfile userProfile = makeUserProfile();
        userProfile.setFirstName(firstName);
        userProfile.setLastName(lastName);

        // Act
        Result<UserProfile> result = userProfileService.update(userProfile);

        // Assert
        assertNotNull(result);
        assertNull(result.getPayload());
        assertEquals(ResultType.INVALID, result.getType());
    }
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "notAnEmail", "@nnt.org", "email@.org", "email@test."})
    void shouldNotUpdateInvalidEmail(String email) {
        // Arrange
        UserProfile userProfile = makeUserProfile();
        userProfile.setEmail(email);

        // Act
        Result<UserProfile> result = userProfileService.update(userProfile);

        // Assert
        assertNotNull(result);
        assertNull(result.getPayload());
        assertEquals(ResultType.INVALID, result.getType());
    }
    @Test
    void shouldNotUpdateDuplicateEmail() {
        // Arrange
        UserProfile userProfile = makeUserProfile();

        when(userProfileRepository.findByEmail(userProfile.getEmail())).thenReturn(userProfile);

        // Act
        Result<UserProfile> result = userProfileService.update(userProfile);

        // Assert
        assertNotNull(result);
        assertNull(result.getPayload());
        assertEquals(ResultType.INVALID, result.getType());
    }
    @ParameterizedTest
    @ValueSource(ints = {13, 11, 5, 1, 0, -1, -2, -10})
    void shouldNotUpdateInvalidDob(int age) {
        // Arrange
        UserProfile userProfile = makeUserProfile();
        userProfile.setDob(LocalDate.now().minusYears(age).plusDays(1));

        // Act
        Result<UserProfile> result = userProfileService.update(userProfile);

        // Assert
        assertNotNull(result);
        assertNull(result.getPayload());
        assertEquals(ResultType.INVALID, result.getType());
    }

    private UserProfile makeUserProfile() {
        return new UserProfile(
                0,
                "first",
                "last",
                "email@gmail.com",
                LocalDate.now().minusYears(15),
                1
        );
    }

    private static Stream<Arguments> invalidFirstLastName() {
        return Stream.of(
                Arguments.of("", "Smith"),
                Arguments.of("  ", "Smith"),
                Arguments.of("  ", " "),
                Arguments.of("", ""),
                Arguments.of("John", " "),
                Arguments.of("John", "")
        );
    }
}
