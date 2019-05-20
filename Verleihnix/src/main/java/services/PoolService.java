package services;

import com.google.gson.JsonObject;
import entities.DeviceBasic;
import entities.DeviceElement;
import entities.Pool;
import entities.User;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/pool")
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

        final JsonObject jsonObject = RequestHelper.httpRequestToJsonObject(httpServletRequest);

        final long id = jsonObject.get("id").getAsLong();
        final String poolDescription = jsonObject.get("poolDescription").getAsString();

        User u = em.find(User.class, id);
        Pool p = new Pool(poolDescription, u);
        em.persist(p);
        return p;
    }



}
