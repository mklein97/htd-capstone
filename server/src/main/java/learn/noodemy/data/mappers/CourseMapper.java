package learn.noodemy.data.mappers;

import learn.noodemy.model.Category;
import learn.noodemy.model.Course;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CourseMapper implements RowMapper<Course> {

    @Override
    public Course mapRow(ResultSet rs, int rowNum) throws SQLException {
        Course course = new Course();

        course.setCourseId(rs.getInt("course_id"));
        course.setCourseName(rs.getString("course_name"));
        course.setCourseDescription(rs.getString("course_description"));
        course.setPrice(rs.getBigDecimal("price"));
        course.setEstimateDuration(rs.getInt("estimate_duration"));

        Category category = new Category();
        category.setCategoryId(rs.getInt("category_id"));

        // Handles when we're not query for category details.
        try {
            category.setCategoryName(rs.getString("category_name"));
            category.setCategoryDescription(rs.getString("category_description"));
            category.setCategoryCode(rs.getString("category_code"));
        } catch (SQLException e) {
            // Do nothing.
        }

        course.setCategory(category);
        return course;
    }
}
