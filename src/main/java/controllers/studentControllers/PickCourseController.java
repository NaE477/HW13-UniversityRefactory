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
        List<Course> allCourses = courseService.findAll(term);

        List<Grade> reportCard = gradeService.findAllByStudent(student);

        List<Course> pickedCourses = reportCard
                .stream()
                .map(Grade::getCourse)
                .collect(Collectors.toList());

        allCourses.forEach(System.out::println);
        Utilities.printGreen("Enter Course ID you want to pick:");
        Integer courseToPickId = Utilities.intReceiver();

        Course courseToPick = allCourses
                .stream()
                .filter(c -> Objects.equals(c.getId(), courseToPickId))
                .findAny()
                .orElse(null);

        if (courseToPick != null) {
            if (!pickedCourses.contains(courseToPick)) {
                if (canPickByThreshold(courseToPick)) {
                    Grade grade = gradeService.pickCourse(new Grade(0, student, courseToPick, null));
                    if (grade != null) Utilities.printGreen(grade.getCourse().getCourseName() + " picked successfully");
                    else Utilities.printGreen("Something went wrong with database.");
                } else Utilities.printGreen("You Can't Pick this course.Your unit threshold is filled.");
            } else Utilities.printGreen("Course Already Picked");
        } else Utilities.printGreen("Wrong ID");
    }

    private Boolean canPickByThreshold(Course courseToPick) {
        Set<Grade> finishedCoursesLastTerm = finishedCourses(student);

        Set<Grade> unfinishedCoursesThisTerm = unfinishedCourses(student);

        Integer unitsPicked = unitsPicked(unfinishedCoursesThisTerm);

        Double gradeSum = gradeSum(finishedCoursesLastTerm);

        Integer unitsPassed = unitsPassed(finishedCoursesLastTerm);

        double averageGrade;
        try {
            averageGrade = gradeSum / unitsPassed;
        } catch (ArithmeticException e) {
            averageGrade = 0.0;
        }
        int pickingThreshold;

        int unitToBePicked = unitsPicked + courseToPick.getUnits();

        if (finishedCoursesLastTerm.size() == 0) pickingThreshold = 20;
        else if (averageGrade > 18) pickingThreshold = 24;
        else if (averageGrade != 0 && averageGrade < 14) pickingThreshold = 12;
        else pickingThreshold = 20;

        return unitToBePicked <= pickingThreshold;
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
