package learn.noodemy.data.mappers;

import learn.noodemy.model.Comment;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommentWithUsernameMapper implements RowMapper<Comment> {
    @Override
    public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
        Comment comment = new Comment();

        comment.setCommentId(rs.getInt("comment_id"));
        comment.setComment(rs.getString("comment"));
        if (rs.getDate("created_at") != null) {
            comment.setCreatedAt(rs.getDate("created_at").toLocalDate());
        }
        comment.setEnrollmentId(rs.getInt("enrollment_id"));

        if (rs.getString("username") != null) {
            comment.setPostedBy(rs.getString("username"));
        }
        return comment;
    }
}
