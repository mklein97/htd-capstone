package learn.noodemy.controllers;

import learn.noodemy.domain.AppUserService;
import learn.noodemy.domain.Result;
import learn.noodemy.domain.UserProfileService;
import learn.noodemy.model.AppUser;
import learn.noodemy.model.UserProfile;
import learn.noodemy.model.authentication.UserProfileAppUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/users")
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final AppUserService appUserService;

    public UserProfileController(UserProfileService userProfileService,
                                 AppUserService appUserService) {
        this.userProfileService = userProfileService;
        this.appUserService = appUserService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UserProfile> getUserProfiles() {
        return userProfileService.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/full")
    public List<UserProfileAppUser> getFullUserProfileOfUser() {
        return userProfileService.findAllFullProfileWithUserRole();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfile> getUserProfileById(@PathVariable int id) {
        UserProfile userProfile = userProfileService.findById(id);

        if (userProfile == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (!isSameProfileWithAuthenticatedUser(userProfile.getAppUserId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(userProfile);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUserProfile(@PathVariable int id,
                                                     @RequestBody UserProfile userProfile) {

        if (id != userProfile.getUserId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        if (!isSameProfileWithAuthenticatedUser(id)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Result<UserProfile> result = userProfileService.update(userProfile);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }

    private boolean isSameProfileWithAuthenticatedUser(int appUserId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;

        String authenticatedUsername = auth.getName();
        AppUser appUserById = appUserService.findById(appUserId);

        if (appUserById == null || !appUserById.getUsername().equals(authenticatedUsername)) return false;

        return true;
    }
}
