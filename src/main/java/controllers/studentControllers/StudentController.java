package controllers.studentControllers;

import controllers.Utilities;
import models.users.Student;
import org.hibernate.SessionFactory;

import java.util.*;

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
        Utilities.printGreed("Welcome to Student Section," + student.getFirstname() + " " + student.getLastname() + "\nChoose an option:");
        label:
        while (true) {
            Utilities.printGreed("1-View Profile");
            Utilities.printGreed("2-View Presented Courses");
            Utilities.printGreed("3-Pick Courses");
            Utilities.printGreed("4-View Picked Courses");
            Utilities.printGreed("5-Change Password");
            Utilities.printGreed("0-Exit");
            Utilities.printGreed("Option: ");
            String opt = sc.nextLine();

            switch (opt) {
                case "1":
                    Utilities.printGreed(String.valueOf(student));
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
                    Utilities.printGreed("Wrong Option.");
                    break;
            }
        }
    }
}
