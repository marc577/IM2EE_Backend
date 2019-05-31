package services;

import entities.Insertion;
import entities.InsertionRequest;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ApplicationScoped
public class DeletionHelper {

    @PersistenceContext
    EntityManager em;

    public void deleteInsertion(Insertion insertion) {
        for(InsertionRequest insertionRequest : insertion.getInsertionRequests()) {
            deleteInsertionRequests(insertionRequest);
        }
        em.createQuery("DELETE from Insertion i where i.id = :id").setParameter("id", insertion.getId()).executeUpdate();
    }

    public void deleteInsertionRequests(InsertionRequest insertionRequest) {
        em.createQuery("DELETE from InsertionRequest isc where isc.id = :id")
                .setParameter("id", insertionRequest.getId())
                .executeUpdate();
    }

}
