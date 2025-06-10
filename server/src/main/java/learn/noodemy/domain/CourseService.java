package learn.noodemy.domain;

import learn.noodemy.data.CategoryRepository;
import learn.noodemy.data.CourseRepository;
import learn.noodemy.model.Course;
import org.springframework.stereotype.Service;
import static learn.noodemy.domain.Validations.*;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CourseService {

    private final CourseRepository repository;
    private final CategoryRepository categoryRepository;

    public CourseService(CourseRepository repository, CategoryRepository categoryRepository) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
    }

    public List<Course> findAll() {
        return repository.findAll();
    }

    public Result<Course> findById(int courseId) {
        Result<Course> result = new Result<>();
        Course course = repository.findById(courseId);
        if (course == null) {
            result.addMessage("Could not find the course with this id", ResultType.NOT_FOUND);
            return result;
        }
        result.setPayload(course);
        return result;
    }

    public List<Course> findAllByUserId(int userId) {
        return repository.findAllByUserId(userId);
    }

    public Result<Course> add(Course course) {
        Result<Course> result = validate(course);
        if (!result.isSuccess()) {
            return result;
        }

        Course newCourse = repository.add(course);
        result.setPayload(newCourse);

        return result;
    }

    public Result<Course> update(Course course) {
        Result<Course> result = validate(course);

        if (!result.isSuccess()) {
            return result;
        }

        if (course.getCourseId() <= 0) {
            result.addMessage("Course Id is missing", ResultType.INVALID);
            return result;
        }

        if (!repository.update(course)) {
            result.addMessage("Could not locate the course to be updated.", ResultType.NOT_FOUND);
            return result;
        }

        return result;

    }

    public Result<Course> delete(int courseId) {
        Result<Course> result = new Result<>();

        if (!repository.delete(courseId)) {
            result.addMessage("Could not find the course id.", ResultType.NOT_FOUND);
            return result;
        }

        return result;
    }

    private Result<Course> validate(Course course) {

        Result<Course> result = new Result<>();

        if (course == null) {
            result.addMessage("Course cannot be null", ResultType.INVALID);
            return result;
        }

        if (isNullOrBlank(course.getCourseName())) {
            result.addMessage("Course name cannot be blank", ResultType.INVALID);
        }

        if (isNullOrBlank(course.getCourseDescription())) {
            result.addMessage("Description cannot be empty", ResultType.INVALID);
        }

        if (!(isNumberWithinRange(course.getPrice(), BigDecimal.ONE, BigDecimal.valueOf(1000)))) {
            result.addMessage("Price must be between 1 and 1000", ResultType.INVALID);
        }

        if(!(isNumberWithinRange(course.getEstimateDuration(), 1, 100))) {
            result.addMessage("Duration must be between 1 and 100 hours.", ResultType.INVALID);
        }

        if (categoryRepository.findById(course.getCategory().getCategoryId()) == null) {
            result.addMessage("Could not find this course category", ResultType.NOT_FOUND);
            return result;
        }


        if (result.isSuccess()) {
            result.setPayload(course);
        }

        return result;
    }


    private boolean isNumberWithinRange(int n, int min, int max) {
        return n >= min && n <= max;
    }

    private boolean isNumberWithinRange(BigDecimal n, BigDecimal min, BigDecimal max) {
        return (n.compareTo(min) >= 0) && (n.compareTo(max) <= 0);
    }
}
