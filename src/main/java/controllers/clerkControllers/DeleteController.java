package controllers.clerkControllers;

import controllers.Utilities;
import models.things.Course;
import models.things.Term;
import models.users.Clerk;
import models.users.Professor;
import models.users.Student;
import org.hibernate.SessionFactory;
import services.ClerkService;
import services.CourseService;
import services.ProfessorService;
import services.StudentService;

import java.util.List;

public class DeleteController {
    private final ClerkService clerkService;
    private final ProfessorService professorService;
    private final StudentService studentService;
    private final CourseService courseService;

    public DeleteController(SessionFactory sessionFactory) {
        clerkService = new ClerkService(sessionFactory);
        professorService = new ProfessorService(sessionFactory);
        studentService = new StudentService(sessionFactory);
        courseService = new CourseService(sessionFactory);
    }

    public void deleteClerk() {
        List<Clerk> clerks = clerkService.findAll();
        clerks.forEach(System.out::println);
        System.out.println("Enter clerk ID to delete: ");
        Integer clerkToDeleteID = Utilities.intReceiver();
        Clerk clerkToDelete = clerkService.find(clerkToDeleteID);
        if (clerkToDelete != null) {
            clerkService.deleteClerk(clerkToDelete);
        } else System.out.println("Wrong ID");
    }

    public void deleteProfessor() {
        List<Professor> professors = professorService.findAll();
        professors.forEach(System.out::println);
        System.out.println("Professor ID: ");
        Integer profToDeleteID = Utilities.intReceiver();
        Professor professorToDelete = professorService.find(profToDeleteID);
        if (professorToDelete != null) {
            professorService.deleteProfessor(professorToDelete);
        } else System.out.println("Wrong ID");
    }

    public void deleteStudent() {
        List<Student> students = studentService.findAll();
        students.forEach(System.out::println);
        System.out.println("Student ID: ");
        Integer studentToDeleteID = Utilities.intReceiver();
        Student studentToDelete = studentService.find(studentToDeleteID);
        if (studentToDelete != null) {
            studentService.delete(studentToDelete);
        } else System.out.println("Wrong ID");
    }

    public void deleteCourse() {
        List<Course> courses = courseService.findAll();
        courses.forEach(System.out::println);
        System.out.println("Course ID: ");
        Integer courseToDeleteID = Utilities.intReceiver();
        Course courseToDelete = courseService.find(courseToDeleteID);
        if (courseToDelete != null) {
            courseService.deleteCourse(courseToDelete);
        } else System.out.println("Wrong ID");
    }
}
