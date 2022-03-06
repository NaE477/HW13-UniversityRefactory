package controllers.clerkControllers;

import controllers.Utilities;
import models.things.Term;
import models.users.Clerk;
import org.hibernate.SessionFactory;
import services.*;

import java.util.*;

public class ClerkController {
    private final Clerk clerk;
    private final TermService termService;
    private final SignUpController signUpController;
    private final EditController editController;
    private final DeleteController deleteController;
    private final EndTermController endTermController;
    private final Scanner sc = new Scanner(System.in);

    public ClerkController(SessionFactory sessionFactory, Clerk clerk) {
        this.clerk = clerk;
        termService = new TermService(sessionFactory);
        signUpController = new SignUpController(sessionFactory);
        editController = new EditController(sessionFactory);
        deleteController = new DeleteController(sessionFactory);
        endTermController = new EndTermController(sessionFactory);
    }

    public void entry() {
        initiateTerm();
        System.out.println("Welcome to Clerk Section.\nChoose an option:");
        label:
        while (true) {
            System.out.println("1-Add Clerk");
            System.out.println("2-Add Professor");
            System.out.println("3-Add Student");
            System.out.println("4-Add Course");
            System.out.println("5-Edit Clerk");
            System.out.println("6-Edit Professor");
            System.out.println("7-Edit Student");
            System.out.println("8-Edit Course");
            System.out.println("9-Delete Clerk");
            System.out.println("10-Delete Professor");
            System.out.println("11-Delete Student");
            System.out.println("12-Delete Course");
            System.out.println("13-View Pay Check");
            System.out.println("14-End Term");
            System.out.println("0-Exit");
            System.out.print("Option: ");
            String opt = sc.nextLine();

            switch (opt) {
                case "1":
                    signUpController.addClerk();
                    break;
                case "2":
                    signUpController.addProfessor();
                    break;
                case "3":
                    signUpController.addStudent();
                    break;
                case "4":
                    signUpController.addCourse();
                    break;
                case "5":
                    editController.editClerk();
                    break;
                case "6":
                    editController.editProfessor();
                    break;
                case "7":
                    editController.editStudent();
                    break;
                case "8":
                    editController.editCourse();
                    break;
                case "9":
                    deleteController.deleteClerk();
                    break;
                case "10":
                    deleteController.deleteProfessor();
                    break;
                case "11":
                    deleteController.deleteStudent();
                    break;
                case "12":
                    deleteController.deleteCourse();
                    break;
                case "13":
                    System.out.println(clerk.getSalary());
                    break;
                case "14":
                    endTermController.endTerm();
                    break;
                case "0":
                    break label;
                default:
                    System.out.println("Wrong Option");
                    break;
            }
        }
    }

    private void initiateTerm() {
        if(termService.findAll().size() == 0) {
            System.out.println("Enter term prefix: ");
            Integer termPrefix = Utilities.intReceiver();
            Integer firstTermNumber = termPrefix * 10 + 1;
            Term firstTerm = new Term(0,firstTermNumber,null);
            var term = termService.initiate(firstTerm);
            System.out.println("First term initiated as: " + term.getTerm());
        }
    }
}
