package demoData;

import entities.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Startup
@Singleton
public class DemoDataProvider {

    @Resource
    private TimerService timerService;

    @PersistenceContext
    private EntityManager em;

    private User daniel;
    private User alex;
    private User marc;

    private void createInitialDemoData() throws IOException {
        final Long userCount = (Long) em.createQuery("SELECT COUNT(u) FROM User u").getSingleResult();
        if (userCount.longValue() == 0) {
            this.daniel = new User("Daniel","Abel","danielabel@mail.de","passDan");
            this.alex = new User("Alexander","Krieg","alexanderkrieg@mail.de","passAle");
            this.marc = new User("Marc","Reinke","marcreinke@mail.de","passMar");

            em.persist(daniel);
            em.persist(alex);
            em.persist(marc);

            this.addDemoData4UserDaniel(daniel);
            this.addDemoData4UserAlex(alex);
            this.addDemoData4UserMarc(marc);

        }
    }

    private void addDemoData4UserDaniel(User daniel) {

        // Pools
        Pool buecher = new Pool("Meine Bücher",daniel);
        Pool cds = new Pool("Meine CD-Sammlung",daniel);
        em.persist(buecher);
        em.persist(cds);

        // Products
        Product harryPotter1 = new Product("Harry Potter 1", "Der Stein der Weisen");
        Product harryPotter2 = new Product("Harry Potter 2", "Kammer des Schreckens");
        Product acdc1 = new Product("AC/DC - Black Ice", "Album, 2008");
        Product acdc2 = new Product("AC/DC - Highway to Hell", "Album, 1979");
        em.persist(harryPotter1);
        em.persist(harryPotter2);
        em.persist(acdc1);
        em.persist(acdc2);

        // Insertions
        Insertion insertion1 = new Insertion(buecher,"Ich kann dieses Buch nur empfehlen!",null,true,harryPotter1);
        Insertion insertion2 = new Insertion(buecher,"Leider fehlt hier das letzte Kapitel...",null,true,harryPotter2);
        Insertion insertion3 = new Insertion(buecher,"Nur per Abholung!!",null,true,acdc1);
        Insertion insertion4 = new Insertion(buecher,"Auf der CD wird das dritte Lied leider nicht mehr richtig abgespielt.",null,true,acdc2);
        em.persist(insertion1);
        em.persist(insertion2);
        em.persist(insertion3);
        em.persist(insertion4);

        // InsertionStateCalendars
        InsertionStateCalendar i1 = new InsertionStateCalendar(
                insertion1,State.requested,
                getDateOfString("01.07.2019"),
                getDateOfString("15.07.2019"),
                "Ich kann es abholen",
                "",
                new Date(System.currentTimeMillis()),
                this.alex);
        InsertionStateCalendar i2 = new InsertionStateCalendar(
                insertion1,State.declined,
                getDateOfString("01.08.2019"),
                getDateOfString("15.08.2019"),
                "Würde mich echt freuen, wenn das klappen würde :)",
                "Sorry, aber da wollte ich es selber lesen.",
                new Date(System.currentTimeMillis()),
                this.alex);
        InsertionStateCalendar i3 = new InsertionStateCalendar(
                insertion1,State.accepted,
                getDateOfString("03.09.2019"),
                getDateOfString("04.09.2019"),
                "Ich kann es abholen",
                "Jo, geht klar. Aber ich schätze du brauchst länger als einen Tag, oder? Wenn ja, dann reserviere es einfach etwas länger.",
                new Date(System.currentTimeMillis()),
                this.alex);

        em.persist(i1);
        em.persist(i2);
        em.persist(i3);

    }

    private Date getDateOfString(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void addDemoData4UserAlex(User alex) {

    }

    private void addDemoData4UserMarc(User marc) {

    }

    @PostConstruct
    private void init() {
        timerService.createSingleActionTimer(1, new TimerConfig("ddp", false));
    }

    /**
     * Create demo data, if database is empty
     */
    @Timeout
    private void timer() {
        try {
            createInitialDemoData();
        } catch (final IOException ignore) {
            // Exception cannot throw
        }
    }

}
