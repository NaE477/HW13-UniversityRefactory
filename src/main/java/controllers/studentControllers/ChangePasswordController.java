package controllers.studentControllers;

import controllers.Utilities;
import models.users.Student;
import org.hibernate.SessionFactory;
import services.StudentService;

import java.util.Scanner;

public class ChangePasswordController {
    private final Scanner sc = new Scanner(System.in);
    private final StudentService studentService;
    private final Student student;

    public ChangePasswordController(SessionFactory sessionFactory,Student student) {
        studentService = new StudentService(sessionFactory);
        this.student = student;
    }
    public void changePassword() {
        Utilities.printGreed("Old Password: ");
        String oldPass = sc.nextLine();
        Utilities.printGreed("New Password: ");
        String newPass = sc.nextLine();
        if (student.getPassword().equals(oldPass)) {
            student.setPassword(newPass);
            studentService.editProfile(student);
        } else Utilities.printGreed("Old Password was Wrong.");
    }
}
