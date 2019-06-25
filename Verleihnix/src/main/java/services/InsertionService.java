package services;

import entities.*;
import proxies.InsertionOutProxy;
import proxies.InsertionProxy;
import security.RequiresWebToken;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Rest-Service for the Chats
 */
@Path("/insertion")
public class InsertionService extends SuperService {

    /**
     * Create or update an Insertion
     * @param poolId
     * @param insertionProxy
     * @return
     */
    @POST
    @Transactional
    @Path("/{poolId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RequiresWebToken
    public Response editInsertion(@PathParam("poolId") long poolId, @Valid InsertionProxy insertionProxy) {
        User user = this.getUserByHttpToken();
        if(user == null){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        Insertion insertion;
        InsertionOutProxy insertionOutProxy = new InsertionOutProxy();
        if(insertionProxy.getId() == -1){
            try {
                Pool pool = this.findPool(poolId, user);
                Product product = this.findProduct(insertionProxy.getProduct().getId(), user, insertionProxy.getProduct().getTitle(), insertionProxy.getProduct().getTitle());
                insertion = new Insertion(pool, insertionProxy.getTitle(), insertionProxy.getDescription(), true, product, insertionProxy.getPricePerDay());
                em.persist(insertion);
                em.flush();
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
                insertion.setPool(this.findPool(poolId, user));
                insertion.setTitle(insertionProxy.getTitle());
                insertion.setDescription(insertionProxy.getDescription());
                insertion.setActive(insertionProxy.isActive());
                insertion.setPricePerDay(insertionProxy.getPricePerDay());
                insertion.setProduct(this.findProduct(insertionProxy.getProduct().getId(), user, insertionProxy.getProduct().getTitle(), insertionProxy.getProduct().getDescription()));
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

    /**
     * returns all insertions of a pool
     * @param poolId
     * @return
     */
    @GET
    @Path("insertionPool/{poolId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RequiresWebToken
    public Response getInsertions(@PathParam("poolId") long poolId) {

        try {
            User user = getUserByHttpToken();
            List<InsertionOutProxy> insertionOutProxies = new ArrayList<InsertionOutProxy>();
            Pool pool = this.findPool(poolId, user);
            InsertionOutProxy insertionOutProxy;
            for (Insertion insertion : pool.getInsertions()) {
                insertionOutProxy = new InsertionOutProxy();
                insertionOutProxy.setInsertion(insertion);
                insertionOutProxy.setProduct(insertion.getProduct());
                insertionOutProxies.add(insertionOutProxy);
            }
            return Response.status(Response.Status.OK).entity(insertionOutProxies).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (NotAuthorizedException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    /**
     * Returns an Insertion by given ID
     * @param insertionId
     * @return
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RequiresWebToken
    public Response getInsertion(@PathParam("id") long insertionId) {

        try {
            User user = getUserByHttpToken();
            Insertion insertion = this.findInsertion(insertionId, user);
            InsertionOutProxy insertionOutProxy = new InsertionOutProxy();
            insertionOutProxy.setInsertion(insertion);
            insertionOutProxy.setProduct(insertion.getProduct());
            return Response.status(Response.Status.OK).entity(insertionOutProxy).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (NotAuthorizedException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RequiresWebToken
    public Response deleteInsertion(@PathParam("id") long insertionId) {
        try {
            User user = getUserByHttpToken();
            Insertion insertion = this.findInsertion(insertionId, user);
            this.deletionHelper.deleteInsertion(insertion);
            return Response.status(Response.Status.OK).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (NotAuthorizedException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }


    /**
     * Load user-image to an Insertion
     * @param id
     * @param imageBase64Data
     * @return
     */
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/image/{id}")
    @Transactional
    @RequiresWebToken
    public Response addByUserId(@PathParam("id") long id, final String imageBase64Data) {
        if (imageBase64Data == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        // check size
        final double imageSize = imageBase64Data.length() * 0.75 / 1000;

        if (imageSize > 2048 || imageSize == 0) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        String contentType = "";
        final Pattern mime = Pattern.compile("^data:([a-zA-Z0-9]+/[a-zA-Z0-9]+).*,.*");
        final Matcher matcher = mime.matcher(imageBase64Data);
        if (matcher.find()) {
            contentType = matcher.group(1).toLowerCase(Locale.ENGLISH);
        }

        // regex path
        final Pattern contentTypePattern = Pattern.compile("^image/((jpeg)|(jpg)|(png))$");
        final Matcher contentTypeMatcher = contentTypePattern.matcher(contentType);

        if (!contentTypeMatcher.find() || contentTypeMatcher.groupCount() == 0) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }

        Insertion i = em.find(Insertion.class, id);
        User u = this.getUserByHttpToken();
        if(i.getPool().getUser() != u){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        i.setImage(imageBase64Data);
        try{
            em.persist(i);
        }catch (final Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }



        return Response.status(Response.Status.OK).build();
    }

    private Insertion findInsertion(long insertionId, User u) {
        Insertion insertion = em.find(Insertion.class, insertionId);
        if(insertion == null){
            throw new NotFoundException();
        }
        if(insertion.getPool().getUser().getId() != u.getId()){
            throw new NotAuthorizedException(Response.Status.UNAUTHORIZED);
        }
        return insertion;
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
