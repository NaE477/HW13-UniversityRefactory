package repos;

import models.things.Grade;
import org.hibernate.SessionFactory;

public class GradeRep extends BaseRepository<Grade> {
    public GradeRep(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
