package repos;

import models.users.Clerk;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClerkRep extends BaseRepository<Clerk> {
    public ClerkRep(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Clerk read(Integer id) {
        try (var session = sessionFactory.openSession()) {
            try {
                return session.get(Clerk.class, id);
            } catch (Exception e) {
                return null;
            }
        }
    }

    public Clerk read(String username) {
        try (var session = sessionFactory.openSession()) {
            try {
                CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
                var criteriaQuery = criteriaBuilder.createQuery(Clerk.class);
                var root = criteriaQuery.from(Clerk.class);
                var query = criteriaQuery
                        .select(root)
                        .where(criteriaBuilder.equal(root.get("username"),username));
                return session.createQuery(query).getSingleResult();
            } catch (Exception e) {
                return null;
            }
        }
    }

    public List<Clerk> readAll() {
        try (var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();
            try {
                CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
                var criteriaQuery = criteriaBuilder.createQuery(Clerk.class);
                var root = criteriaQuery.from(Clerk.class);
                var query = criteriaQuery
                        .select(root);
                return session.createQuery(query).list();
            } catch (Exception e) {
                e.printStackTrace();
                transaction.rollback();
                System.out.println("Database changes rolled back due to error.");
                throw e;
            }
        }
    }
}
