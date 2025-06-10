package learn.noodemy.data;

import learn.noodemy.data.mappers.CourseMapper;
import learn.noodemy.data.mappers.UserProfileAppUserMapper;
import learn.noodemy.data.mappers.UserProfileMapper;
import learn.noodemy.model.Course;
import learn.noodemy.model.EnrollmentUserProfile;
import learn.noodemy.model.UserProfile;
import learn.noodemy.model.authentication.UserProfileAppUser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserProfileJdbcTemplateRepository implements UserProfileRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserProfileJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<UserProfile> findAll() {

        final String sql =
            "SELECT user_id, first_name, last_name, dob, email, app_user_id " +
            "FROM user_profile " +
            "LIMIT 100";

        return jdbcTemplate.query(sql, new UserProfileMapper());
    }

    @Override
    public List<UserProfileAppUser> findAllFullProfileByUserRole() {
        final String sql =
                "SELECT " +
                        "up.user_id, up.first_name, up.last_name, up.dob, up.email, " +
                        "au.app_user_id, au.username, au.disabled " +
                "FROM user_profile up " +
                "INNER JOIN app_user au ON up.app_user_id = au.app_user_id " +
                "WHERE au.role_id = 2 " +
                "LIMIT 100";

        return jdbcTemplate.query(sql, new UserProfileAppUserMapper());
    }

    @Override
    public UserProfile findByUsername(String username) {
        final String sql =
                "SELECT up.user_id, up.first_name, up.last_name, up.dob, up.email, up.app_user_id " +
                "FROM user_profile up " +
                "INNER JOIN app_user au ON up.app_user_id = au.app_user_id " +
                "WHERE au.username = ?;";

        UserProfile userProfile = jdbcTemplate.query(sql, new UserProfileMapper(), username)
                .stream()
                .findAny().orElse(null);

        if (userProfile != null) {
            addEnrollmentUserProfile(userProfile);
        }

        return userProfile;
    }

    @Override
    public UserProfile findByEmail(String email) {
        final String sql =
            "SELECT user_id, first_name, last_name, dob, email, app_user_id " +
            "FROM user_profile " +
            "WHERE email = ?;";

        UserProfile userProfile = jdbcTemplate.query(sql, new UserProfileMapper(), email)
                .stream()
                .findAny().orElse(null);

        if (userProfile != null) {
            addEnrollmentUserProfile(userProfile);
        }

        return userProfile;
    }


    @Override
    public UserProfile findById(int userId) {

        final String sql =
            "SELECT user_id, first_name, last_name, dob, email, app_user_id " +
            "FROM user_profile " +
            "WHERE user_id = ?;";

        UserProfile userProfile = jdbcTemplate.query(sql, new UserProfileMapper(), userId)
                .stream()
                .findAny().orElse(null);

        if (userProfile != null) {
            addEnrollmentUserProfile(userProfile);
        }

        return userProfile;
    }


    @Override
    public UserProfile add(UserProfile userProfile) {

        final String sql =
                "INSERT INTO user_profile(first_name, last_name, dob, email, app_user_id) " +
                "VALUES (?, ?, ?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, userProfile.getFirstName());
            ps.setString(2, userProfile.getLastName());
            ps.setDate(3, userProfile.getDob() == null ? null : Date.valueOf(userProfile.getDob()));
            ps.setString(4, userProfile.getEmail());
            ps.setInt(5, userProfile.getAppUserId());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        userProfile.setUserId(keyHolder.getKey().intValue());
        return userProfile;
    }

    @Override
    public boolean update(UserProfile userProfile) {
        final String sql =
            "UPDATE user_profile " +
            "SET " +
                "first_name = ?, " +
                "last_name = ?, " +
                "dob = ?, " +
                "email = ? " +
            "WHERE user_id = ?;";

        return jdbcTemplate.update(
                sql,
                userProfile.getFirstName(),
                userProfile.getLastName(),
                userProfile.getDob(),
                userProfile.getEmail(),
                userProfile.getUserId()
        ) > 0;
    }

    @Override
    public boolean deleteById(int userId) {
        return jdbcTemplate.update("DELETE FROM user_profile WHERE user_id = ?;", userId) > 0;
    }

    private void addEnrollmentUserProfile(UserProfile userProfile) {
        final String sql = "SELECT c.course_id, c.course_name, c.course_description, c.price, c.estimate_duration, c.category_id " +
                "FROM enrollment e " +
                "INNER JOIN course c ON c.course_id = e.course_id " +
                "INNER JOIN user_profile up ON up.user_id = e.user_id " +
                "WHERE e.user_id = ?;";

        final String sql2 = "SELECT enrollment_id, course_id FROM enrollment WHERE user_id =?;";

        List<Course> courses = jdbcTemplate.query(sql, new CourseMapper(), userProfile.getUserId());
        List<EnrollmentUserProfile> enrollmentUserProfiles = new ArrayList<>();

        // Hot fixes to include enrollment id
        // Key: Value -> courseId, enrollmentId
        List<SimpleEntry<Integer, Integer>> enrollment = jdbcTemplate.query(sql2, (rs, rowNum) -> {
            return new SimpleEntry<Integer, Integer>(rs.getInt("course_id"), rs.getInt("enrollment_id"));
        }, userProfile.getUserId());

        for (Course course : courses) {
            EnrollmentUserProfile enrollmentUserProfile = new EnrollmentUserProfile(userProfile.getUserId(), course);

            SimpleEntry<Integer, Integer> enrollmentCourse = enrollment.stream()
                    .filter(e -> e.getKey() == course.getCourseId())
                            .findFirst().orElse(null);
            assert enrollmentCourse != null;
            int enrollmentId = enrollmentCourse.getValue();
            enrollmentUserProfile.setEnrollmentId(enrollmentId);

            enrollmentUserProfiles.add(enrollmentUserProfile);
        }

        userProfile.setEnrollmentList(enrollmentUserProfiles);
    }
}
