package demoData;

import entities.DeviceBasic;
import entities.DeviceElement;
import entities.Pool;
import entities.User;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import java.io.IOException;

@Startup
@Singleton
public class DemoDataProvider {

    @Resource
    private TimerService timerService;

    @PersistenceContext
    private EntityManager em;

    private void createInitialDemoData() throws IOException {
        final Long userCount = (Long) em.createQuery("SELECT COUNT(u) FROM User u").getSingleResult();
        if (userCount.longValue() == 0) {
            User daniel = new User("Daniel","Abel","danielabel@mail.de","passDan");
            User alex = new User("Alexander","Krieg","alexanderkrieg@mail.de","passAle");
            User marc = new User("Marc","Reinke","marcreinke@mail.de","passMar");

            em.persist(daniel);
            em.persist(alex);
            em.persist(marc);

            this.addDemoData4UserDaniel(daniel);
            this.addDemoData4UserAlex(alex);
            this.addDemoData4UserMarc(marc);

        }
    }

    private void addDemoData4UserDaniel(User daniel) {
        Pool buecher = new Pool("Bücher",daniel);
        Pool cds = new Pool("CDs",daniel);

        em.persist(buecher);
        em.persist(cds);

        DeviceBasic herrDerRinge = new DeviceBasic("Herr der Ringe",buecher);
        DeviceBasic harryPotter = new DeviceBasic("Harry Potter",buecher);

        em.persist(herrDerRinge);
        em.persist(harryPotter);

        DeviceBasic acdc = new DeviceBasic("ACDC",cds);
        DeviceBasic metallica = new DeviceBasic("Metallica",cds);

        em.persist(acdc);
        em.persist(metallica);

        for (int i=0; i<5; i++) {
            DeviceElement deviceElement = new DeviceElement(acdc);
            em.persist(deviceElement);
        }

        for (int i=0; i<10; i++) {
            DeviceElement deviceElement = new DeviceElement(metallica);
            em.persist(deviceElement);
        }

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
