package learn.noodemy.model;

import java.math.BigDecimal;
import java.util.List;

public class Course {

    private int courseId;
    private String courseName;
    private String courseDescription;
    private BigDecimal price;
    private int estimateDuration;
    private Category category;

    private List<EnrollmentCourse> enrollmentList;

    public Course() {}
    public Course(int courseId, String courseName, String courseDescription, BigDecimal price, int estimateDuration, Category category) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
        this.price = price;
        this.estimateDuration = estimateDuration;
        this.category = category;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getEstimateDuration() {
        return estimateDuration;
    }

    public void setEstimateDuration(int estimateDuration) {
        this.estimateDuration = estimateDuration;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<EnrollmentCourse> getEnrollmentList() {
        return enrollmentList;
    }

    public void setEnrollmentList(List<EnrollmentCourse> enrollmentList) {
        this.enrollmentList = enrollmentList;
    }
}
