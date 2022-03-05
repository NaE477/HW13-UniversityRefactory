package services;

import models.things.Term;
import org.hibernate.SessionFactory;
import repos.TermRepository;

import java.sql.Connection;

public class TermService {
    private final TermRepository termRepository;
    public TermService(SessionFactory sessionFactory){
        termRepository = new TermRepository(sessionFactory);
    }
    public void endTerm(){
        Term oldTerm = getCurrentTerm();
        Term termToUpdate = new Term(0,oldTerm.getTerm() + 1,null);
        termRepository.ins(termToUpdate);
    }
    public Term getCurrentTerm(){
        return termRepository.read();
    }
}
