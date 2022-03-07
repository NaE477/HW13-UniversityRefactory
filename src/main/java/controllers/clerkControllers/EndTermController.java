package controllers.clerkControllers;

import controllers.Utilities;
import org.hibernate.SessionFactory;
import services.TermService;

import java.util.Locale;
import java.util.Scanner;

public class EndTermController {
    private final Scanner sc = new Scanner(System.in);
    private final TermService termService;

    public EndTermController(SessionFactory sessionFactory) {
        termService = new TermService(sessionFactory);
    }

    public void endTerm() {
        Utilities.printGreed("Sure?(Y/N)");
        String yOrN = sc.nextLine().toUpperCase(Locale.ROOT);
        if (yOrN.equals("Y")) {
            termService.endTerm();
            Utilities.printGreed("Term Ended successfully.");
        } else {
            Utilities.printGreed("Cancelled.");
        }
    }
}
