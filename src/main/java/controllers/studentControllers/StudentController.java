package controllers.studentControllers;

import controllers.Utilities;
import models.things.Course;
import models.things.Grade;
import models.things.Term;
import models.users.Student;
import org.hibernate.SessionFactory;
import services.CourseService;
import services.GradeService;
import services.StudentService;
import services.TermService;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class StudentController {
    private final Scanner sc = new Scanner(System.in);
    private final PickCourseController pickCourseController;
    private final ChangePasswordController changePasswordController;
    private final ViewCoursesController viewCoursesController;
    private final Student student;

    public StudentController(SessionFactory sessionFactory, Student student) {
        pickCourseController = new PickCourseController(sessionFactory, student);
        changePasswordController = new ChangePasswordController(sessionFactory,student);
        viewCoursesController = new ViewCoursesController(sessionFactory,student);
        this.student = student;
    }

    public void entry() {
        System.out.println("Welcome to Student Section," + student.getFirstname() + " " + student.getLastname() + "\nChoose an option:");
        label:
        while (true) {
            System.out.println("1-View Profile");
            System.out.println("2-View Presented Courses");
            System.out.println("3-Pick Courses");
            System.out.println("4-View Picked Courses");
            System.out.println("5-Change Password");
            System.out.println("0-Exit");
            System.out.println("Option: ");
            String opt = sc.nextLine();

            switch (opt) {
                case "1":
                    System.out.println(student);
                    break;
                case "2":
                    viewCoursesController.viewCourses();
                    break;
                case "3":
                    pickCourseController.pickCourse();
                    break;
                case "4":
                    viewCoursesController.viewPickedCourses();
                    break;
                case "5":
                    changePasswordController.changePassword();
                case "0":
                    break label;
                default:
                    System.out.println("Wrong Option.");
                    break;
            }
        }
    }
}
