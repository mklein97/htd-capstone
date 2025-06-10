package learn.noodemy.domain;

import learn.noodemy.data.AppUserRepository;
import learn.noodemy.model.AppUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AppUserServiceTest {

    @Autowired
    private AppUserService appUserService;
    @MockitoBean
    private AppUserRepository appUserRepository;

    @Test
    void shouldAdd(){
        // Arrange
        AppUser appUser = new AppUser(
                0,
                "test",
                "password",
                false,
                new ArrayList<>()
        );

        when(appUserRepository.add(appUser)).thenReturn(appUser);

        // Act
        Result<AppUser> actual = appUserService.add(appUser);

        // Assert
        assertNotNull(actual);
        assertNotNull(actual.getPayload());
        assertEquals(ResultType.SUCCESS, actual.getType());
    }
    @ParameterizedTest
    @ValueSource(strings = {"  ", ""})
    void shouldNotAddInvalidUsername(String username){
        // Arrange
        AppUser appUser = makeAppUser();
        appUser.setUsername(username);

        // Act
        Result<AppUser> actual = appUserService.add(appUser);

        // Assert
        assertNotNull(actual);
        assertNull(actual.getPayload());
        assertEquals(ResultType.INVALID, actual.getType());
    }
    @Test
    void shouldNotAddDuplicateUsername(){
        // Arrange
        AppUser appUser = makeAppUser();

        when(appUserRepository.findByUsername(appUser.getUsername())).thenReturn(appUser);

        // Act
        Result<AppUser> actual = appUserService.add(appUser);

        // Assert
        assertNotNull(actual);
        assertNull(actual.getPayload());
        assertEquals(ResultType.INVALID, actual.getType());
    }
    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "  ",
            "abcde",
            "longerThan30Chars_eabcdeabcdeabcdeabcdeabcde"})
    void shouldNotAddInvalidPassword(String password){
        // Arrange
        AppUser appUser = makeAppUser();
        appUser.setPassword(password);

        // Act
        Result<AppUser> actual = appUserService.add(appUser);

        // Assert
        assertNotNull(actual);
        assertNull(actual.getPayload());
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void deleteById_ShouldUpdateDisabledToTrue() {
        // Arrange
        int id = 2;

        when(appUserRepository.deleteById(id)).thenReturn(true);

        // Act
        Result<AppUser> actual = appUserService.deleteById(id);

        // Assert
        assertNotNull(actual);
        assertNull(actual.getPayload());
        assertEquals(ResultType.SUCCESS, actual.getType());
    }
    @Test
    void deleteById_ShouldReturnErrorsIfAppUserDisabled() {
        // Arrange
        int id = 2;

        when(appUserRepository.deleteById(id)).thenReturn(false);

        // Act
        Result<AppUser> actual = appUserService.deleteById(id);

        // Assert
        assertNotNull(actual);
        assertNull(actual.getPayload());
        assertEquals(ResultType.INVALID, actual.getType());
    }

    private AppUser makeAppUser() {
        return new AppUser(
                0,
                "test",
                "password",
                false,
                new ArrayList<>()
        );
    }
}
