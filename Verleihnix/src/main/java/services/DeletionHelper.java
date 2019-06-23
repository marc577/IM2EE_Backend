package services;

import entities.*;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@ApplicationScoped
public class DeletionHelper {

    @PersistenceContext
    EntityManager em;

    @Transactional
    public void deleteChatEntry(ChatEntry chatEntry) {
        // delete from ChatEntry (id)
        em.createQuery("DELETE FROM ChatEntry c WHERE c.id = :id")
                .setParameter("id", chatEntry.getId())
                .executeUpdate();
    }

    @Transactional
    public void deleteInsertion(Insertion insertion) {
        // delete from InsertionRequest (insertion_id)
        for(InsertionRequest insertionRequest : insertion.getInsertionRequests()) {
            deleteInsertionRequests(insertionRequest);
        }
        // delete from Insertion (id)
        em.createQuery("DELETE FROM Insertion i WHERE i.id = :id")
                .setParameter("id", insertion.getId())
                .executeUpdate();
    }

    @Transactional
    public void deleteInsertionRequests(InsertionRequest insertionRequest) {
        // delete from ChatEntry(insertionRequest_id)
        em.createQuery("DELETE FROM ChatEntry c WHERE c.insertionRequest = :id")
                .setParameter("id", insertionRequest)
                .executeUpdate();

        // delete from InsertionRequest (id)
        em.createQuery("DELETE from InsertionRequest isc where isc.id = :id")
                .setParameter("id", insertionRequest.getId())
                .executeUpdate();
    }

    @Transactional
    public void deletePool(Pool pool) {
        // delete from Insertion (pool_id)
        em.createQuery("DELETE FROM Insertion i WHERE i.pool = :id")
                .setParameter("id", pool)
                .executeUpdate();

        // delete from Pool (id)
        em.createQuery("DELETE FROM Pool p WHERE p.id = :id")
                .setParameter("id", pool.getId())
                .executeUpdate();
    }

    @Transactional
    public void deleteProduct(Product product) {
        // delete from insertion(product_id)
        em.createQuery("DELETE FROM Insertion i WHERE i.product = :id")
                .setParameter("id", product)
                .executeUpdate();

        // delete from Product (id)
        em.createQuery("DELETE FROM Product p WHERE p.id = :id")
                .setParameter("id", product.getId())
                .executeUpdate();
    }

    @Transactional
    public void deleteUser(User user) {

        // delete from ChatEntry (sederId)
        em.createQuery("DELETE FROM ChatEntry c WHERE c.senderId = :id")
                .setParameter("id", user.getId())
                .executeUpdate();

        // delete from InsertionRequest (requesterId)
        em.createQuery("DELETE FROM InsertionRequest ir WHERE ir.requesterId = :id")
                .setParameter("id", user.getId())
                .executeUpdate();

        // delete from Pool (user_id)
        em.createQuery("DELETE FROM Pool p WHERE p.user = :id")
                .setParameter("id", user)
                .executeUpdate();
        // delete from User (id)
        em.createQuery("DELETE FROM User u WHERE u.id = :id")
                .setParameter("id", user.getId())
                .executeUpdate();

    }

}
