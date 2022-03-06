package repos;

import models.things.Term;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TermRepository extends BaseRepository<Term> {

    public TermRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Term read() {
        try (var session = sessionFactory.openSession()) {
            return session.createNamedQuery("getLastTerm", Term.class).getSingleResult();
        }
    }
    public Term readFirst() {
        try (var session = sessionFactory.openSession()) {
            return session.createNamedQuery("getFirstTerm", Term.class).getSingleResult();
        }
    }
}
