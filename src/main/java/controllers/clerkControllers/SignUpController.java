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
        System.out.println("New Clerk Created with ID: " + newClerk.getId());
    }

    public void addProfessor() {
        ArrayList<String> profInits = initialReceiver();
        ProfPosition profPosition = profPositionReceiver();
        Professor profToSign = new Professor(0, profInits.get(0), profInits.get(1), profInits.get(2), profInits.get(3), profPosition);
        Professor newProf = professorService.signUpProfessor(profToSign);
        System.out.println("New Professor Created with ID: " + newProf.getId());
    }

    public void addStudent() {
        ArrayList<String> studentInits = initialReceiver();
        Student studentToSign = new Student(0, studentInits.get(0), studentInits.get(1), studentInits.get(2), studentInits.get(3));
        Student newStudent = studentService.signUpStudent(studentToSign);
        System.out.println("New Student Created with ID: " + newStudent.getId());
    }

    public void addCourse() {
        System.out.println("Course Name: ");
        String courseName = sc.nextLine();
        System.out.println("Course units: ");
        Integer units = Utilities.intReceiver();
        List<Professor> professors = professorService.findAll();
        if (professors.size() > 0) {
            professors.forEach(System.out::println);
            System.out.println("Enter professor ID for this Course: ");
            Integer profID = Utilities.intReceiver();
            Professor professorToTeach = professorService.find(profID);
            if (professorToTeach != null) {
                Course newCourse = new Course(0, units, courseName, professorToTeach, null, null);
                Course course = courseService.createNewCourse(newCourse);
                System.out.println("New Course Created with ID: " + course.getId());
            } else System.out.println("Wrong ID");
        } else System.out.println("A Professor must be added first.");
    }

    private ArrayList<String> initialReceiver() {
        System.out.println("Firstname: ");
        String firstname = sc.nextLine();
        System.out.println("Lastname: ");
        String lastname = sc.nextLine();
        String username = Utilities.usernameReceiver();
        System.out.println("Password: ");
        String password = sc.nextLine();
        return new ArrayList<>(Arrays.asList(firstname, lastname, username, password));
    }

    private ProfPosition profPositionReceiver() {
        System.out.println("Committee or Non-Committee(C/NC): ");
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
