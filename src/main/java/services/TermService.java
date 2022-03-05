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
    public void endTerm(Term term){
        termRepository.update(term);
    }
    public Term getCurrentTerm(){
        return termRepository.read();
    }
}
