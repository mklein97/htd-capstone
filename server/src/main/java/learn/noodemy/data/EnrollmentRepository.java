package learn.noodemy.data;

import learn.noodemy.model.EnrollmentCourse;

public interface EnrollmentRepository {

    boolean existsById(int enrollmentId);
    boolean add(EnrollmentCourse enrollment);
    boolean deleteById(int enrollmentId);
}
