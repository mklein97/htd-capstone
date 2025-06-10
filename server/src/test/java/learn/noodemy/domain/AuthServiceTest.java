package learn.noodemy.domain;

import learn.noodemy.data.AppUserRepository;
import learn.noodemy.data.UserProfileRepository;
import learn.noodemy.model.AppUser;
import learn.noodemy.model.UserProfile;
import learn.noodemy.model.authentication.LoginRequestDTO;
import learn.noodemy.model.authentication.LoginResponseDTO;
import learn.noodemy.model.authentication.RegisterRequestDTO;
import learn.noodemy.model.authentication.RegisterResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthServiceTest {

    @Autowired
    private AuthService authService;
    @MockitoBean
    private AppUserRepository appUserRepository;
    @MockitoBean
    private UserProfileRepository profileRepository;

    @Test
    void shouldRegister() {
        // Arrange
        RegisterRequestDTO registerRequestDTO = makeRegisterRequestDTO();
        AppUser appUser = makeAppUser();
        UserProfile userProfile = makeUserProfile();

        when(appUserRepository.findByUsername(appUser.getUsername())).thenReturn(null);
        when(profileRepository.findByEmail(userProfile.getEmail())).thenReturn(null);
        when(appUserRepository.add(any())).thenReturn(appUser);
        when(profileRepository.add(any())).thenReturn(userProfile);

        // Act
        Result<RegisterResponseDTO> result = authService.register(registerRequestDTO);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getPayload());
        assertEquals(ResultType.SUCCESS, result.getType());
    }
    @Test
    void shouldNotRegisterNullRequest() {
        // Arrange

        // Act
        Result<RegisterResponseDTO> result = authService.register(null);

        // Assert
        assertNotNull(result);
        assertNull(result.getPayload());
        assertEquals(ResultType.INVALID, result.getType());
    }
    @ParameterizedTest
    @ValueSource(strings = {"  ", ""})
    void shouldNotRegisterInvalidUsername(String username) {
        // Arrange
        RegisterRequestDTO registerRequestDTO = makeRegisterRequestDTO();
        registerRequestDTO.setUsername(username);

        // Act
        Result<RegisterResponseDTO> result = authService.register(registerRequestDTO);

        // Assert
        assertNotNull(result);
        assertNull(result.getPayload());
        assertEquals(ResultType.INVALID, result.getType());
    }
    @Test
    void shouldNotRegisterDuplicateUsername() {
        // Arrange
        RegisterRequestDTO registerRequestDTO = makeRegisterRequestDTO();
        when(appUserRepository.findByUsername(registerRequestDTO.getUsername()))
                .thenReturn(makeAppUser());

        // Act
        Result<RegisterResponseDTO> result = authService.register(registerRequestDTO);

        // Assert
        assertNotNull(result);
        assertNull(result.getPayload());
        assertEquals(ResultType.INVALID, result.getType());
    }
    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "  ",
            "abcde",
            "longerThan30Chars_eabcdeabcdeabcdeabcdeabcde"})
    void shouldNotRegisterInvalidPassword(String password) {
        // Arrange
        RegisterRequestDTO registerRequestDTO = makeRegisterRequestDTO();
        registerRequestDTO.setPassword(password);

        // Act
        Result<RegisterResponseDTO> result = authService.register(registerRequestDTO);

        // Assert
        assertNotNull(result);
        assertNull(result.getPayload());
        assertEquals(ResultType.INVALID, result.getType());
    }
    @ParameterizedTest
    @MethodSource("invalidFirstLastName")
    void shouldNotRegisterInvalidFirstLastName(String firstName, String lastName) {
        // Arrange
        RegisterRequestDTO registerRequestDTO = makeRegisterRequestDTO();
        registerRequestDTO.setFirstName(firstName);
        registerRequestDTO.setLastName(lastName);

        when(appUserRepository.findByUsername(registerRequestDTO.getUsername())).thenReturn(null);

        // Act
        Result<RegisterResponseDTO> result = authService.register(registerRequestDTO);

        // Assert
        assertNotNull(result);
        assertNull(result.getPayload());
        assertEquals(ResultType.INVALID, result.getType());
    }
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "notAnEmail", "@nnt.org", "email@.org", "email@test."})
    void shouldNotRegisterInvalidEmail(String email) {
        // Arrange
        RegisterRequestDTO registerRequestDTO = makeRegisterRequestDTO();
        registerRequestDTO.setEmail(email);

        when(appUserRepository.findByUsername(registerRequestDTO.getUsername())).thenReturn(null);

        // Act
        Result<RegisterResponseDTO> result = authService.register(registerRequestDTO);

        // Assert
        assertNotNull(result);
        assertNull(result.getPayload());
        assertEquals(ResultType.INVALID, result.getType());
    }
    @Test
    void shouldNotRegisterDuplicateEmail() {
        // Arrange
        RegisterRequestDTO registerRequestDTO = makeRegisterRequestDTO();

        when(appUserRepository.findByUsername(registerRequestDTO.getUsername())).thenReturn(null);
        when(profileRepository.findByEmail(registerRequestDTO.getEmail()))
                .thenReturn(makeUserProfile());

        // Act
        Result<RegisterResponseDTO> result = authService.register(registerRequestDTO);

        // Assert
        assertNotNull(result);
        assertNull(result.getPayload());
        assertEquals(ResultType.INVALID, result.getType());
    }
    @ParameterizedTest
    @ValueSource(ints = {13, 11, 5, 1, 0, -1, -2, -10})
    void shouldNotRegisterInvalidDob(int age) {
        // Arrange
        RegisterRequestDTO registerRequestDTO = makeRegisterRequestDTO();
        registerRequestDTO.setDob(LocalDate.now().minusYears(age).plusDays(1));

        when(appUserRepository.findByUsername(registerRequestDTO.getUsername())).thenReturn(null);
        when(profileRepository.findByEmail(registerRequestDTO.getEmail())).thenReturn(null);

        // Act
        Result<RegisterResponseDTO> result = authService.register(registerRequestDTO);

        // Assert
        assertNotNull(result);
        assertNull(result.getPayload());
        assertEquals(ResultType.INVALID, result.getType());
    }

    private RegisterRequestDTO makeRegisterRequestDTO() {
        return new RegisterRequestDTO(
                "test",
                "password",
                "first",
                "last",
                "email@gmail.com",
                LocalDate.now().minusYears(13),
                1
        );
    }

    private AppUser makeAppUser() {
        return new AppUser(
                1,
                "test",
                "password",
                false,
                new ArrayList<>()
        );
    }
    private UserProfile makeUserProfile() {
        return new UserProfile(
                1,
                "first",
                "last",
                "email@gmail.com",
                LocalDate.now().minusYears(13),
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
