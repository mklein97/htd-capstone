package learn.noodemy.data;

import learn.noodemy.model.Course;

import java.util.List;

public interface CourseRepository {

    List<Course> findAll();

    List<Course> findAllByUserId(int userId);

    Course findById(int courseId);

    Course add(Course course);

    boolean update(Course course);

    boolean delete(int courseId);
}
