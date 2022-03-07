package controllers.studentControllers;

import controllers.Utilities;
import models.things.Course;
import models.things.Grade;
import models.things.Term;
import models.users.Student;
import org.hibernate.SessionFactory;
import services.CourseService;
import services.GradeService;
import services.TermService;

import java.util.List;

public class ViewCoursesController {
    private final GradeService gradeService;
    private final CourseService courseService;
    private final TermService termService;
    private final Student student;

    public ViewCoursesController(SessionFactory sessionFactory,Student student) {
        gradeService = new GradeService(sessionFactory);
        courseService = new CourseService(sessionFactory);
        termService = new TermService(sessionFactory);
        this.student = student;
    }

    public void viewCourses() {
        List<Course> courses = courseService.findAll();
        Term currentTerm = termService.findCurrentTerm();
        courses
                .stream()
                .filter(course -> course.getTerm().equals(currentTerm))
                .forEach(System.out::println);
    }

    public void viewPickedCourses() {
        List<Grade> grades = gradeService.findAllByStudent(student);
        grades.forEach((grade) -> {
            if (grade.getGrade() != 0) {
                Utilities.printGreed(grade.getCourse() + "\nGrade: " + grade);
            } else Utilities.printGreed(grade.getCourse() + "\nProfessor haven't entered a grade for this course yet.");
        });
    }
}
