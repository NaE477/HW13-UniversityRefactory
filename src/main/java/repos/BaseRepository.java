package repos;

import lombok.Getter;
import models.users.User;
import org.hibernate.SessionFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;


public abstract class BaseRepository<T> implements Repository<T>{
    protected final SessionFactory sessionFactory;

    public BaseRepository(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }


    @Override
    public T ins(T t) {
        try(var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();
            try {
                session.save(t);
                transaction.commit();
                System.out.println("Data Inserted.");
                return t;
            } catch (Exception e) {
                e.printStackTrace();
                transaction.rollback();
                System.out.println("Database changes rolled back due to error.");
                return null;
            }
        }
    }

    @Override
    public void update(T t) {
        try(var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();
            try {
                session.update(t);
                transaction.commit();
                System.out.println("Edits saved.");
            } catch (Exception e) {
                e.printStackTrace();
                transaction.rollback();
                System.out.println("Database changes rolled back due to error.");
                throw e;
            }
        }
    }

    @Override
    public void delete(T t) {
        try(var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();
            try {
                session.delete(t);
                transaction.commit();
                System.out.println("Deleted.");
            } catch (Exception e) {
                e.printStackTrace();
                transaction.rollback();
                System.out.println("Database changes rolled back due to error.");
                throw e;
            }
        }
    }
}
