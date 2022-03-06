package controllers.professorController;

import models.users.Professor;
import org.hibernate.SessionFactory;
import services.ProfessorService;

import java.util.Scanner;

public class ChangePasswordController {
    private final Scanner sc = new Scanner(System.in);
    private final Professor professor;
    private final ProfessorService professorService;

    public ChangePasswordController(SessionFactory sessionFactory,Professor professor) {
        professorService = new ProfessorService(sessionFactory);
        this.professor = professor;
    }

    public void changePassword() {
        System.out.println("Old Password: ");
        String oldPass = sc.nextLine();
        System.out.println("New Password: ");
        String newPass = sc.nextLine();
        if (professor.getPassword().equals(oldPass)) {
            professor.setPassword(newPass);
            professorService.editProfile(professor);
        } else System.out.println("Old Password was Wrong.");
    }
}
