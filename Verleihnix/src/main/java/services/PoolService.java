package services;

import entities.Pool;
import entities.User;
import proxies.PoolProxy;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/pool")
@Stateless
public class PoolService {

    @PersistenceContext
    EntityManager em;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	@Transactional
    public Response editPool(@Valid PoolProxy pp) {
        //TODO: Benutzer from JSON TOKEN
        long uid = 1;
        User u = em.find(User.class,uid);
        if(u == null){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        Pool p;
        if(pp.getId() == -1){
            p = new Pool(pp.getDescription(), u);
            em.persist(p);
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

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
	@Transactional
    public Response getPool(@PathParam("id") long id) {
        //TODO: Benutzer from JSON TOKEN
        long uid = 1;
        User u = em.find(User.class,uid);
        if(u == null){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        Pool p = em.find(Pool.class, id);
        if(p == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if(p.getUser() != u){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.status(Response.Status.OK).entity(p).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
	@Transactional
    public Response getPools() {
        //TODO: Benutzer from JSON TOKEN
        long uid = 1;
        User u = em.find(User.class,uid);
        if(u == null){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        List<Pool> pools = u.getPools();
        return Response.status(Response.Status.OK).entity(pools).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
	@Transactional
    public Response deletePool(@PathParam("id") long id) {
        //TODO: Benutzer from JSON TOKEN
        long uid = 1;
        User u = em.find(User.class,uid);
        if(u == null){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        Pool p = em.find(Pool.class, id);
        if(p == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if(p.getUser() != u){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        em.remove(p);
		//em.remove(p);
        //p = null;
        return Response.status(Response.Status.OK).build();
    }



}
