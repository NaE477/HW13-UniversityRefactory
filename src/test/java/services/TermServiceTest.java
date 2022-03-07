package services;

import models.things.Term;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TermServiceTest {
    private static TermService termService;
    @BeforeAll
    static void initiate() {
        SessionFactory sessionFactory = SessionFactorySingletonTest.getInstance();
        termService = new TermService(sessionFactory);
    }

    @Test
    void sessionFactoryTest() {
        assertDoesNotThrow(() -> {
            SessionFactory sessionFactory = SessionFactorySingletonTest.getInstance();
            TermService termService = new TermService(sessionFactory);
            var size = termService.findAll().size();
        });
    }

    @Test
    void initiateTest() {
        //Arrange
        Term toInitiate = new Term(0,14001,null);
        //Act
        var toIns = termService.initiate(toInitiate);
        //Assert
        assertNotNull(toIns);
        assertEquals(termService.findFirstTerm(),toIns);
        assertEquals(termService.findCurrentTerm(),toIns);
    }

    @Test
    void endTerm() {
        //Arrange
        Term firstTerm = new Term(0,14001,null);
        termService.initiate(firstTerm);
        //Act
        termService.endTerm();
        //Assert
        assertEquals(firstTerm.getTerm() + 1,termService.findCurrentTerm().getTerm());
    }

    @Test
    void findCurrentTerm() {
        //Arrange
        Term firstTerm = new Term(0,14001,null);
        termService.initiate(firstTerm);
        termService.endTerm();
        termService.endTerm();
        //Act
        Term currentTerm = termService.findCurrentTerm();
        //Assert
        assertEquals(firstTerm.getTerm() + 2,currentTerm.getTerm());
    }

    @Test
    void findFirstTerm() {
        //Arrange
        Term firstTerm = new Term(0,14001,null);
        termService.initiate(firstTerm);
        termService.endTerm();
        termService.endTerm();
        //Act
        Term firstTermToFind = termService.findFirstTerm();
        //Assert
        assertEquals(firstTerm.getTerm(),firstTermToFind.getTerm());
        assertEquals(firstTerm,firstTermToFind);
    }

    @Test
    void findAll() {
        //Arrange
        Term firstTerm = new Term(0,14001,null);
        termService.initiate(firstTerm);
        termService.endTerm();
        termService.endTerm();
        //Act
        List<Term> terms = termService.findAll();
        //Assert
        assertNotNull(terms);
        assertEquals(3,terms.size());
    }
}