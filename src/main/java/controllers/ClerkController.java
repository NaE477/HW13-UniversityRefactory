package controllers;

import models.things.Course;
import models.things.Term;
import models.users.Clerk;
import models.users.ProfPosition;
import models.users.Professor;
import models.users.Student;
import org.hibernate.SessionFactory;
import services.*;

import java.util.*;

public class ClerkController {
    private final ClerkService clerkService;
    private final ProfessorService professorService;
    private final StudentService studentService;
    private final CourseService courseService;
    private final TermService termService;
    private final Term term;
    private final Clerk clerk;
    private final Scanner sc = new Scanner(System.in);

    public ClerkController(SessionFactory sessionFactory, Clerk clerk) {
        this.clerk = clerk;
        this.clerkService = new ClerkService(sessionFactory);
        this.professorService = new ProfessorService(sessionFactory);
        this.studentService = new StudentService(sessionFactory);
        this.courseService = new CourseService(sessionFactory);
        this.termService = new TermService(sessionFactory);
        term = termService.getCurrentTerm();
    }

    public void entry() {
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
                    addClerk();
                    break;
                case "2":
                    addProfessor();
                    break;
                case "3":
                    addStudent();
                    break;
                case "4":
                    addCourse();
                    break;
                case "5":
                    editClerk();
                    break;
                case "6":
                    editProfessor();
                    break;
                case "7":
                    editStudent();
                    break;
                case "8":
                    editCourse();
                    break;
                case "9":
                    deleteClerk();
                    break;
                case "10":
                    deleteProfessor();
                    break;
                case "11":
                    deleteStudent();
                    break;
                case "12":
                    deleteCourse();
                    break;
                case "13":
                    System.out.println(clerk.getSalary());
                    break;
                case "14":
                    System.out.println("Sure?(Y/N)");
                    String yOrN = sc.nextLine().toUpperCase(Locale.ROOT);
                    if (yOrN.equals("Y")) {
                        termService.endTerm();
                        System.out.println("Term Ended successfully.");
                    } else {
                        System.out.println("Cancelled.");
                    }
                    break;
                case "0":
                    break label;
                default:
                    System.out.println("Wrong Option");
                    break;
            }
        }
    }

    private void addClerk() {
        ArrayList<String> clerkInits = initialReceiver();
        Clerk clerkToSign = new Clerk(0, clerkInits.get(0), clerkInits.get(1), clerkInits.get(2), clerkInits.get(3));
        Clerk newClerk = clerkService.signUpClerk(clerkToSign);
        System.out.println("New Clerk Created with ID: " + newClerk.getId());
    }

    private void addProfessor() {
        ArrayList<String> profInits = initialReceiver();
        ProfPosition profPosition = profPositionReceiver();
        Professor profToSign = new Professor(0, profInits.get(0), profInits.get(1), profInits.get(2), profInits.get(3), profPosition);
        Professor newProf = professorService.signUpProfessor(profToSign);
        System.out.println("New Professor Created with ID: " + newProf.getId());
    }

    private void addStudent() {
        ArrayList<String> studentInits = initialReceiver();
        Student studentToSign = new Student(0, studentInits.get(0), studentInits.get(1), studentInits.get(2), studentInits.get(3));
        Student newStudent = studentService.signUpStudent(studentToSign);
        System.out.println("New Student Created with ID: " + newStudent.getId());
    }

    private void addCourse() {
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

    private void editClerk() {
        List<Clerk> clerks = clerkService.findAll();
        clerks.forEach(System.out::println);
        System.out.println("Choose Clerk ID to edit: ");
        Integer clerkID = Utilities.intReceiver();
        Clerk clerk = clerkService.find(clerkID);
        if (clerk != null) {
            label:
            while (true) {
                System.out.println("1-Change username");
                System.out.println("2-Change firstname/lastname");
                System.out.println("0-Finish Editing");
                String opt = sc.nextLine();
                switch (opt) {
                    case "1":
                        System.out.println("New Username: ");
                        String newUsername = Utilities.usernameReceiver();
                        clerk.setUsername(newUsername);
                        break;
                    case "2":
                        System.out.println("New Firstname: ");
                        String newFirstName = sc.nextLine();
                        System.out.println("New Lastname: ");
                        String newLastName = sc.nextLine();
                        clerk.setFirstname(newFirstName);
                        clerk.setLastname(newLastName);
                        break;
                    case "0":
                        clerkService.editProfile(clerk);
                        System.out.println("Clerk updated");
                        break label;
                    default:
                        System.out.println("Wrong Option");
                }
            }
        } else System.out.println("Wrong ID");
    }

    private void editProfessor() {
        List<Professor> professors = professorService.findAll();
        if (professors.size() > 0) {
            professors.forEach(System.out::println);
            System.out.println("Enter Professor ID to edit: ");
            Integer profToEditID = Utilities.intReceiver();
            Professor professor = professorService.find(profToEditID);
            if (professor != null) {
                label:
                while (true) {
                    System.out.println("1-Change Username");
                    System.out.println("2-Change Full Name");
                    System.out.println("3-Change Committee Status");
                    System.out.println("0-Finish Editing");
                    System.out.print("Option: ");
                    String opt = sc.nextLine();
                    switch (opt) {
                        case "1":
                            System.out.println("New Username: ");
                            String newUsername = Utilities.usernameReceiver();
                            professor.setUsername(newUsername);
                            break;
                        case "2":
                            System.out.println("New Firstname: ");
                            String newFirstName = sc.nextLine();
                            System.out.println("New Lastname: ");
                            String newLastName = sc.nextLine();
                            professor.setFirstname(newFirstName);
                            professor.setLastname(newLastName);
                            break;
                        case "3":
                            if (professor.getProfPosition().equals(ProfPosition.C)) {
                                professor.setProfPosition(ProfPosition.NC);
                            } else professor.setProfPosition(ProfPosition.C);
                            System.out.println("Professor Position Changed.");
                            break;
                        case "0":
                            professorService.editProfile(professor);
                            break label;
                        default:
                            System.out.println("Wrong Option");
                            break;
                    }
                }
            } else System.out.println("Wrong ID");
        } else System.out.println("No Professors Added yet.");
    }

    private void editStudent() {
        List<Student> students = studentService.findAll();
        if (students.size() > 0) {
            students.forEach(System.out::println);
            System.out.println("Enter Student ID to edit: ");
            Integer studentToEditID = Utilities.intReceiver();
            Student student = studentService.find(studentToEditID);
            if (student != null) {
                label:
                while (true) {
                    System.out.println("1-Change Username");
                    System.out.println("2-Change Full Name");
                    System.out.println("0-Finish Editing");
                    System.out.print("Option: ");
                    String opt = sc.nextLine();
                    switch (opt) {
                        case "1":
                            System.out.println("New Username: ");
                            String newUsername = Utilities.usernameReceiver();
                            student.setUsername(newUsername);
                            break;
                        case "2":
                            System.out.println("New Firstname: ");
                            String newFirstName = sc.nextLine();
                            System.out.println("New Lastname: ");
                            String newLastName = sc.nextLine();
                            student.setFirstname(newFirstName);
                            student.setLastname(newLastName);
                            break;
                        case "0":
                            studentService.editProfile(student);
                            break label;
                        default:
                            System.out.println("Wrong Option");
                            break;
                    }
                }
            } else System.out.println("Wrong ID");
        } else System.out.println("No Student Added yet.");
    }

    private void editCourse() {
        List<Course> courses = courseService.findAll();
        if (courses.size() > 0) {
            courses.forEach(System.out::println);
            Integer courseToEditId = Utilities.intReceiver();
            Course courseToEdit = courseService.find(courseToEditId);
            if (courseToEdit != null) {
                label:
                while (true) {
                    System.out.println("1-Change Name");
                    System.out.println("2-Change Units");
                    System.out.println("3-Change Professor");
                    System.out.println("0-Finish Editing");
                    System.out.print("Option: ");
                    String opt = sc.nextLine();
                    switch (opt) {
                        case "1":
                            System.out.println("New Name: ");
                            String newName = sc.nextLine();
                            courseToEdit.setCourseName(newName);
                            break;
                        case "2":
                            System.out.println("New Units: ");
                            Integer newUnit = Utilities.intReceiver();
                            courseToEdit.setUnits(newUnit);
                            break;
                        case "3":
                            List<Professor> professors = professorService.findAll();
                            professors.forEach(System.out::println);
                            System.out.println("Enter professor ID to set for the course: ");
                            Integer profId = Utilities.intReceiver();
                            Professor professor = professorService.find(profId);
                            if (professor != null) {
                                if (courseToEdit.getProfessor() != null) {
                                    if (!courseToEdit.getProfessor().equals(professor)) {
                                        courseToEdit.setProfessor(professor);
                                    } else System.out.println("It's the same professor");
                                } else courseToEdit.setProfessor(professor);
                            } else System.out.println("Wrong ID");
                            break;
                        case "0":
                            courseService.editCourse(courseToEdit);
                            break label;
                    }
                }
            } else System.out.println("Wrong ID");
        } else System.out.println("No Courses added yet");
    }

    private void deleteClerk() {
        List<Clerk> clerks = clerkService.findAll();
        clerks.forEach(System.out::println);
        System.out.println("Enter clerk ID to delete: ");
        Integer clerkToDeleteID = Utilities.intReceiver();
        Clerk clerkToDelete = clerkService.find(clerkToDeleteID);
        if (clerkToDelete != null) {
            clerkService.deleteClerk(clerkToDelete);
        } else System.out.println("Wrong ID");
    }

    private void deleteProfessor() {
        List<Professor> professors = professorService.findAll();
        professors.forEach(System.out::println);
        System.out.println("Professor ID: ");
        Integer profToDeleteID = Utilities.intReceiver();
        Professor professorToDelete = professorService.find(profToDeleteID);
        if (professorToDelete != null) {
            professorService.deleteProfessor(professorToDelete);
        } else System.out.println("Wrong ID");
    }

    private void deleteStudent() {
        List<Student> students = studentService.findAll();
        students.forEach(System.out::println);
        System.out.println("Student ID: ");
        Integer studentToDeleteID = Utilities.intReceiver();
        Student studentToDelete = studentService.find(studentToDeleteID);
        if (studentToDelete != null) {
            studentService.delete(studentToDelete);
        } else System.out.println("Wrong ID");
    }

    private void deleteCourse() {
        List<Course> courses = courseService.findAll();
        courses.forEach(System.out::println);
        System.out.println("Course ID: ");
        Integer courseToDeleteID = Utilities.intReceiver();
        Course courseToDelete = courseService.find(courseToDeleteID);
        if (courseToDelete != null) {
            courseService.deleteCourse(courseToDelete);
        } else System.out.println("Wrong ID");
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
