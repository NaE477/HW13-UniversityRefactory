package controllers.studentControllers;

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
        System.out.println("Old Password: ");
        String oldPass = sc.nextLine();
        System.out.println("New Password: ");
        String newPass = sc.nextLine();
        if (student.getPassword().equals(oldPass)) {
            student.setPassword(newPass);
            studentService.editProfile(student);
        } else System.out.println("Old Password was Wrong.");
    }
}
