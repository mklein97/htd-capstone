package learn.noodemy.model;

public class EnrollmentCourse {

    private int courseId;

    private UserProfile userProfile;

    public EnrollmentCourse(int courseId, UserProfile userProfile) {
        this.courseId = courseId;
        this.userProfile = userProfile;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }
}
