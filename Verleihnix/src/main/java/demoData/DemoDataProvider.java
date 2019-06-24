package demoData;

import entities.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.persistence.EntityManager;
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


    private final StringBuilder imgBase64HP1 = new StringBuilder();
    private final StringBuilder imgBase64HP2 = new StringBuilder();
    private final StringBuilder imgBase64ACDC1 = new StringBuilder();
    private final StringBuilder imgBase64ACDC2 = new StringBuilder();


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
        Insertion insertion1 = new Insertion(buecher,"Harry Potter und der Stein der Weisen","Ich kann dieses Buch nur empfehlen!",true,harryPotter1, 0.2);
        Insertion insertion2 = new Insertion(buecher,"Harry Potter und die Kammer des Schreckens","Leider fehlt hier das letzte Kapitel...",true,harryPotter2, 0.2);
        Insertion insertion3 = new Insertion(cds,"Beste CD von AC/DC","Nur per Abholung!!",true,acdc1, 0.3);
        Insertion insertion4 = new Insertion(cds,"Album von AC/DC","Auf der CD wird das dritte Lied leider nicht mehr richtig abgespielt.",true,acdc2, 0.4);

        //InsertionImages
        String b64Placeholder = "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAYGBgYHBgcICAcKCwoLCg8ODAwODxYQERAREBYiFRkVFRkVIh4kHhweJB42KiYmKjY+NDI0PkxERExfWl98fKcBBgYGBgcGBwgIBwoLCgsKDw4MDA4PFhAREBEQFiIVGRUVGRUiHiQeHB4kHjYqJiYqNj40MjQ+TERETF9aX3x8p//CABEIAEAAQAMBIQACEQEDEQH/xAAbAAACAwEBAQAAAAAAAAAAAAACAwABBQQGCP/aAAgBAQAAAAD6pWsqszVdjGhQP87sc16Sqdm9uTy+jVH5vZEdFW3JiUb4GRSRUaAXVMP/xAAUAQEAAAAAAAAAAAAAAAAAAAAA/9oACAECEAAAAAAAAAD/xAAUAQEAAAAAAAAAAAAAAAAAAAAA/9oACAEDEAAAAAAAAAD/xAA7EAACAQMBBAUHCgcAAAAAAAABAgMABBEFEiExURATFEFxFSIjJDKRsjM0U2FiZHWSscEWIHJzdJOh/9oACAEBAAE/AKkmWPA3ljwUcTQSd97ybA5Jx99G1tsDzCx7yxJrstvg4XZ5bJIrq7lfk5OsHJ+P5qimR8g5DDip49EsnVruGWJwo5moo9jJJy7e01dfbgkNMgI7iwFdog+mj/MK7RB9NH+YUCCAQcg1LCHUMDh13q3Lx8aifbU5GHG5l5GkG1PI/cnmL+/Rpum6fcC/knsoJH7fcDbdAzbnoaBo/fp9r/qWpNG0nq3I0213A8IlrRCPIelj7uvQxK3UcndIdhv2q1Pq6faJY/WSejRz6DUfxK4+OrqRobS5mUAskTsAeG4Z34qH+IJ7OOXrLBVljDfJyZG0P6qMeuaVpRAlsnS1tyQCkm0Qg+phUTF4YmIGWQE+Jq6+bse9cN7jVqfV0GfZyv8A3o0c+g1H8SuPjq+A8nagfu0nwmrEjydp4+7R/CK11dnR9Q/xpfhNW/zeH+2n6VdD1Zs8WIUDmSaTK3MkXASHbUfr0NosCyTNFe3sfWSNIwSXZXLHJwBUlgYLO/CXF1MzwSALI+3vweFWeq9VZW0T6fqGUiRWAt34qK1HUu0adewx6ff7ckLoubd+JFQZFvCDxCD9Kc7U8adyHbbx7qljDqMHDqcq3I1DKHBVhh13MOXh4/yzSbGAN7n2VqGPYXecsxyx5noliR8EZDDgw41t3Cn0iGQc04+40Ly3A9oq32gRiu1W+D55Y9wUE0XnfckewOb8fdUcKx5O8seLHiej/8QAFBEBAAAAAAAAAAAAAAAAAAAAUP/aAAgBAgEBPwAD/8QAFBEBAAAAAAAAAAAAAAAAAAAAUP/aAAgBAwEBPwAD/9k=";

        insertion1.setImage(b64Placeholder);
        insertion2.setImage(b64Placeholder);
        insertion3.setImage(b64Placeholder);
        insertion4.setImage(b64Placeholder);

        em.persist(insertion1);
        em.persist(insertion2);
        em.persist(insertion3);
        em.persist(insertion4);




        // InsertionStateCalendars
        InsertionRequest i1 = new InsertionRequest(
                insertion1,
                State.requested,
                getDateOfString("01.07.2019"),
                getDateOfString("15.07.2019"),
                System.currentTimeMillis(),
                this.alex.getId());
        InsertionRequest i2 = new InsertionRequest(
                insertion1,
                State.declined,
                getDateOfString("01.08.2019"),
                getDateOfString("15.08.2019"),
                System.currentTimeMillis(),
                this.alex.getId());
        InsertionRequest i3 = new InsertionRequest(
                insertion1,
                State.accepted,
                getDateOfString("03.09.2019"),
                getDateOfString("04.09.2019"),
                System.currentTimeMillis(),
                this.alex.getId());

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
