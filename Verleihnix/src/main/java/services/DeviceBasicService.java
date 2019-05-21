package services;

import entities.DeviceBasic;
import entities.DeviceElement;
import entities.Pool;
import entities.User;
import proxies.DeviceBasicProxy;
import security.RequiresWebToken;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/deviceBasic")
public class DeviceBasicService extends SuperService {


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RequiresWebToken
    public Response editDeviceBasic(@Valid DeviceBasicProxy dbp) {
        User u;
        try {
            u = getUserByHttpToken();
        } catch (NotAuthorizedException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        DeviceBasic db;
        if(dbp.getId() == -1){ // ADD
            Pool p = em.find(Pool.class, dbp.getIdPool());
            if (p == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            if (p.getUser() != u) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            db = new DeviceBasic(dbp.getDescription(), p);
            em.persist(db);
            dbp.setId(db.getId());
            dbp.setIdPool(db.getPool().getId());
            if (dbp.getAmount() > 0) {
                for (int i = 0; i < dbp.getAmount(); i++) {
                    DeviceElement de = new DeviceElement(db);
                    em.persist(de);
                    db.getDeviceElements().add(de);
                }
            }
        }else{ // UPDATE
            db =  em.find(DeviceBasic.class, dbp.getId());
            if(db == null){
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            if(db.getPool().getUser() != u){
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            db.setDescription(dbp.getDescription());
            db.setPool(em.find(Pool.class, dbp.getIdPool()));
            em.persist(db);
        }
        return Response.status(Response.Status.OK).entity(dbp).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RequiresWebToken
    public Response getDeviceBasic(@PathParam("id") long id) {

        try {
            User u = getUserByHttpToken();
            DeviceBasic db = findDeviceBasic(id, u);
            return Response.status(Response.Status.OK).entity(db).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (NotAuthorizedException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @GET
    @Path("/pool/{idPool}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RequiresWebToken
    public Response getDeviceBasics(@PathParam("idPool") long idPool) {
        User u;
        try {
            u = getUserByHttpToken();
        } catch (NotAuthorizedException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        List<DeviceBasic> deviceBasics = new ArrayList<>();
        for (Pool pool : u.getPools()) {
            if (pool.getId() == idPool) {
                deviceBasics = pool.getBasicDevices();
            }
        }
        return Response.status(Response.Status.OK).entity(deviceBasics).build();
    }


    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RequiresWebToken
    public Response deleteDeviceBasic(@PathParam("id") long id) {
        try {
            User u = getUserByHttpToken();
            DeviceBasic db = findDeviceBasic(id, u);
            em.remove(db);
            return Response.status(Response.Status.OK).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (NotAuthorizedException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    private DeviceBasic findDeviceBasic(long id, User u) throws NotFoundException, NotAuthorizedException{
        DeviceBasic db = em.find(DeviceBasic.class, id);
        if(db == null){
            throw new NotFoundException();
        }
        if(db.getPool().getUser() != u){
            throw new NotAuthorizedException(Response.Status.UNAUTHORIZED);
        }
        return db;
    }
}
