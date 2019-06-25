package services;

import entities.Pool;
import entities.User;
import proxies.PoolProxy;
import security.RequiresWebToken;

import javax.ejb.Stateless;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Rest-Service for the Pools
 */
@Path("/pool")
@Stateless
public class PoolService extends SuperService {


    /**
     * Create or update a Pool of the logged-in User
     * @param pp
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RequiresWebToken
    public Response editPool(@Valid PoolProxy pp) {
        String token = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        User u = jwtTokenService.getUserByToken(token);
        if(u == null){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        Pool p;
        if(pp.getId() == -1){
            p = new Pool(pp.getDescription(), u);
            em.persist(p);
            pp.setId(p.getId());
        }else{
            p =  em.find(Pool.class, pp.getId());
            if(p == null){
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            if(p.getUser() != u){
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            p.setDescription(pp.getDescription());
            em.persist(p);
        }
        return Response.status(Response.Status.OK).entity(pp).build();
    }

    /**
     * returns a Pool by given ID
     * @param id
     * @return
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RequiresWebToken
    public Response getPool(@PathParam("id") long id) {
        User user = this.getUserByHttpToken();
        if(user == null){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        Pool p = em.find(Pool.class, id);
        if(p == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if(p.getUser() != user){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.status(Response.Status.OK).entity(p).build();
    }

    /**
     * Returns a list of all pools of the logged-in User
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RequiresWebToken
    public Response getPools() {
        String token = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        User u = jwtTokenService.getUserByToken(token);
        if(u == null){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        List<Pool> pools = u.getPools();
        return Response.status(Response.Status.OK).entity(pools).build();
    }

    /**
     * Delete the pool by given ID
     * @param id
     * @return
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RequiresWebToken
    public Response deletePool(@PathParam("id") long id) {
        User user = this.getUserByHttpToken();
        if(user == null){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        Pool p = em.find(Pool.class, id);
        if(p == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if(p.getUser() != user){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        this.deletionHelper.deletePool(p);

        return Response.status(Response.Status.OK).build();
    }



}
