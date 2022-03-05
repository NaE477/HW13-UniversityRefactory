package repos;

import models.users.ProfPosition;
import models.users.Professor;
import org.hibernate.SessionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProfessorRep extends BaseRepository<Professor> {
    public ProfessorRep(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Professor read(Integer id) {
        String readStmt = "SELECT * FROM professors WHERE prof_id = ?;";
        try {
            PreparedStatement ps = super.getConnection().prepareStatement(readStmt);
            ps.setInt(1,id);
            return mapTo(ps.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Professor read(String username){
        String readStmt = "SELECT * FROM professors WHERE prof_username = ?;";
        try {
            PreparedStatement ps = super.getConnection().prepareStatement(readStmt);
            ps.setString(1,username);
            return  mapTo(ps.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<Professor> readAll(){
        String readStmt = "SELECT * FROM professors;";
        try {
            PreparedStatement ps = super.getConnection().prepareStatement(readStmt);
            return mapToList(ps.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
