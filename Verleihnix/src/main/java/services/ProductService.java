package services;

import entities.Product;
import proxies.ProductProxy;
import security.RequiresWebToken;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/product")
public class ProductService extends SuperService{

    /*@POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RequiresWebToken
    public Response edit(@Valid UserProxy userProxy) {
        try {
            User user = this.getUserByHttpToken();
            User uneu = em.find(User.class, user.getId());
            if(user.getId() == uneu.getId()){
                uneu.setFirstName(userProxy.getFirstName());
                uneu.setLastName(userProxy.getLastName());
                if (!uneu.getEmail().equals(userProxy.getEmail())) {
                    if(!this.emailExists(userProxy.getEmail())) {
                        uneu.setEmail(userProxy.getEmail());
                    } else {
                        return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Email is already in use").build();
                    }

                }
                em.persist(uneu);
                return Response.status(Response.Status.OK).entity(userProxy).build();
            }else{
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        } catch (NotAuthorizedException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }*/

    @GET
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RequiresWebToken
    public Response get() {
        TypedQuery<Product> query = em.createQuery("SELECT p FROM Product p", Product.class);
        List<Product> results = query.getResultList();
        return Response.status(Response.Status.OK).entity(results).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RequiresWebToken
    public Response getPool(@PathParam("id") long id) {
        Product p = em.find(Product.class, id);
        if(p == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        ProductProxy pp = new ProductProxy();
        pp.setId(p.getId());
        pp.setTitle(p.getTitle());
        pp.setDescription(p.getDescription());
        pp.setInsertions(p.getInsertions());
        return Response.status(Response.Status.OK).entity(pp).build();
    }

}

