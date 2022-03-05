package services;

import models.things.Course;
import models.things.Grade;
import models.users.Student;
import org.hibernate.SessionFactory;
import repos.GradeRep;

import java.util.List;

public class GradeService extends BaseService{
    private GradeRep gradeRep;
    public GradeService(SessionFactory sessionFactory) {
        super(sessionFactory);
        gradeRep = new GradeRep(super.getSessionFactory());
    }

    public List<Grade> findAllByStudent(Student student) {
        return gradeRep.readAllByStudent(student);
    }
}
