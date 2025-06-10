package learn.noodemy.domain;

import learn.noodemy.data.UserProfileRepository;
import learn.noodemy.model.UserProfile;
import learn.noodemy.model.authentication.UserProfileAppUser;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public List<UserProfile> findAll() {
        return userProfileRepository.findAll();
    }

    public UserProfile findById(int id) {
        return userProfileRepository.findById(id);
    }

    public List<UserProfileAppUser> findAllFullProfileWithUserRole() {
        return userProfileRepository.findAllFullProfileByUserRole();
    }

    public Result<UserProfile> add(UserProfile userProfile) {
        Result<UserProfile> result = validateUserProfile(userProfile);
        if (!result.isSuccess()) {
            return result;
        }

        userProfile = userProfileRepository.add(userProfile);
        result.setPayload(userProfile);
        return result;
    }

    public Result<UserProfile> update(UserProfile userProfile) {
        Result<UserProfile> result = validateUserProfile(userProfile);

        if (!result.isSuccess()) {
            return result;
        }

        if (!userProfileRepository.update(userProfile)) {
            result.addMessage("Could not locate the userProfile to be updated.", ResultType.NOT_FOUND);
            return result;
        }

        return result;
    }

    public Result<UserProfile> validateUserProfile(UserProfile userProfile) {
        Result<UserProfile> result = new Result<>();

        if (userProfile == null) {
            result.addMessage("UserProfile cannot  be null", ResultType.INVALID);
            return result;
        }

        if (Validations.isNullOrBlank(userProfile.getFirstName())
                || Validations.isNullOrBlank(userProfile.getLastName())) {
            result.addMessage("First name or last name cannot be null or blank", ResultType.INVALID);
        }

        if (!Validations.validEmail(userProfile.getEmail())) {
            result.addMessage("Invalid email", ResultType.INVALID);
        } else if (userProfile.getUserId() == 0 && userProfileRepository.findByEmail(userProfile.getEmail()) != null) {
            result.addMessage("Email already exists", ResultType.INVALID);
        }

        if (userProfile.getDob() == null || userProfile.getDob().isAfter(LocalDate.now().minusYears(13))) {
            result.addMessage("Must be at least 13 years old", ResultType.INVALID);
        }

        return result;
    }
}
