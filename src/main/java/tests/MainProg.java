package tests;

import models.Session;
import models.User;
import services.SessionService;
import services.UserService;

import java.sql.Timestamp;

public class MainProg {
    public static void main(String[] args) {
        UserService us = new UserService();
        User user = new User("a","kli", "alaa@gmail.com","bk","organisateur");
        us.ajouter(user);
//        us.supprimer(2);
//        us.rechercher();
//        SessionService s = new SessionService();
////        Session ss = new Session(1,new Timestamp(System.currentTimeMillis()),null,true);
////        s.ajouter(ss);
////        Session sss = new Session(1, new Timestamp(System.currentTimeMillis()),false);
////        s.modifier(sss);
////          s.supprimer(4);
//          s.rechercher();
//          s.rechercherSessionPerUser(7);
    }

//        PersonneService ps = new PersonneService();
//
//        ps.ajouter(new Personne("Nabil","Mrah",10));
////        ps.modifier(new Personne(1,"Kawther","Ben Salem",30));
////        ps.supprimer(new Personne(3,"","",0));
//        ps.rechercher();
//

}