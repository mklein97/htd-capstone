package learn.noodemy.data;

import learn.noodemy.data.mappers.CourseMapper;
import learn.noodemy.data.mappers.UserProfileMapper;
import learn.noodemy.model.Course;
import learn.noodemy.model.EnrollmentCourse;
import learn.noodemy.model.UserProfile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class CourseJdbcTemplateRepository implements CourseRepository {

    private final JdbcTemplate jdbcTemplate;

    private final String CourseCategorySQL =
            "SELECT course_id, course_name, course_description, price, estimate_duration, cou.category_id, " +
                    "category_name, category_description, category_code " +
                    "FROM course cou " +
                    "INNER JOIN category cat ON cat.category_id = cou.category_id;";

    public CourseJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Course> findAll() {
        return jdbcTemplate.query(CourseCategorySQL, new CourseMapper());
    }

    @Override
    public Course findById(int courseId) {
        final String sql = CourseCategorySQL.replace(";", "") + " WHERE cou.course_id = ?;";

        Course course = jdbcTemplate.query(sql, new CourseMapper(), courseId).stream()
                .findFirst().orElse(null);

        if (course != null) {
            addEnrollmentCourseList(course);
        }

        return course;
    }

    @Override
    public List<Course> findAllByUserId(int userId) {
        final String sql =
                "SELECT " +
                        "cou.course_id, " +
                        "cou.course_name, " +
                        "cou.course_description, " +
                        "cou.price, " +
                        "cou.estimate_duration, " +
                        "cou.category_id " +
                "FROM course cou " +
                "INNER JOIN enrollment e ON cou.course_id = e.course_id " +
                "WHERE e.user_id = ?;";

        return jdbcTemplate.query(sql, new CourseMapper(), userId);
    }

    @Override
    public Course add(Course course) {
        final String sql =
                "INSERT INTO course(course_name, course_description, price, estimate_duration, category_id) " +
                        "VALUES (?, ?, ?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, course.getCourseName());
            ps.setString(2, course.getCourseDescription());
            ps.setBigDecimal(3, course.getPrice());
            ps.setInt(4, course.getEstimateDuration());
            ps.setInt(5, course.getCategory().getCategoryId());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        course.setCourseId(Objects.requireNonNull(keyHolder.getKey()).intValue());

        return course;
    }

    @Override
    public boolean update(Course course) {
        final String sql =
                "UPDATE course " +
                        "SET " +
                        "course_name = ?, " +
                        "course_description = ?, " +
                        "price = ?, " +
                        "estimate_duration = ?, " +
                        "category_id = ? " +
                        "WHERE course_id = ?";

        return jdbcTemplate.update(
                sql,
                course.getCourseName(),
                course.getCourseDescription(),
                course.getPrice(),
                course.getEstimateDuration(),
                course.getCategory().getCategoryId(),
                course.getCourseId()
        ) > 0;
    }

    @Override
    public boolean delete(int courseId) {
        return jdbcTemplate.update("DELETE FROM course WHERE course_id = ?;", courseId) > 0;
    }

    private void addEnrollmentCourseList(Course course) {
        final String sql =
                "SELECT e.user_id, u.first_name, u.last_name, u.email, u.dob, u.app_user_id " +
                "FROM enrollment e " +
                "INNER JOIN course c on c.course_id = e.course_id " +
                "INNER JOIN user_profile u on u.user_id = e.user_id " +
                "WHERE e.course_id = ?;";

        List<UserProfile> userProfiles = jdbcTemplate.query(sql, new UserProfileMapper(), course.getCourseId());
        List<EnrollmentCourse> enrollmentCourses = new ArrayList<>();

        for (UserProfile userProfile : userProfiles) {
            enrollmentCourses.add(new EnrollmentCourse(course.getCourseId(), userProfile));
        }
        course.setEnrollmentList(enrollmentCourses);
    }
}
