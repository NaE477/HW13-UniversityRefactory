package repos;

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
}
