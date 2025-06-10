package learn.noodemy.model;

import java.util.ArrayList;
import java.util.List;

public class Category {

    private int categoryId;
    private String categoryName;
    private String categoryDescription;
    private String categoryCode;
    private List<Course> courseList = new ArrayList<>();

    public Category() {}
    public Category(int categoryId, String categoryName, String categoryDescription, String categoryCode) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
        this.categoryCode = categoryCode;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public List<Course> getCourseList() {
        return new ArrayList<>(courseList);
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }
}
