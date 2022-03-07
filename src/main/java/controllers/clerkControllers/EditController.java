package controllers.clerkControllers;

import controllers.Utilities;
import models.things.Course;
import models.users.Clerk;
import models.users.ProfPosition;
import models.users.Professor;
import models.users.Student;
import org.hibernate.SessionFactory;
import services.ClerkService;
import services.CourseService;
import services.ProfessorService;
import services.StudentService;

import java.util.List;
import java.util.Scanner;

public class EditController {
    private final Scanner sc = new Scanner(System.in);
    private final ClerkService clerkService;
    private final ProfessorService professorService;
    private final StudentService studentService;
    private final CourseService courseService;

    public EditController(SessionFactory sessionFactory) {
        clerkService = new ClerkService(sessionFactory);
        professorService = new ProfessorService(sessionFactory);
        studentService = new StudentService(sessionFactory);
        courseService = new CourseService(sessionFactory);
    }

    public void editClerk() {
        List<Clerk> clerks = clerkService.findAll();
        clerks.forEach(System.out::println);
        Utilities.printGreed("Choose Clerk ID to edit: ");
        Integer clerkID = Utilities.intReceiver();
        Clerk clerk = clerkService.find(clerkID);
        if (clerk != null) {
            label:
            while (true) {
                Utilities.printGreed("1-Change username");
                Utilities.printGreed("2-Change firstname/lastname");
                Utilities.printGreed("0-Finish Editing");
                String opt = sc.nextLine();
                switch (opt) {
                    case "1":
                        Utilities.printGreed("New Username: ");
                        String newUsername = Utilities.usernameReceiver();
                        clerk.setUsername(newUsername);
                        break;
                    case "2":
                        Utilities.printGreed("New Firstname: ");
                        String newFirstName = sc.nextLine();
                        Utilities.printGreed("New Lastname: ");
                        String newLastName = sc.nextLine();
                        clerk.setFirstname(newFirstName);
                        clerk.setLastname(newLastName);
                        break;
                    case "0":
                        clerkService.editProfile(clerk);
                        Utilities.printGreed("Clerk updated");
                        break label;
                    default:
                        Utilities.printGreed("Wrong Option");
                }
            }
        } else Utilities.printGreed("Wrong ID");
    }

    public void editProfessor() {
        List<Professor> professors = professorService.findAll();
        if (professors.size() > 0) {
            professors.forEach(System.out::println);
            Utilities.printGreed("Enter Professor ID to edit: ");
            Integer profToEditID = Utilities.intReceiver();
            Professor professor = professorService.find(profToEditID);
            if (professor != null) {
                label:
                while (true) {
                    Utilities.printGreed("1-Change Username");
                    Utilities.printGreed("2-Change Full Name");
                    Utilities.printGreed("3-Change Committee Status");
                    Utilities.printGreed("0-Finish Editing");
                    System.out.print("Option: ");
                    String opt = sc.nextLine();
                    switch (opt) {
                        case "1":
                            Utilities.printGreed("New Username: ");
                            String newUsername = Utilities.usernameReceiver();
                            professor.setUsername(newUsername);
                            break;
                        case "2":
                            Utilities.printGreed("New Firstname: ");
                            String newFirstName = sc.nextLine();
                            Utilities.printGreed("New Lastname: ");
                            String newLastName = sc.nextLine();
                            professor.setFirstname(newFirstName);
                            professor.setLastname(newLastName);
                            break;
                        case "3":
                            if (professor.getProfPosition().equals(ProfPosition.C)) {
                                professor.setProfPosition(ProfPosition.NC);
                            } else professor.setProfPosition(ProfPosition.C);
                            Utilities.printGreed("Professor Position Changed.");
                            break;
                        case "0":
                            professorService.editProfile(professor);
                            break label;
                        default:
                            Utilities.printGreed("Wrong Option");
                            break;
                    }
                }
            } else Utilities.printGreed("Wrong ID");
        } else Utilities.printGreed("No Professors Added yet.");
    }

    public void editStudent() {
        List<Student> students = studentService.findAll();
        if (students.size() > 0) {
            students.forEach(System.out::println);
            Utilities.printGreed("Enter Student ID to edit: ");
            Integer studentToEditID = Utilities.intReceiver();
            Student student = studentService.find(studentToEditID);
            if (student != null) {
                label:
                while (true) {
                    Utilities.printGreed("1-Change Username");
                    Utilities.printGreed("2-Change Full Name");
                    Utilities.printGreed("0-Finish Editing");
                    System.out.print("Option: ");
                    String opt = sc.nextLine();
                    switch (opt) {
                        case "1":
                            Utilities.printGreed("New Username: ");
                            String newUsername = Utilities.usernameReceiver();
                            student.setUsername(newUsername);
                            break;
                        case "2":
                            Utilities.printGreed("New Firstname: ");
                            String newFirstName = sc.nextLine();
                            Utilities.printGreed("New Lastname: ");
                            String newLastName = sc.nextLine();
                            student.setFirstname(newFirstName);
                            student.setLastname(newLastName);
                            break;
                        case "0":
                            studentService.editProfile(student);
                            break label;
                        default:
                            Utilities.printGreed("Wrong Option");
                            break;
                    }
                }
            } else Utilities.printGreed("Wrong ID");
        } else Utilities.printGreed("No Student Added yet.");
    }

    public void editCourse() {
        List<Course> courses = courseService.findAll();
        if (courses.size() > 0) {
            courses.forEach(System.out::println);
            Integer courseToEditId = Utilities.intReceiver();
            Course courseToEdit = courseService.find(courseToEditId);
            if (courseToEdit != null) {
                label:
                while (true) {
                    Utilities.printGreed("1-Change Name");
                    Utilities.printGreed("2-Change Units");
                    Utilities.printGreed("3-Change Professor");
                    Utilities.printGreed("0-Finish Editing");
                    System.out.print("Option: ");
                    String opt = sc.nextLine();
                    switch (opt) {
                        case "1":
                            Utilities.printGreed("New Name: ");
                            String newName = sc.nextLine();
                            courseToEdit.setCourseName(newName);
                            break;
                        case "2":
                            Utilities.printGreed("New Units: ");
                            Integer newUnit = Utilities.intReceiver();
                            courseToEdit.setUnits(newUnit);
                            break;
                        case "3":
                            List<Professor> professors = professorService.findAll();
                            professors.forEach(System.out::println);
                            Utilities.printGreed("Enter professor ID to set for the course: ");
                            Integer profId = Utilities.intReceiver();
                            Professor professor = professorService.find(profId);
                            if (professor != null) {
                                if (courseToEdit.getProfessor() != null) {
                                    if (!courseToEdit.getProfessor().equals(professor)) {
                                        courseToEdit.setProfessor(professor);
                                    } else Utilities.printGreed("It's the same professor");
                                } else courseToEdit.setProfessor(professor);
                            } else Utilities.printGreed("Wrong ID");
                            break;
                        case "0":
                            courseService.editCourse(courseToEdit);
                            break label;
                    }
                }
            } else Utilities.printGreed("Wrong ID");
        } else Utilities.printGreed("No Courses added yet");
    }
}
