package controllers;

import controllers.clerkControllers.ClerkController;
import controllers.professorControllers.ProfessorController;
import controllers.studentControllers.StudentController;
import models.users.Clerk;
import models.users.Professor;
import models.users.Student;
import models.users.User;
import org.hibernate.SessionFactory;
import services.ClerkService;
import services.ProfessorService;
import services.StudentService;

import java.util.*;

public class Entry {

    static Scanner sc = new Scanner(System.in);
    static SessionFactory sessionFactory = SessionFactorySingleton.getInstance();

    public static void main(String[] args) {
        initiateAdmin();

        System.out.println("Welcome to University App.\n");
        while (true) {
            System.out.println("Enter L/l to login or E/e to exit:");
            String opt = sc.nextLine().toUpperCase(Locale.ROOT);

            if (opt.equals("L")) login();
            else if (opt.equals("E")) break;
            else System.out.print("Wrong Option. Choose L/S/E: ");
        }
    }

    private static void login() {
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();
        User user = auth(username, password);
        if (user != null) {
            if (user instanceof Clerk) {
                ClerkController clerkController = new ClerkController(sessionFactory, (Clerk) user);
                clerkController.entry();
            } else if (user instanceof Professor) {
                ProfessorController professorController = new ProfessorController(sessionFactory, (Professor) user);
                professorController.entry();
            } else if (user instanceof Student) {
                StudentController studentController = new StudentController(sessionFactory, (Student) user);
                studentController.entry();
            }
        } else System.out.println("Wrong Username/Password.");
    }

    private static User auth(String username, String password) {
        ClerkService clerkService = new ClerkService(sessionFactory);
        ProfessorService professorService = new ProfessorService(sessionFactory);
        StudentService studentService = new StudentService(sessionFactory);

        Clerk probableClerk = clerkService.find(username);
        Professor probableProfessor = professorService.find(username);
        Student probableStudent = studentService.find(username);

        if (probableClerk != null && probableClerk.getPassword().equals(password)) return probableClerk;
        else if (probableProfessor != null && probableProfessor.getPassword().equals(password))
            return probableProfessor;
        else if (probableStudent != null && probableStudent.getPassword().equals(password)) return probableStudent;
        else return null;
    }

    private static void initiateAdmin(){
        ClerkService clerkService = new ClerkService(sessionFactory);
        if(clerkService.findAll().size() == 0) {
            clerkService.signUpClerk(new Clerk(0,"admin","admin","admin","admin"));
            System.out.println("Admin user initiated due to first time login.");
        }
    }
}
