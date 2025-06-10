package learn.noodemy.data;

import learn.noodemy.data.mappers.CommentMapper;
import learn.noodemy.data.mappers.CommentWithUsernameMapper;
import learn.noodemy.model.Comment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class CommentJdbcTemplateRepository implements CommentRepository {

    private final JdbcTemplate jdbcTemplate;

    public CommentJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Comment> findAll() {
        final String sql =
                "SELECT comment_id, comment, created_at, enrollment_id " +
                "FROM comment " +
                "LIMIT 100;";

        return jdbcTemplate.query(sql, new CommentMapper());
    }

    @Override
    public List<Comment> findByCourseId(int courseId) {
        final String sql =
            "SELECT cm.comment_id, cm.comment, cm.created_at, cm.enrollment_id, au.username " +
            "FROM comment cm " +
            "INNER JOIN enrollment e ON cm.enrollment_id = e.enrollment_id " +
            "INNER JOIN course c ON c.course_id = e.course_id " +
            "INNER JOIN user_profile up ON e.user_id = up.user_id " +
            "INNER JOIN app_user au ON au.app_user_id = up.app_user_id " +
            "WHERE c.course_id = ? " +
            "LIMIT 100;";


        return jdbcTemplate.query(sql, new CommentWithUsernameMapper(), courseId);
    }

    @Override
    public List<Comment> findByUserId(int userId) {
        final String sql =
            "SELECT cm.comment_id, cm.comment, cm.created_at, cm.enrollment_id " +
            "FROM comment cm " +
            "INNER JOIN enrollment e ON cm.enrollment_id = e.enrollment_id " +
            "WHERE e.user_id = ? " +
            "LIMIT 100;";

        return jdbcTemplate.query(sql, new CommentMapper(), userId);
    }

    @Override
    public Comment findById(int id) {
        final String sql =
                "SELECT comment_id, comment, created_at, enrollment_id " +
                "FROM comment " +
                "WHERE comment_id = ?;";

        return jdbcTemplate.queryForObject(sql, new CommentMapper(), id);
    }

    @Override
    public Comment add(Comment comment) {
        final String sql =
                "INSERT INTO comment(comment, created_at, enrollment_id) " +
                "VALUES (?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, comment.getComment());
                ps.setDate(2, Date.valueOf(comment.getCreatedAt()));
                ps.setInt(3, comment.getEnrollmentId());
                return ps;
            },
            keyHolder
        );

        if (rowsAffected <= 0) return null;

        comment.setCommentId(keyHolder.getKey().intValue());
        return comment;
    }

    @Override
    public boolean update(Comment comment) {
        final String sql =
                "UPDATE comment " +
                "SET " +
                    "comment = ? " +
                "WHERE comment_id = ?;";
        return jdbcTemplate.update(
                sql,
                comment.getComment(),
                comment.getCommentId()) > 0;
    }

    @Override
    public boolean deleteById(int id) {
        final String sql =
                "DELETE FROM comment " +
                "WHERE comment_id = ?;";

        return jdbcTemplate.update(sql, id) > 0;
    }
}
