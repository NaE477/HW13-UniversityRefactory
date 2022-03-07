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

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class PickCourseController {
    private final CourseService courseService;
    private final GradeService gradeService;
    private final TermService termService;
    private final Term term;
    private final Student student;

    public PickCourseController(SessionFactory sessionFactory, Student student) {
        courseService = new CourseService(sessionFactory);
        gradeService = new GradeService(sessionFactory);
        termService = new TermService(sessionFactory);
        term = termService.findCurrentTerm();
        this.student = student;
    }


    public void pickCourse() {
        List<Course> courses = courseService.findAll(term);
        List<Grade> pickedCoursesWithGrade = gradeService.findAllByStudent(student)
                .stream()
                .filter(grade -> grade.getGrade() == null)
                .collect(Collectors.toList());

        List<Course> finishedCourses = pickedCoursesWithGrade
                .stream()
                .map(Grade::getCourse)
                .collect(Collectors.toList());

        courses.forEach(System.out::println);
        Utilities.printGreen("Enter Course ID you want to pick:");
        Integer courseToPickId = Utilities.intReceiver();
        Course courseToPick = courses
                .stream().filter(c -> Objects.equals(c.getId(), courseToPickId))
                .findAny()
                .orElse(null);
        if (courseToPick != null) {
            if (!finishedCourses.contains(courseToPick)) {
                if (canPick(courseToPick)) {
                    Grade grade = gradeService.pickCourse(new Grade(0, student, courseToPick, null));
                    Utilities.printGreen(grade.getCourse().getCourseName() + " picked successfully");
                } else Utilities.printGreen("You Can't Pick this course.Your unit threshold is filled.");
            } else Utilities.printGreen("Already Picked");
        } else Utilities.printGreen("Wrong ID");
    }

    private Boolean canPick(Course courseToPick) {
        Set<Grade> finishedCoursesLastTerm = finishedCourses(student);

        Set<Grade> unfinishedCoursesThisTerm = unfinishedCourses(student);

        Integer unitsPicked = unitsPicked(unfinishedCoursesThisTerm);

        Double gradeSum = gradeSum(finishedCoursesLastTerm);

        Integer unitsPassed = unitsPassed(finishedCoursesLastTerm);

        if (finishedCoursesLastTerm.size() > 0) {
            if (unfinishedCoursesThisTerm.size() > 0 && gradeSum > 0) {
                double averageGrade = gradeSum / unitsPassed;

                int pickingThreshold;
                if (averageGrade > 18) pickingThreshold = 24;
                else pickingThreshold = 20;

                return unitsPicked + courseToPick.getUnits() < pickingThreshold;
            } else return true;
        } else return false;
    }

    private Set<Grade> finishedCourses(Student student) {
        if (term.equals(termService.findFirstTerm())) return new HashSet<>();
        else return student
                .getGrades()
                .stream()
                .filter(grade -> Objects.nonNull(grade.getGrade()))
                .filter(grade -> grade.getCourse().getTerm().getTerm() == term.getTerm() - 1)
                .collect(Collectors.toSet());
    }

    private Set<Grade> unfinishedCourses(Student student) {
        return student
                .getGrades()
                .stream()
                .filter(grade -> Objects.isNull(grade.getGrade()))
                .filter(grade -> grade.getCourse().getTerm().equals(term))
                .collect(Collectors.toSet());
    }

    private Integer unitsPicked(Set<Grade> whateverSetOfGrades) {
        AtomicReference<Integer> unitsPicked = new AtomicReference<>(0);
        if (whateverSetOfGrades.size() > 0)
            whateverSetOfGrades.stream().filter(grade -> grade.getCourse() != null).forEach(grade -> unitsPicked.updateAndGet(v -> v + (grade.getCourse().getUnits())));
        return unitsPicked.get();
    }

    private Double gradeSum(Set<Grade> whateverSetOfGrades) {
        AtomicReference<Double> gradeSum = new AtomicReference<>(0.0);
        if (whateverSetOfGrades.size() > 0)
            whateverSetOfGrades.stream().filter(grade -> grade.getCourse() != null).forEach((grade) -> gradeSum.updateAndGet(v -> v + grade.getCourse().getUnits() * grade.getGrade()));
        return gradeSum.get();
    }

    private Integer unitsPassed(Set<Grade> whateverSetOfGrades) {
        AtomicReference<Integer> unitsPassed = new AtomicReference<>(0);
        if (whateverSetOfGrades.size() > 0)
            whateverSetOfGrades.stream().filter(grade -> grade.getCourse() != null).forEach((grade) -> unitsPassed.updateAndGet(v -> v + grade.getCourse().getUnits()));
        return unitsPassed.get();
    }
}
