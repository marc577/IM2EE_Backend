package entities;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

public class EntityHelper {

    @PersistenceContext
    private static EntityManager em;

    @Transactional
    public static void deleteEntity(IEntity iEntity, long id) {
        IEntity entity = em.find(iEntity.getClass() ,id);
        em.remove(entity);
    }

    public static void deleteInsertion(Insertion insertion) {
        Insertion insertion2Delete = em.find(Insertion.class, insertion.getId());
        em.remove(insertion);
        em.flush();
    }

}
