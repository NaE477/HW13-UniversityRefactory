package repos;

import models.things.Course;
import models.things.Grade;
import models.users.Student;
import org.hibernate.SessionFactory;

import java.util.List;

public class GradeRep extends BaseRepository<Grade> {
    public GradeRep(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    public List<Grade> readAllByStudent(Student student) {
        return null;
    }

    public Grade read(Student student, Course course) {
        try(var session = sessionFactory.openSession()) {
            return session
                    .createQuery("select g from Grade g where g.course.id = :cId and g.student.id = :sId",Grade.class)
                    .setParameter("cId",course.getId())
                    .setParameter("sId",student.getId())
                    .getSingleResult();
        }
    }
}
