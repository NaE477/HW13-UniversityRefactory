package services;

import models.users.Professor;
import org.hibernate.SessionFactory;
import repos.ProfessorRep;

import java.util.List;

public class ProfessorService extends BaseService{

    private final ProfessorRep professorRep;

    public ProfessorService(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.professorRep = new ProfessorRep(super.getSessionFactory());
    }

    public Professor signUpProfessor(Professor professor){
        return professorRep.ins(professor);
    }
    public Professor find(Integer professorId){
        return professorRep.read(professorId);
    }
    public Professor find(String username){
        return professorRep.read(username);
    }
    public List<Professor> findAll(){
        return professorRep.readAll();
    }
    public void editProfile(Professor professor){
        professorRep.update(professor);
    }
    public void deleteProfessor(Professor professor){
        professorRep.delete(professor);
    }

}
