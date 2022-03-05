package controllers;

import models.things.Course;
import models.things.Grade;
import models.things.Term;
import models.users.ProfPosition;
import models.users.Professor;
import models.users.Student;
import org.hibernate.SessionFactory;
import repos.GradeRep;
import services.*;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class ProfessorController {
    private final ProfessorService professorService;
    private final CourseService courseService;
    private final StudentService studentService;
    private final GradeService gradeService;
    private final Professor professor;
    private final Term term;
    private final Scanner sc = new Scanner(System.in);

    public ProfessorController(SessionFactory sessionFactory, Professor professor) {
        professorService = new ProfessorService(sessionFactory);
        TermService termService = new TermService(sessionFactory);
        courseService = new CourseService(sessionFactory);
        studentService = new StudentService(sessionFactory);
        gradeService = new GradeService(sessionFactory);
        term = termService.getCurrentTerm();
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
                    enterGrade();
                    break;
                case "3":
                    System.out.println("Which Term: ");
                    Integer term = termReceiver();
                    System.out.println(getSalary(term));
                    break;
                case "4":
                    changePassword();
                    break;
                case "0":
                    break label;
            }
        }
    }

    private void enterGrade() {
        List<Course> courses = courseService.findAllByProfessor(professor);
        courses.forEach(System.out::println);
        System.out.println("Enter Course ID: ");
        Integer courseID = Utilities.intReceiver();
        Course course = courseService.find(courseID);
        if (course != null && courses.contains(course)) {
            List<Student> students = studentService.findAll(course);
            if (students.size() > 0) {
                students.forEach(System.out::println);
                System.out.println("Enter Student ID: ");
                Integer studentID = Utilities.intReceiver();
                Student student = studentService.find(studentID);
                if (student != null && students.contains(student)) {
                    List<Grade> grades = gradeService.findAllByStudent(student);

                    if () {
                        System.out.println("Enter Grade: ");
                        Double grade = gradeReceiver();
                        courseService.insertGradeForStudent(grade, course, student);
                        System.out.println("Grade inserted for student");
                    } else System.out.println("Grade already inserted for student");
                } else System.out.println("Wrong ID");
            } else System.out.println("No Students for this course yet");
        } else System.out.println("Wrong ID");
    }

    private Long getSalary(Integer term) {
        AtomicReference<Long> salary = new AtomicReference<>((long) 0);
        List<Course> termCourses = professor
                .getCourses()
                .stream()
                .filter(course -> course.getTerm().equals(term))
                .collect(Collectors.toList());

        termCourses
                .stream()
                .mapToLong(Course::getUnits)
                .forEach(unit -> salary.updateAndGet(v -> v + unit * 1000000));

        if (professor.getProfPosition().equals(ProfPosition.C)) {
            return salary.updateAndGet(v -> v + 5000000);
        } else {
            return salary.get();
        }
    }

    private void changePassword() {
        System.out.println("Old Password: ");
        String oldPass = sc.nextLine();
        System.out.println("New Password: ");
        String newPass = sc.nextLine();
        if (professor.getPassword().equals(oldPass)) {
            professor.setPassword(newPass);
            Integer changePassID = professorService.editProfile(professor);
            if (changePassID != null) System.out.println("Password changed successfully.");
            else System.out.println("Something went wrong with database");
        } else System.out.println("Old Password was Wrong.");
    }

    private Integer termReceiver() {
        while (true) {
            Integer term = Utilities.intReceiver();
            if (term <= this.term && term > 0) {
                return term;
            } else System.out.println("Wrong term,Choose a term between: " + "1-" + this.term);
        }
    }

    private Double gradeReceiver() {
        while (true) {
            Double grade = Utilities.doubleReceiver();
            if (grade <= 20.0 && grade > 0.0) {
                return grade;
            } else System.out.println("Wrong Grade,Enter a number between 0-20");
        }
    }
}
