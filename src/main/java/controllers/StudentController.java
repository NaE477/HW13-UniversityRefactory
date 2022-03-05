package controllers;

import models.users.Student;
import org.hibernate.SessionFactory;
import services.CourseService;
import services.StudentService;
import services.TermService;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class StudentController {
    private final StudentService studentService;
    private final CourseService courseService;
    private final Scanner sc = new Scanner(System.in);
    private final Student student;
    private final Integer term;

    public StudentController(SessionFactory sessionFactory, Student student) {
        this.studentService = new StudentService(sessionFactory);
        this.courseService = new CourseService(sessionFactory);
        TermService termService = new TermService(sessionFactory);
        this.student = student;
        this.term = termService.getCurrentTerm();
    }

    public void entry() {
        System.out.println("Welcome to Student Section," + student.getFirstname() + " " + student.getLastname() + "\nChoose an option:");
        label:
        while (true) {
            System.out.println("1-View Profile");
            System.out.println("2-View Presented Courses");
            System.out.println("3-Pick Courses");
            System.out.println("4-View Picked Courses");
            System.out.println("5-Change Password");
            System.out.println("0-Exit");
            System.out.println("Option: ");
            String opt = sc.nextLine();

            switch (opt) {
                case "1":
                    System.out.println(student);
                    break;
                case "2":
                    List<Course> courses = courseService.findAll();
                    courses.forEach(System.out::println);
                    break;
                case "3":
                    pickCourse();
                    break;
                case "4":
                    viewPickedCourses();
                    break;
                case "0":
                    break label;
                case "5":
                    changePassword();
                default:
                    System.out.println("Wrong Option.");
                    break;
            }
        }
    }

    private void pickCourse() {
        List<Course> courses = courseService.findAll();
        HashMap<Course, Double> pickedCourses = courseService.findAllByStudent(student);
        courses.forEach(System.out::println);
        System.out.println("Enter Course ID you want to pick:");
        Integer courseToPickId = Utilities.intReceiver();
        Course courseToPick = courseService.find(courseToPickId);
        if (courseToPick != null) {
            if (!pickedCourses.containsKey(courseToPick)) {
                if (canPick(courseToPick, pickedCourses)) {
                    courseService.pickCourse(courseToPick, student);
                    System.out.println("Course picked successfully");
                } else System.out.println("You Can't Pick this course.Your unit threshold is filled.");
            } else System.out.println("Already Picked");
        } else System.out.println("Wrong ID");
    }

    private void viewPickedCourses() {
        Map<Course, Double> courses = courseService.findAllByStudent(student);
        courses.forEach((course, grade) -> {
            if (grade != 0) {
                System.out.println(course + "\nGrade: " + grade);
            } else System.out.println(course + "\nProfessor haven't entered a grade for this course yet.");
        });
    }

    private Boolean canPick(Course courseToPick, HashMap<Course, Double> pickedCourses) {
        Map<Course, Double> finishedCourses = pickedCourses
                .entrySet()
                .stream()
                .filter(courseDoubleEntry -> !Objects.equals(courseDoubleEntry.getKey().getTerm(), term))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        List<Course> unfinishedCourses = new ArrayList<>(pickedCourses
                .entrySet()
                .stream()
                .filter(a -> Objects.equals(a.getKey().getTerm(), term))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).keySet());


        AtomicReference<Integer> unitsPicked = new AtomicReference<>(0);
        if (unfinishedCourses.size() > 0) {
            unfinishedCourses.forEach(course -> unitsPicked.updateAndGet(v -> v + (course.getUnits())));
        }

        AtomicReference<Double> gradeSum = new AtomicReference<>(0.0);
        if (finishedCourses.size() > 0) {
            finishedCourses.forEach((course, grade) -> {
                assert false;
                gradeSum.updateAndGet(v -> v + course.getUnits() * grade);
            });
        }

        AtomicReference<Integer> unitsPasses = new AtomicReference<>(0);
        if(finishedCourses.size() > 0){
            finishedCourses.forEach((course,grade) -> {
                assert false;
                unitsPasses.updateAndGet(v -> v + course.getUnits());
            });
        }

        assert false;
        if (unfinishedCourses.size() > 0 && finishedCourses.size() > 0) {
            double averageGrade = gradeSum.get() / unitsPasses.get();

            int pickingThreshold;
            assert false;
            if (averageGrade > 18) pickingThreshold = 24;
            else pickingThreshold = 20;

            return unitsPicked.get() + courseToPick.getUnits() < pickingThreshold;
        } else return true;
    }

    private void changePassword() {
        System.out.println("Old Password: ");
        String oldPass = sc.nextLine();
        System.out.println("New Password: ");
        String newPass = sc.nextLine();
        if (student.getPassword().equals(oldPass)) {
            student.setPassword(newPass);
            Integer changePassID = studentService.editProfile(student);
            if (changePassID != null) System.out.println("Password changed successfully.");
            else System.out.println("Something went wrong with database");
        } else System.out.println("Old Password was Wrong.");
    }
}
