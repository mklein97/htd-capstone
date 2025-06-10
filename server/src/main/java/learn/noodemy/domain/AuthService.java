package learn.noodemy.domain;

import learn.noodemy.data.AppUserRepository;
import learn.noodemy.data.UserProfileRepository;
import learn.noodemy.model.AppUser;
import learn.noodemy.model.UserProfile;
import learn.noodemy.model.authentication.LoginRequestDTO;
import learn.noodemy.model.authentication.LoginResponseDTO;
import learn.noodemy.model.authentication.RegisterRequestDTO;
import learn.noodemy.model.authentication.RegisterResponseDTO;
import learn.noodemy.security.JwtConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;

@Service
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final UserProfileRepository userProfileRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtConverter jwtConverter;

    public AuthService(AppUserRepository appUserRepository,
                       UserProfileRepository userProfileRepository,
                       AuthenticationManager authenticationManager,
                       JwtConverter jwtConverter) {
        this.appUserRepository = appUserRepository;
        this.userProfileRepository = userProfileRepository;
        this.authenticationManager = authenticationManager;
        this.jwtConverter = jwtConverter;
    }

    public Result<LoginResponseDTO> login(LoginRequestDTO loginRequestDTO) {

        // authenticate with Spring Security
        // if success, generate and return jwt token
        // else, return result with empty payload

        Result<LoginResponseDTO> result = new Result<>();

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getUsername(),
                        loginRequestDTO.getPassword());

        try {
            Authentication authentication = authenticationManager.authenticate(authToken);

            if (authentication.isAuthenticated()) {

                UserProfile userProfile = userProfileRepository.findByUsername(loginRequestDTO.getUsername());
                User authenticatedUser = (User) authentication.getPrincipal();
                String jwtToken = jwtConverter.getTokenFromUser(authenticatedUser);

                result.setPayload(
                    new LoginResponseDTO(
                        jwtToken,
                        authenticatedUser.getUsername(),
                        userProfile.getUserId(),
                        authenticatedUser.getAuthorities().stream().toList().get(0).toString()));
            }

        } catch (AuthenticationException ex) {
            if (ex.getCause() instanceof DisabledException) {
                result.addMessage(ex.getMessage(), ResultType.INVALID);
            } else {
                result.addMessage("Your username and password do not match. " +
                        "Please try again.", ResultType.INVALID);
            }
            System.out.println(ex);
        }

        return result;
    }

    @Transactional
    public Result<RegisterResponseDTO> register(RegisterRequestDTO registerRequestDTO) {

        // validate request
            // if any errors, return

        // create an AppUser
        // create a UserProfile referencing the new AppUser

        // if all success,
            // create payload and assign to result
            // return result
        // else, return errors

        Result<RegisterResponseDTO> result = validateRegisterRequest(registerRequestDTO);
        if (!result.isSuccess()) return result;

        AppUser newAppUser = new AppUser(
                0,
                registerRequestDTO.getUsername(),
                registerRequestDTO.getPassword(),
                false,
                new ArrayList<>()
        );
        newAppUser.setRoleId(registerRequestDTO.getRoleId());
        newAppUser = appUserRepository.add(newAppUser);

        UserProfile newUserProfile = userProfileRepository.add(
                new UserProfile(
                        0,
                        registerRequestDTO.getFirstName(),
                        registerRequestDTO.getLastName(),
                        registerRequestDTO.getEmail(),
                        registerRequestDTO.getDob(),
                        newAppUser.getAppUserId()
                )
        );

        RegisterResponseDTO responseDTO = new RegisterResponseDTO(
                newUserProfile.getUserId(),
                newAppUser.getUsername()
        );
        result.setPayload(responseDTO);
        return result;
    }

    private Result<RegisterResponseDTO> validateRegisterRequest(RegisterRequestDTO registerRequestDTO) {
        Result<RegisterResponseDTO> result = new Result<>();

        if (registerRequestDTO == null) {
            result.addMessage("Register request cannot be null", ResultType.INVALID);
            return result;
        }

        if (Validations.isNullOrBlank(registerRequestDTO.getUsername())
                || appUserRepository.findByUsername(registerRequestDTO.getUsername()) != null) {
            result.addMessage("Username already exists", ResultType.INVALID);
        }

        if (Validations.isNullOrBlank(registerRequestDTO.getPassword())
                || registerRequestDTO.getPassword().length() < 7
                || registerRequestDTO.getPassword().length() > 30) {
            result.addMessage("Password must be between 7 and 30 characters", ResultType.INVALID);
        }

        if (Validations.isNullOrBlank(registerRequestDTO.getFirstName())
                || Validations.isNullOrBlank(registerRequestDTO.getLastName())) {
            result.addMessage("First name or last name cannot be null or blank", ResultType.INVALID);
        }

        if (!Validations.validEmail(registerRequestDTO.getEmail())) {
            result.addMessage("Invalid email", ResultType.INVALID);
        } else if (userProfileRepository.findByEmail(registerRequestDTO.getEmail()) != null) {
            result.addMessage("Email already exists", ResultType.INVALID);
        }

        if (registerRequestDTO.getDob() == null || registerRequestDTO.getDob().isAfter(LocalDate.now().minusYears(13))) {
            result.addMessage("Must be at least 13 years old", ResultType.INVALID);
        }

        return result;
    }
}
