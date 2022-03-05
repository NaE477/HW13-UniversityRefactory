package services;

import models.things.Course;
import models.users.Professor;
import models.users.Student;
import org.hibernate.SessionFactory;
import repos.CourseRep;

import java.util.List;

public class CourseService extends BaseService {
    private final CourseRep courseRep;

    public CourseService(SessionFactory sessionFactory) {
        super(sessionFactory);
        courseRep = new CourseRep(super.getSessionFactory());
    }

    public Course createNewCourse(Course course) {
        return courseRep.ins(course);
    }

    public void insertGradeForStudent(Double grade, Course course, Student student) {
        courseToStudentRep.insGrade(grade,course,student);
    }

    public Course find(Integer id) {
        return courseRep.read(id);
    }

    public List<Course> findAll() {
        return courseRep.readAll();
    }

    public List<Course> findAllByProfessor(Professor professor) {
        return courseRep.readAllByProfessor(professor);
    }

    public void pickCourse(Course course, Student student) {
        courseToStudentRep.ins(course, student);
    }

    public void editCourse(Course course) {
        courseRep.update(course);
    }

    public void deleteCourse(Course course) {
        courseRep.delete(course);
    }
}
