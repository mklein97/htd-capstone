package learn.noodemy.domain;

import learn.noodemy.data.EnrollmentRepository;
import learn.noodemy.data.UserProfileRepository;
import learn.noodemy.model.EnrollmentCourse;
import learn.noodemy.model.EnrollmentUserProfile;
import learn.noodemy.model.UserProfile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService {

    private final EnrollmentRepository repository;
    private final UserProfileRepository userProfileRepository;

    public EnrollmentService(EnrollmentRepository repository, UserProfileRepository userProfileRepository) {
        this.repository = repository;
        this.userProfileRepository = userProfileRepository;
    }

    public Result<Boolean> existsById(int enrollmentId) {
        Result<Boolean> result = new Result<>();
        if (repository.existsById(enrollmentId)) {
            result.setPayload(true);
            return result;
        } else {
            result.addMessage("This enrollment does not exist", ResultType.NOT_FOUND);
            return result;
        }
    }

    public Result<Boolean> add(int courseId, int userId) {
        Result<Boolean> result = new Result<>();

        UserProfile userProfile = userProfileRepository.findById(userId);

        if (isAlreadyEnrolled(courseId, userProfile)) {
            result.addMessage("The user is already enrolled in this course", ResultType.INVALID);
            return result;
        }

        EnrollmentCourse enrollmentCourse = new EnrollmentCourse(courseId, userProfile);

        if (repository.add(enrollmentCourse)) {
            result.setPayload(true);
            return result;
        } else {
            result.addMessage("Failed to enroll.", ResultType.INVALID);
            return result;
        }
    }

    public Result<Boolean> deleteById(int enrollmentId) {
        Result<Boolean> result = new Result<>();
        if (repository.deleteById(enrollmentId)) {
            result.setPayload(true);
            return result;
        } else {
            result.addMessage("Could not find this enrollment id", ResultType.NOT_FOUND);
            return result;
        }
    }

    private boolean isAlreadyEnrolled(int courseId, UserProfile user) {
        List<EnrollmentUserProfile> enrollmentList = user.getEnrollmentList();
        return enrollmentList.stream().anyMatch(up -> up.getCourse().getCourseId() == courseId);
    }
}
