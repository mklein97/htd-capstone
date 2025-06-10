package learn.noodemy.controllers;

import learn.noodemy.domain.AppUserService;
import learn.noodemy.domain.Result;
import learn.noodemy.model.AppUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasRole('ADMIN')")
@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/app-users")
public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @DeleteMapping("/{appUserId}")
    public ResponseEntity<Object> disableAppUser(@PathVariable int appUserId) {
        Result<AppUser> result = appUserService.deleteById(appUserId);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }

    @PutMapping("/{appUserId}")
    public ResponseEntity<Object> enableAppUser(@PathVariable int appUserId) {
        Result<AppUser> result = appUserService.enableById(appUserId);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }
}
