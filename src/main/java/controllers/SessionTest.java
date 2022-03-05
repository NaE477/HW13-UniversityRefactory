package controllers;

import org.hibernate.SessionFactory;

public class SessionTest {
    public static void main(String[] args) {
        SessionFactory sessionFactory = SessionFactorySingleton.getInstance();
    }
}
