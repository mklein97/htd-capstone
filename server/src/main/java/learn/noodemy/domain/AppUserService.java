package learn.noodemy.domain;

import learn.noodemy.data.AppUserRepository;
import learn.noodemy.model.AppUser;
import org.springframework.stereotype.Service;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;

    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public AppUser findById(int id) {
        return appUserRepository.findById(id);
    }

    public AppUser findByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    public Result<AppUser> add(AppUser appUser) {
        Result<AppUser> result = validateAppUser(appUser);
        if (!result.isSuccess()) {
            return result;
        }

        appUser = appUserRepository.add(appUser);
        result.setPayload(appUser);
        return result;
    }

    public Result<AppUser> deleteById(int appUserId) {
        Result<AppUser> result = new Result<>();

        if (!appUserRepository.deleteById(appUserId)) {
            result.addMessage("User is already disabled.", ResultType.INVALID);
            return result;
        }

        return result;
    }

    public Result<AppUser> enableById(int appUserId) {
        Result<AppUser> result = new Result<>();

        if (!appUserRepository.enableById(appUserId)) {
            result.addMessage("User is already enabled.", ResultType.INVALID);
            return result;
        }

        return result;
    }

    private Result<AppUser> validateAppUser(AppUser appUser) {
        Result<AppUser> result = new Result<>();

        if (appUser == null) {
            result.addMessage("AppUser cannot be null", ResultType.INVALID);
            return result;
        }

        if (Validations.isNullOrBlank(appUser.getUsername())
                || appUserRepository.findByUsername(appUser.getUsername()) != null) {
            result.addMessage("Username already exists", ResultType.INVALID);
        }

        if (Validations.isNullOrBlank(appUser.getPassword())
                || appUser.getPassword().length() < 7
                || appUser.getPassword().length() > 30) {
            result.addMessage("Password must be between 7 and 30 characters", ResultType.INVALID);
        }

        return result;
    }
}
