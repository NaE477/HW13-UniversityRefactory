package repos;

import models.things.Term;
import org.hibernate.SessionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TermRepository extends BaseRepository<Term> {

    public TermRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Integer read(){
        String readStmt = "SELECT term FROM term;";
        try {
            PreparedStatement ps = connection.prepareStatement(readStmt);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) return rs.getInt("term");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
