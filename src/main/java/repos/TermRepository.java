package repos;

import models.things.Term;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

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
    public List<Term> readAll() {
        try (var session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            var criteriaQuery = criteriaBuilder.createQuery(Term.class);
            var root = criteriaQuery.from(Term.class);
            var query = criteriaQuery.select(root);
            return session.createQuery(criteriaQuery).list();
        }
    }

    @Override
    public void update(Term term) {}

    @Override
    public void delete(Term term) {}
}
