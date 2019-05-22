package services;

import entities.Pool;
import entities.User;
import proxies.PoolProxy;
import security.JwtTokenService;
import security.RequiresWebToken;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/pool")
@Stateless
public class PoolService {

    @PersistenceContext
    EntityManager em;

    @Inject
    private HttpServletRequest httpServletRequest;

    @Inject
    private JwtTokenService jwtTokenService;

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

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RequiresWebToken
    public Response getPool(@PathParam("id") long id) {
        String token = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        User u = jwtTokenService.getUserByToken(token);
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

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RequiresWebToken
    public Response deletePool(@PathParam("id") long id) {
        String token = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        User u = jwtTokenService.getUserByToken(token);
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

        em.createQuery("DELETE from Pool p where p.id = :id").setParameter("id", p.getId()).executeUpdate();

        return Response.status(Response.Status.OK).build();
    }



}
