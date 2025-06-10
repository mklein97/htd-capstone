package learn.noodemy.data;

import learn.noodemy.model.Comment;

import java.util.List;

public interface CommentRepository {

    List<Comment> findAll();
    List<Comment> findByCourseId(int courseId);
    List<Comment> findByUserId(int userId);
    Comment findById(int id);
    Comment add(Comment comment);
    boolean update(Comment comment);
    boolean deleteById(int id);
}
