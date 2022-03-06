package controllers.clerkControllers;

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
        System.out.println("Sure?(Y/N)");
        String yOrN = sc.nextLine().toUpperCase(Locale.ROOT);
        if (yOrN.equals("Y")) {
            termService.endTerm();
            System.out.println("Term Ended successfully.");
        } else {
            System.out.println("Cancelled.");
        }
    }
}
