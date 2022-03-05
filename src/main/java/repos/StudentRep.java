package repos;

import models.things.Course;
import models.users.Student;
import org.hibernate.SessionFactory;

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

    @Override
    public Student read(Integer id) {
        String readStmt = "SELECT * FROM students WHERE student_id = ?;";
        try {
            PreparedStatement ps = super.getConnection().prepareStatement(readStmt);
            ps.setInt(1, id);
            return mapTo(ps.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Student read(String username) {
        String readStmt = "SELECT * FROM students WHERE student_username = ?;";
        try {
            PreparedStatement ps = super.getConnection().prepareStatement(readStmt);
            ps.setString(1, username);
            return mapTo(ps.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Student> readAll() {
        String readStmt = "SELECT * FROM students;";
        try {
            PreparedStatement ps = super.getConnection().prepareStatement(readStmt);
            return mapToList(ps.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Student> readAll(Course course){
        String readStmt = "SELECT students.* FROM students " +
                "INNER JOIN course_to_student cts on students.student_id = cts.student_id " +
                "WHERE cts.course_id = ?;";
        try {
            PreparedStatement ps = super.getConnection().prepareStatement(readStmt);
            ps.setInt(1,course.getId());
            return mapToList(ps.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
