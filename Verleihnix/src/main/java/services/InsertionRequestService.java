package services;

import entities.*;
import proxies.InsertionRequestProxy;
import proxies.InsertionRequestStateProxy;
import security.RequiresWebToken;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;


@Path("/insertionRequest")
public class InsertionRequestService extends SuperService {

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RequiresWebToken
    public Response editInsertionRequest(@Valid InsertionRequestProxy insertionRequestProxy) {
        User user = this.getUserByHttpToken();
        if(user == null){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        InsertionRequest insertionRequest;
        if(insertionRequestProxy.getId() == -1){
            try {
                Insertion insertion = this.findInsertion(insertionRequestProxy.getInsertionId(), user);
                insertionRequest = new InsertionRequest();
                insertionRequest = createInsertionRequest(insertionRequest,insertionRequestProxy,insertion);
                if(insertionRequest.getState() == null) insertionRequest.setState(State.requested);
                insertionRequest.setRequester(user.getId());
                insertionRequestProxy.setState(State.requested);
                insertionRequestProxy.setRequesterId(insertionRequest.getRequester());
                em.persist(insertionRequest);
                insertionRequestProxy.setId(insertionRequest.getId());

                if (!insertionRequestProxy.getMessage().equals("")) {
                    persistChatEntry(user.getId(),insertionRequestProxy.getMessage(),insertionRequest);
                }

            } catch (IllegalArgumentException e){
                return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Value is missing").build();
            } catch (NotFoundException e) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } catch (NotAuthorizedException e) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        }else{
            insertionRequest = em.find(InsertionRequest.class, insertionRequestProxy.getId());
            if (insertionRequest==null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            if(insertionRequest.getRequester() != user.getId()){
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            try {
                Insertion insertion = this.findInsertion(insertionRequestProxy.getInsertionId(), user);
                insertionRequest = createInsertionRequest(insertionRequest,insertionRequestProxy,insertion);
                em.persist(insertionRequest);
                insertionRequestProxy.setRequesterId(insertionRequest.getRequester());
                if (!insertionRequestProxy.getMessage().equals("")) {
                    persistChatEntry(user.getId(),insertionRequestProxy.getMessage(),insertionRequest);
                }
            } catch (NotAcceptableException e) {
                return Response.status(Response.Status.NOT_ACCEPTABLE).entity("value is missing").build();
            } catch (IllegalArgumentException e){
                return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Product already exists").build();
            } catch (NotFoundException e) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } catch (NotAuthorizedException e) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        }
        return Response.status(Response.Status.OK).entity(insertionRequestProxy).build();
    }

    private void persistChatEntry(long senderId, String message, InsertionRequest insertionRequest) {
        ChatEntry chatEntry = new ChatEntry();
        chatEntry.setMessage(message);
        chatEntry.setReadByListener(false);
        chatEntry.setSendDate(System.currentTimeMillis());
        chatEntry.setSenderId(senderId);
        chatEntry.setInsertionRequest(insertionRequest);
        em.persist(chatEntry);
    }

    private InsertionRequest createInsertionRequest(InsertionRequest insertionRequest, InsertionRequestProxy insertionRequestProxy, Insertion insertion) {
        insertionRequest.setInsertion(insertion);
        insertionRequest.setState(insertionRequestProxy.getState()==null ? insertionRequest.getState() : insertionRequestProxy.getState());
        insertionRequest.setDateFrom(insertionRequestProxy.getDateFrom());
        insertionRequest.setDateTo(insertionRequestProxy.getDateTo());
        insertionRequest.setEditAt(System.currentTimeMillis());
        return insertionRequest;

    }

    private Insertion findInsertion(long insertionId, User u) {
        Insertion insertion = em.find(Insertion.class, insertionId);
        if(insertion == null){
            throw new NotFoundException();
        }
        return insertion;
    }



    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RequiresWebToken
    public Response getInsertionsRequests() {

        try {
            User user = getUserByHttpToken();
            List<InsertionRequestProxy> insertionRequests = new ArrayList<>();

            // requested to user
            for(Pool pool : user.getPools()) {
                for (Insertion insertion : pool.getInsertions()) {
                    for (InsertionRequest insertionRequest : insertion.getInsertionRequests()) {
                        InsertionRequestProxy insertionRequestProxy = new InsertionRequestProxy(insertionRequest,1);
                        insertionRequests.add(insertionRequestProxy);
                    }
                }
            }

            // requested by user
            List<InsertionRequest> userRequests = this.em.createQuery("SELECT r FROM InsertionRequest r " +
                                                                                    "WHERE r.requesterId= :id")
                    .setParameter("id",user.getId())
                    .getResultList();
            for (InsertionRequest insertionRequest : userRequests) {
                InsertionRequestProxy insertionRequestProxy = new InsertionRequestProxy(insertionRequest,2);
                insertionRequests.add(insertionRequestProxy);
            }
            return Response.status(Response.Status.OK).entity(insertionRequests).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (NotAuthorizedException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @POST
    @Path("state/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RequiresWebToken
    public Response setInsertionRequestState(@PathParam("id") long insertionRequestId, InsertionRequestStateProxy p) {
        try {
            User user = getUserByHttpToken();
            InsertionRequest insertionRequest = em.find(InsertionRequest.class, insertionRequestId);
            switch (p.getState()){
                case 0:
                    insertionRequest.setState(State.requested);
                    break;
                case 1:
                    insertionRequest.setState(State.accepted);
                    break;
                case 2:
                    insertionRequest.setState(State.declined);
                    break;
            }
            insertionRequest.setEditAt(System.currentTimeMillis());
            em.persist(insertionRequest);
            if (!p.getMessage().equals("") && p.getMessage() != null) {
                persistChatEntry(user.getId(),p.getMessage(),insertionRequest);
            }
            InsertionRequestProxy op = new InsertionRequestProxy(insertionRequest);
            return Response.status(Response.Status.OK).entity(op).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (NotAuthorizedException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }



    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RequiresWebToken
    public Response getInsertionRequest(@PathParam("id") long insertionRequestId) {

        try {
            InsertionRequest insertionRequest = em.find(InsertionRequest.class, insertionRequestId);
            InsertionRequestProxy insertionRequestProxy = new InsertionRequestProxy(insertionRequest);
            return Response.status(Response.Status.OK).entity(insertionRequestProxy).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (NotAuthorizedException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    /*
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RequiresWebToken
    public Response deleteInsertion(@PathParam("id") long insertionId) {
        try {
            User user = getUserByHttpToken();
            Insertion insertion = this.findInsertion(insertionId, user);
            this.deletionHelper.deleteInsertion(insertion);
            return Response.status(Response.Status.OK).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (NotAuthorizedException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }
     */

}
