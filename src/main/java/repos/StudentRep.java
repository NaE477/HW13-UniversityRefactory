package repos;

import models.things.Course;
import models.users.Clerk;
import models.users.Student;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentRep extends BaseRepository<Student> {
    public StudentRep(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Student read(Integer id) {
        try (var session = sessionFactory.openSession()) {
            try {
                return session.get(Student.class, id);
            } catch (Exception e) {
                return null;
            }
        }
    }

    public Student read(String username) {
        try (var session = sessionFactory.openSession()) {
            try {
                CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
                var criteriaQuery = criteriaBuilder.createQuery(Student.class);
                var root = criteriaQuery.from(Student.class);
                var query = criteriaQuery
                        .select(root)
                        .where(criteriaBuilder.equal(root.get("username"),username));
                return session.createQuery(query).getSingleResult();
            } catch (Exception e) {
                return null;
            }
        }
    }

    public List<Student> readAll() {
        try (var session = sessionFactory.openSession()) {
            try {
                CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
                var criteriaQuery = criteriaBuilder.createQuery(Student.class);
                var root = criteriaQuery.from(Student.class);
                var query = criteriaQuery
                        .select(root);
                return session.createQuery(query).list();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public List<Student> readAll(Course course){
        try (var session = sessionFactory.openSession()) {
            try {
                return session
                        .createQuery("select s from Student s left join fetch s.grades g where g.course.id = :courseId",Student.class)
                        .setParameter("courseId",course.getId())
                        .list();
            } catch (Exception e) {
                return null;
            }
        }
    }
}
