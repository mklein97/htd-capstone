package learn.noodemy.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserProfile {

    private int userId;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dob;
    private int appUserId;

    private List<EnrollmentUserProfile> enrollmentList = new ArrayList<>();

    public UserProfile() {}
    public UserProfile(int userId, String firstName, String lastName, String email, LocalDate dob, int appUserId) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dob = dob;
        this.appUserId = appUserId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public int getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(int appUserId) {
        this.appUserId = appUserId;
    }

    public List<EnrollmentUserProfile> getEnrollmentList() {
        return new ArrayList<>(enrollmentList);
    }

    public void setEnrollmentList(List<EnrollmentUserProfile> enrollmentList) {
        this.enrollmentList = enrollmentList;
    }
}