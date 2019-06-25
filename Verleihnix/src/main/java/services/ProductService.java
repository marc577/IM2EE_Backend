package services;

import entities.Insertion;
import entities.Product;
import entities.User;
import proxies.ProductOutProxy;
import proxies.ProductProxy;
import security.RequiresWebToken;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;


/**
 * Rest-Service for the Products
 */
@Path("/product")
public class ProductService extends SuperService{

    /**
     *
     * @return
     */
    @GET
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RequiresWebToken
    public Response get() {
        TypedQuery<Product> query = em.createQuery("SELECT p FROM Product p", Product.class);
        List<Product> products = query.getResultList();
        List<ProductOutProxy> results = new ArrayList<ProductOutProxy>();
        for(Product p : products) {
            ProductOutProxy po = new ProductOutProxy();
            po.setId(p.getId());
            po.setDescription(p.getDescription());
            po.setTitle(p.getTitle());
            po.setInsertions(p.getInsertions());


            double minPrice = 0;
            for (Insertion i : p.getInsertions()) {
                if(po.getImage() == null){
                    po.setImage(i.getImage());
                }
                minPrice = (minPrice == 0) ? i.getPricePerDay() : 0;
                minPrice = (i.getPricePerDay() < minPrice ) ? i.getPricePerDay() : minPrice;
            }
            po.setMinPricePerDay(minPrice);
            results.add(po);
        }
        return Response.status(Response.Status.OK).entity(results).build();
    }

    /**
     *
     * @param id
     * @return
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RequiresWebToken
    public Response getProduct(@PathParam("id") long id) {
        Product p = em.find(Product.class, id);
        if(p == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        ProductProxy pp = new ProductProxy();
        pp.setId(p.getId());
        pp.setTitle(p.getTitle());
        pp.setDescription(p.getDescription());
        pp.setInsertions(p.getInsertions());
        for (Insertion i : p.getInsertions()) {
            if(pp.getImage() == null){
                pp.setImage(i.getImage());
            }else{
                break;
            }
        }
        return Response.status(Response.Status.OK).entity(pp).build();
    }

    /**
     *
     * @param id
     * @return
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RequiresWebToken
    public Response deleteProduct(@PathParam("id") long id) {
        String token = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        User u = jwtTokenService.getUserByToken(token);
        if(u == null){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        Product product = em.find(Product.class, id);
        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        this.deletionHelper.deleteProduct(product);
        return Response.status(Response.Status.OK).build();
    }

}

