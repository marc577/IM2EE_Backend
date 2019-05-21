package services;

import entities.*;
import proxies.DeviceElementProxy;
import security.RequiresWebToken;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/deviceElement")
public class DeviceElementService extends SuperService {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RequiresWebToken
    public Response editDeviceElement(@Valid DeviceElementProxy deviceElementProxy) {
        /*
        User u;
        try {
            u = getUserByHttpToken();
        } catch (NotAuthorizedException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        DeviceElement deviceElement = null;

        if(deviceElementProxy.getId() == -1){ // ADD
            DeviceBasic deviceBasic = em.find(DeviceBasic.class, deviceElementProxy.getDeviceBasicId());
            if (deviceBasic == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            if (deviceBasic.getPool().getUser() != u) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            deviceElement = new DeviceElement(deviceBasic);
            deviceElement.setActive(deviceElementProxy.isActive());
            em.persist(deviceElement);
        } else { // UPDATE
            deviceElement = em.find(DeviceElement.class, deviceElementProxy.getId());
            if(deviceElement == null){
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            if(deviceElement.getDeviceBasic().getPool().getUser() != u){
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            deviceElement.setActive(deviceElementProxy.isActive());
            deviceElement.setDeviceBasic(em.find(DeviceBasic.class, deviceElementProxy.getDeviceBasicId()));
            em.persist(deviceElement);
        }
        return Response.status(Response.Status.OK).entity(deviceElement).build();

         */
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }


    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RequiresWebToken
    public Response getDeviceElement(@PathParam("id") long id) {

        /*
        try {
            User u = getUserByHttpToken();
            DeviceElement deviceElement = this.findDeviceElement(id, u);
            return Response.status(Response.Status.OK).entity(deviceElement).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (NotAuthorizedException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

         */
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @GET
    @Path("/deviceBasic/{deviceBasicId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RequiresWebToken
    public Response getDeviceElements(@PathParam("deviceBasicId") long deviceBasicId) {
        /*
        User u;
        try {
            u = getUserByHttpToken();
        } catch (NotAuthorizedException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        List<DeviceElement> deviceElements = new ArrayList<>();

        for (Pool pool : u.getPools()) {
            for (DeviceBasic deviceBasic : pool.getBasicDevices()) {
                if (deviceBasic.getId()==deviceBasicId) {
                    deviceElements = deviceBasic.getDeviceElements();
                    break;
                }
            }
            if (deviceElements.size()>0) {
                break;
            }
        }

         */
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @GET
    @Path("/openRequests/")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RequiresWebToken
    public Response getOpenRequests() {
        User u;
        try {
            u = getUserByHttpToken();
        } catch (NotAuthorizedException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        List<InsertionStateCalendar> openRequestsDeviceElements = new ArrayList<>();
        /*
        for (Pool pool : u.getPools()) {
            for (DeviceBasic deviceBasic : pool.getBasicDevices()) {
                for(DeviceElement deviceElement : deviceBasic.getDeviceElements()) {
                    for (InsertionStateCalendar insertionStateCalendar : deviceElement.getInsertionStateCalendars()) {
                        if (insertionStateCalendar.getState()==State.requested) {
                            openRequestsDeviceElements.add(insertionStateCalendar);
                        }
                    }
                }
            }
        }

         */
        return Response.status(Response.Status.OK).entity(openRequestsDeviceElements).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RequiresWebToken
    public Response deleteDeviceElement(@PathParam("id") long id) {
        /*
        try {
            User u = getUserByHttpToken();
            DeviceElement db = findDeviceElement(id, u);
            em.remove(db);
            return Response.status(Response.Status.OK).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (NotAuthorizedException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

         */
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    private Response findDeviceElement(long id, User u) throws NotFoundException, NotAuthorizedException{
        /*
        DeviceElement deviceElement = em.find(DeviceElement.class, id);
        if(deviceElement == null){
            throw new NotFoundException();
        }
        if(deviceElement.getDeviceBasic().getPool().getUser() != u){
            throw new NotAuthorizedException(Response.Status.UNAUTHORIZED);
        }
        return deviceElement;

         */

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }
}
