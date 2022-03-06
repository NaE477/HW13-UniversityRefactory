package controllers.studentControllers;

import controllers.Utilities;
import models.things.Course;
import models.things.Grade;
import models.things.Term;
import models.users.Student;
import org.hibernate.SessionFactory;
import services.CourseService;
import services.GradeService;
import services.TermService;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class PickCourseController {
    private final CourseService courseService;
    private final GradeService gradeService;
    private final Term term;
    private final Student student;

    public PickCourseController(SessionFactory sessionFactory,Student student) {
        courseService = new CourseService(sessionFactory);
        gradeService = new GradeService(sessionFactory);
        TermService termService = new TermService(sessionFactory);
        term = termService.findCurrentTerm();
        this.student = student;
    }


    public void pickCourse() {
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
}
