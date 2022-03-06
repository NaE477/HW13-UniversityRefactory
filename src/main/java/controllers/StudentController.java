package controllers;

import models.things.Course;
import models.things.Grade;
import models.things.Term;
import models.users.Student;
import org.hibernate.SessionFactory;
import services.CourseService;
import services.GradeService;
import services.StudentService;
import services.TermService;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class StudentController {
    private final StudentService studentService;
    private final CourseService courseService;
    private final GradeService gradeService;
    private final Scanner sc = new Scanner(System.in);
    private final Student student;
    private final Term term;

    public StudentController(SessionFactory sessionFactory, Student student) {
        this.studentService = new StudentService(sessionFactory);
        this.courseService = new CourseService(sessionFactory);
        this.gradeService = new GradeService(sessionFactory);
        this.student = student;
        TermService termService = new TermService(sessionFactory);
        this.term = termService.findCurrentTerm();
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
        List<Grade> pickedCoursesWithGrade = gradeService.findAllByStudent(student);
        List<Course> pickedCourses = pickedCoursesWithGrade.stream().map(Grade::getCourse).collect(Collectors.toList());
        courses.forEach(System.out::println);
        System.out.println("Enter Course ID you want to pick:");
        Integer courseToPickId = Utilities.intReceiver();
        Course courseToPick = courseService.find(courseToPickId);
        if (courseToPick != null) {
            if (!pickedCourses.contains(courseToPick)) {
                if (canPick(courseToPick)) {
                    Grade grade = gradeService.pickCourse(new Grade(0,student,courseToPick,null));
                    System.out.println(grade.getCourse().getCourseName() + " picked successfully");
                } else System.out.println("You Can't Pick this course.Your unit threshold is filled.");
            } else System.out.println("Already Picked");
        } else System.out.println("Wrong ID");
    }

    private void viewPickedCourses() {
        List<Grade> grades = gradeService.findAllByStudent(student);
        grades.forEach((grade) -> {
            if (grade.getGrade() != 0) {
                System.out.println(grade.getCourse() + "\nGrade: " + grade);
            } else System.out.println(grade.getCourse() + "\nProfessor haven't entered a grade for this course yet.");
        });
    }

    private Boolean canPick(Course courseToPick) {
        Set<Grade> finishedCourses = student.getGrades()
                .stream()
                .filter(courseDoubleEntry -> !Objects.equals(courseDoubleEntry.getCourse().getTerm(), term))
                .collect(Collectors.toSet());

        Set<Grade> unfinishedCourses = student.getGrades()
                .stream()
                .filter(grade -> Objects.equals(grade.getCourse().getTerm(), term))
                .collect(Collectors.toSet());


        AtomicReference<Integer> unitsPicked = new AtomicReference<>(0);
        if (unfinishedCourses.size() > 0) {
            unfinishedCourses.forEach(grade -> unitsPicked.updateAndGet(v -> v + (grade.getCourse().getUnits())));
        }

        AtomicReference<Double> gradeSum = new AtomicReference<>(0.0);
        if (finishedCourses.size() > 0) {
            finishedCourses.forEach((grade) -> gradeSum.updateAndGet(v -> v + grade.getCourse().getUnits() * grade.getGrade()));
        }

        AtomicReference<Integer> unitsPasses = new AtomicReference<>(0);
        if(finishedCourses.size() > 0){
            finishedCourses.forEach((grade) -> unitsPasses.updateAndGet(v -> v + grade.getCourse().getUnits()));
        }

        if (finishedCourses.size() > 0) {
            if(unfinishedCourses.size() > 0 && gradeSum.get() > 0) {
                double averageGrade = gradeSum.get() / unitsPasses.get();

                int pickingThreshold;
                if (averageGrade > 18) pickingThreshold = 24;
                else pickingThreshold = 20;

                return unitsPicked.get() + courseToPick.getUnits() < pickingThreshold;
            } else return true;
        } else return false;
    }

    private void changePassword() {
        System.out.println("Old Password: ");
        String oldPass = sc.nextLine();
        System.out.println("New Password: ");
        String newPass = sc.nextLine();
        if (student.getPassword().equals(oldPass)) {
            student.setPassword(newPass);
            studentService.editProfile(student);
        } else System.out.println("Old Password was Wrong.");
    }
}
