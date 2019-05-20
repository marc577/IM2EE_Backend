package services;

import com.google.gson.JsonObject;
import com.sun.org.apache.xpath.internal.operations.Bool;
import entities.DeviceBasic;
import entities.DeviceElement;
import entities.Pool;
import entities.User;
import proxies.PoolProxy;
//import javax.json.JsonObject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/pool")
@Stateless
public class PoolService {

    @Inject
    private HttpServletRequest httpServletRequest;

    @PersistenceContext
    EntityManager em;


    @Path("/addPool")
    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Pool addPool() {

        final com.google.gson.JsonObject jsonObject = RequestHelper.httpRequestToJsonObject(httpServletRequest);

        final long id = jsonObject.get("id").getAsLong();
        final String poolDescription = jsonObject.get("poolDescription").getAsString();

        User u = em.find(User.class, id);
        Pool p = new Pool(poolDescription, u);
        em.persist(p);
        return p;
    }


    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postPool(@Valid PoolProxy pp) {
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
    @Transactional
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response postPool(@PathParam("id") long id) {
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
        return Response.status(Response.Status.OK).entity(p).build();
    }



}
