package learn.noodemy.data;

import learn.noodemy.model.EnrollmentCourse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EnrollmentJdbcTemplateRepository implements EnrollmentRepository {

    private JdbcTemplate jdbcTemplate;

    public EnrollmentJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean existsById(int enrollmentId) {
        final String sql =
            "SELECT COUNT(*) " +
            "FROM enrollment " +
            "WHERE enrollment_id = ?;";

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, enrollmentId);
        return count != null && count > 0;
    }

    @Override
    public boolean add(EnrollmentCourse enrollment) {

        final String sql =
                "INSERT INTO enrollment (user_id, course_id) " +
                "VALUES (?, ?);";

        return jdbcTemplate.update(sql,
                enrollment.getUserProfile().getUserId(),
                enrollment.getCourseId()) > 0;
    }

    @Override
    public boolean deleteById(int enrollmentId) {
        final String sql =
                "DELETE FROM enrollment " +
                "WHERE enrollment_id = ?;";

        return jdbcTemplate.update(sql, enrollmentId) > 0;
    }
}
