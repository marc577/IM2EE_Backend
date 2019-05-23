package services;

import entities.Insertion;
import entities.Pool;
import entities.Product;
import entities.User;
import proxies.InsertionOutProxy;
import proxies.InsertionProxy;
import security.RequiresWebToken;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.util.List;

@Path("/insertion")
public class InsertionService extends SuperService {

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RequiresWebToken
    public Response editInsertion(@Valid InsertionProxy insertionProxy) {
        User user = this.getUserByHttpToken();
        if(user == null){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        Insertion insertion;
        InsertionOutProxy insertionOutProxy = new InsertionOutProxy();
        if(insertionProxy.getId() == -1){
            try {
                Pool pool = this.findPool(insertionProxy.getPoolId(), user);
                Product product = this.findProduct(insertionProxy.getProductId(), user, insertionProxy.getProductTitle(), insertionProxy.getProductDescription());
                insertion = new Insertion(pool, insertionProxy.getInsertionTitle(), insertionProxy.getInsertionDescription(), true, product);
                em.persist(insertion);
                insertionOutProxy.setInsertion(insertion);
                insertionOutProxy.setProduct(insertion.getProduct());
            } catch (IllegalArgumentException e){
                return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Product already exists").build();
            } catch (NotFoundException e) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } catch (NotAuthorizedException e) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        }else{
            insertion =  em.find(Insertion.class, insertionProxy.getId());
            if(insertion == null){
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            if(insertion.getPool().getUser().getId() != user.getId()){
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            try {
                insertion.setPool(this.findPool(insertionProxy.getPoolId(), user));
                insertion.setTitle(insertionProxy.getInsertionTitle());
                insertion.setDescription(insertionProxy.getInsertionDescription());
                insertion.setImage(insertionProxy.getImage());
                insertion.setActive(insertionProxy.isActive());
                insertion.setProduct(this.findProduct(insertionProxy.getProductId(), user, insertionProxy.getProductTitle(), insertionProxy.getProductDescription()));
                em.persist(insertion);
                insertionOutProxy.setInsertion(insertion);
                insertionOutProxy.setProduct(insertion.getProduct());
            } catch (NotAcceptableException e) {
                return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Product title is missing").build();
            } catch (IllegalArgumentException e){
                return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Product already exists").build();
            } catch (NotFoundException e) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } catch (NotAuthorizedException e) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        }
        return Response.status(Response.Status.OK).entity(insertionOutProxy).build();
    }

    private Product findProduct(long productId, User u, String productTitle, String productDescription) throws NotAcceptableException, IllegalArgumentException {

        Product product = em.find(Product.class, productId);
        if(product == null){
            if (productTitle==null) {
                throw new NotAcceptableException();
            }
            if (this.productExists(productTitle)) {
                throw new IllegalArgumentException();
            }
            product = new Product(productTitle, productDescription);
            em.persist(product);
        }
        return product;
    }

    private boolean productExists(final String productTitle) {
        List resultList = em.createNativeQuery("SELECT COUNT(*) " +
                "FROM product " +
                "WHERE title = ?")
                .setParameter(1,productTitle)
                .getResultList();
        return (resultList.get(0) != BigInteger.valueOf(0));
    }

    private Pool findPool(long poolId, User u) throws NotFoundException, NotAuthorizedException {

        Pool pool = em.find(Pool.class, poolId);
        if(pool == null){
            throw new NotFoundException();
        }
        if(pool.getUser().getId() != u.getId()){
            throw new NotAuthorizedException(Response.Status.UNAUTHORIZED);
        }
        return pool;
    }
}
