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

import java.util.*;

public class SignUpController {
    private final Scanner sc = new Scanner(System.in);
    private final ClerkService clerkService;
    private final ProfessorService professorService;
    private final StudentService studentService;
    private final CourseService courseService;

    public SignUpController(SessionFactory sessionFactory) {
        clerkService = new ClerkService(sessionFactory);
        professorService = new ProfessorService(sessionFactory);
        studentService = new StudentService(sessionFactory);
        courseService = new CourseService(sessionFactory);
    }



    public void addClerk() {
        ArrayList<String> clerkInits = initialReceiver();
        Clerk clerkToSign = new Clerk(0, clerkInits.get(0), clerkInits.get(1), clerkInits.get(2), clerkInits.get(3));
        Clerk newClerk = clerkService.signUpClerk(clerkToSign);
        Utilities.printGreed("New Clerk Created with ID: " + newClerk.getId());
    }

    public void addProfessor() {
        ArrayList<String> profInits = initialReceiver();
        ProfPosition profPosition = profPositionReceiver();
        Professor profToSign = new Professor(0, profInits.get(0), profInits.get(1), profInits.get(2), profInits.get(3), profPosition);
        Professor newProf = professorService.signUpProfessor(profToSign);
        Utilities.printGreed("New Professor Created with ID: " + newProf.getId());
    }

    public void addStudent() {
        ArrayList<String> studentInits = initialReceiver();
        Student studentToSign = new Student(0, studentInits.get(0), studentInits.get(1), studentInits.get(2), studentInits.get(3));
        Student newStudent = studentService.signUpStudent(studentToSign);
        Utilities.printGreed("New Student Created with ID: " + newStudent.getId());
    }

    public void addCourse() {
        Utilities.printGreed("Course Name: ");
        String courseName = sc.nextLine();
        Utilities.printGreed("Course units: ");
        Integer units = Utilities.intReceiver();
        List<Professor> professors = professorService.findAll();
        if (professors.size() > 0) {
            professors.forEach(System.out::println);
            Utilities.printGreed("Enter professor ID for this Course: ");
            Integer profID = Utilities.intReceiver();
            Professor professorToTeach = professorService.find(profID);
            if (professorToTeach != null) {
                Course newCourse = new Course(0, units, courseName, professorToTeach, null, null);
                Course course = courseService.createNewCourse(newCourse);
                Utilities.printGreed("New Course Created with ID: " + course.getId());
            } else Utilities.printGreed("Wrong ID");
        } else Utilities.printGreed("A Professor must be added first.");
    }

    private ArrayList<String> initialReceiver() {
        Utilities.printGreed("Firstname: ");
        String firstname = sc.nextLine();
        Utilities.printGreed("Lastname: ");
        String lastname = sc.nextLine();
        String username = Utilities.usernameReceiver();
        Utilities.printGreed("Password: ");
        String password = sc.nextLine();
        return new ArrayList<>(Arrays.asList(firstname, lastname, username, password));
    }

    private ProfPosition profPositionReceiver() {
        Utilities.printGreed("Committee or Non-Committee(C/NC): ");
        while (true) {
            String cOrNc = sc.nextLine().toUpperCase(Locale.ROOT);
            if (cOrNc.equals("C")) {
                return ProfPosition.C;
            } else if (cOrNc.equals("NC")) {
                return ProfPosition.NC;
            } else System.out.print("Wrong Input,Try C or NC: ");
        }
    }
}
