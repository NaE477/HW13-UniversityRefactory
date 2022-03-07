package controllers.professorControllers;

import controllers.Utilities;
import models.things.Course;
import models.things.Grade;
import models.users.Professor;
import models.users.Student;
import org.hibernate.SessionFactory;
import services.CourseService;
import services.GradeService;
import services.StudentService;

import java.util.List;

public class EnterGradeController {
    private final CourseService courseService;
    private final StudentService studentService;
    private final GradeService gradeService;
    private final Professor professor;

    public EnterGradeController(SessionFactory sessionFactory,Professor professor) {
        courseService = new CourseService(sessionFactory);
        studentService = new StudentService(sessionFactory);
        gradeService = new GradeService(sessionFactory);
        this.professor = professor;
    }

    public void enterGrade() {
        List<Course> courses = courseService.findAllByProfessor(professor);
        courses.forEach(System.out::println);
        Utilities.printGreed("Enter Course ID: ");
        Integer courseID = Utilities.intReceiver();
        Course course = courseService.find(courseID);
        if (course != null && courses.contains(course)) {
            List<Student> students = studentService.findAll(course);
            if (students.size() > 0) {
                students.forEach(System.out::println);
                Utilities.printGreed("Enter Student ID: ");
                Integer studentID = Utilities.intReceiver();
                Student student = studentService.find(studentID);
                if (student != null && students.contains(student)) {
                    Grade grade = gradeService.find(student,course);
                    if (grade.getGrade() == null) {
                        Utilities.printGreed("Enter Grade: ");
                        Double gradeToSave = gradeReceiver();
                        grade.setGrade(gradeToSave);
                        gradeService.updateGrade(grade);
                        Utilities.printGreed("Grade inserted for student");
                    } else Utilities.printGreed("Grade already inserted for student,try editing it.");
                } else Utilities.printGreed("Wrong Student ID");
            } else Utilities.printGreed("No Students for this course yet");
        } else Utilities.printGreed("Wrong Course ID");
    }

    private Double gradeReceiver() {
        while (true) {
            Double grade = Utilities.doubleReceiver();
            if (grade <= 20.0 && grade > 0.0) {
                return grade;
            } else Utilities.printGreed("Wrong Grade,Enter a number between 0-20");
        }
    }
}
