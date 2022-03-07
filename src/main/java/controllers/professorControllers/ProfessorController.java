package controllers.professorControllers;

import controllers.Utilities;
import models.users.Professor;
import org.hibernate.SessionFactory;

import java.util.Scanner;

public class ProfessorController {
    private final EnterGradeController enterGradeController;
    private final GetSalaryController getSalaryController;
    private final ChangePasswordController changePasswordController;
    private final Professor professor;
    private final Scanner sc = new Scanner(System.in);

    public ProfessorController(SessionFactory sessionFactory, Professor professor) {
        enterGradeController = new EnterGradeController(sessionFactory, professor);
        getSalaryController = new GetSalaryController(sessionFactory, professor);
        changePasswordController = new ChangePasswordController(sessionFactory, professor);
        this.professor = professor;
    }

    public void entry() {
        Utilities.printGreed("Welcome to Professor Section," + professor.getFirstname() + " " + professor.getLastname() + ".\nChoose an Option:");
        label:
        while (true) {
            Utilities.printGreed("1-View Profile");
            Utilities.printGreed("2-Enter Grade");
            Utilities.printGreed("3-View Paycheck");
            Utilities.printGreed("4-Change Password");
            Utilities.printGreed("0-Exit");
            System.out.print("Option: ");
            String opt = sc.nextLine();

            switch (opt) {
                case "1":
                    Utilities.printGreed(String.valueOf(professor));
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
