package services;

import com.google.gson.JsonObject;
import entities.DeviceBasic;
import entities.DeviceElement;
import entities.Pool;

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

@Path("/deviceBasic")
public class DeviceBasicService {

    @Inject
    private HttpServletRequest httpServletRequest;

    @PersistenceContext
    EntityManager em;

    @Path("/addDeviceBasic")
    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public DeviceBasic addDeviceBasic() {

        final JsonObject jsonObject = RequestHelper.httpRequestToJsonObject(httpServletRequest);

        final long id = jsonObject.get("poolId").getAsLong();
        final String deviceBasicDescription = jsonObject.get("description").getAsString();
        final long countOfElements = jsonObject.get("countOfElements").getAsLong();

        Pool pool = em.find(Pool.class, id);
        DeviceBasic db = new DeviceBasic(deviceBasicDescription, pool);
        em.persist(db);
        for (long l = 0; l < countOfElements; l++) {
            DeviceElement dv = new DeviceElement(db);
            em.persist(dv);
            db.getDeviceElements().add(dv);
        }
        return db;
    }
}
