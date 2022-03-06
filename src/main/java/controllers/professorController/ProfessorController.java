package controllers.professorController;

import models.users.Professor;
import org.hibernate.SessionFactory;
import services.*;

import java.util.Scanner;

public class ProfessorController {
    private final EnterGradeController enterGradeController;
    private final GetSalaryController getSalaryController;
    private final ChangePasswordController changePasswordController;
    private final Professor professor;
    private final Scanner sc = new Scanner(System.in);

    public ProfessorController(SessionFactory sessionFactory, Professor professor) {
        enterGradeController = new EnterGradeController(sessionFactory,professor);
        getSalaryController = new GetSalaryController(sessionFactory,professor);
        changePasswordController = new ChangePasswordController(sessionFactory,professor);
        this.professor = professor;
    }

    public void entry() {
        System.out.println("Welcome to Professor Section," + professor.getFirstname() + " " + professor.getLastname() + ".\nChoose an Option:");
        label:
        while (true) {
            System.out.println("1-View Profile");
            System.out.println("2-Enter Grade");
            System.out.println("3-View Paycheck");
            System.out.println("4-Change Password");
            System.out.println("0-Exit");
            System.out.print("Option: ");
            String opt = sc.nextLine();

            switch (opt) {
                case "1":
                    System.out.println(professor);
                    break;
                case "2":
                    enterGradeController.enterGrade();
                    break;
                case "3":
                    getSalaryController.initiation();
                    break;
                case "4":
                    changePasswordController.changePassword();
                    break;
                case "0":
                    break label;
            }
        }
    }


}
