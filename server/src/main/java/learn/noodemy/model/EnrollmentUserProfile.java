package learn.noodemy.model;

public class EnrollmentUserProfile {

    private int userId;

    private Course course;

    private int enrollmentId;

    public EnrollmentUserProfile(int userId, Course course) {
        this.userId = userId;
        this.course = course;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public int getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }
}
