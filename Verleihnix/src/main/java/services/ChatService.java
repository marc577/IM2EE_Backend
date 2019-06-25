package services;

import entities.*;
import proxies.ChatEntryInProxy;
import security.RequiresWebToken;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Rest-Service for the Chats
 */
@Path("/chat")
public class ChatService extends SuperService {

    /**
     * Create a new object of ChatEntry object and store in database
     * @param chatentryInProxy
     * @return the created ChatEntry object
     */
    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RequiresWebToken
    public Response createChatEntry(@Valid ChatEntryInProxy chatentryInProxy) {
        User user = this.getUserByHttpToken();
        if(user == null){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        if(chatentryInProxy.getIdSender() == -1 || chatentryInProxy.getIdInsertionRequest() == -1 || chatentryInProxy.getMessage().equals("")) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Value is missing").build();
        } else {
            long currTime = System.currentTimeMillis();
            ChatEntry chatEntry = new ChatEntry();
            chatEntry.setMessage(chatentryInProxy.getMessage());
            chatEntry.setSendDate(currTime);
            chatEntry.setReadByListener(false);
            chatEntry.setSenderId(chatentryInProxy.getIdSender());
            InsertionRequest insertionRequest = em.find(InsertionRequest.class,chatentryInProxy.getIdInsertionRequest());
            insertionRequest.setEditAt(currTime);
            em.persist(insertionRequest);
            chatEntry.setInsertionRequest(insertionRequest);
            em.persist(chatEntry);
            return Response.status(Response.Status.OK).entity(chatEntry).build();
        }

    }


    /**
     * returns an object of a ChatRoom by given id of an insertionRequest
     * @param insertionRequestId
     * @return
     */
    @GET
    @Path("/{idInsertionRequest}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RequiresWebToken
    public Response getChatRoom(@PathParam("idInsertionRequest") long insertionRequestId) {
        try {
            User user = this.getUserByHttpToken();
            if(user == null){
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            List<ChatEntry> chatEntries = this.em.createQuery("SELECT c FROM ChatEntry c " +
                    "ORDER BY c.sendDate ASC ")
                    .getResultList();

            List<ChatEntry> returnList = new ArrayList<ChatEntry>();
            for (ChatEntry chatEntry : chatEntries) {
                if (chatEntry.getInsertionRequest().getId()==insertionRequestId) {
                    if (chatEntry.getSenderId() != user.getId()) {
                        chatEntry.setReadByListener(true);
                        em.persist(chatEntry);
                    }
                    returnList.add(chatEntry);
                }
            }

            return Response.status(Response.Status.OK).entity(returnList).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (NotAuthorizedException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }


    /**
     * Sets a ChatEntry to listened by the user
     * @param id
     * @return
     */
    @GET
    @Path("/setChatEntryListened/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RequiresWebToken
    public Response setChatEntryListened(@PathParam("id") long id) {

        try {
            User user = getUserByHttpToken();
            if(user == null){
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            ChatEntry chatEntry = em.find(ChatEntry.class, id);
            chatEntry.setReadByListener(true);
            em.persist(chatEntry);
            return Response.status(Response.Status.OK).entity(chatEntry).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (NotAuthorizedException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    /**
     *
     */
    @GET
    @Path("/new")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RequiresWebToken
    public Response setChatEntryListened() {
        // Anzahl der neuen Nachrichten für den eingeloggten Benutzer
        // über alle Requests und Chats hinweg
        try {
            User user = getUserByHttpToken();

            return Response.status(Response.Status.OK).entity(10).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (NotAuthorizedException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    /**
     * delete a chatEntry
     * @param id
     * @return
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RequiresWebToken
    public Response deleteChatEntry(@PathParam("id") long id) {
        User user = this.getUserByHttpToken();
        if(user == null){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        ChatEntry chatEntry = em.find(ChatEntry.class, id);
        if(chatEntry == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if(chatEntry.getSenderId() != user.getId()){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        this.deletionHelper.deleteChatEntry(chatEntry);

        return Response.status(Response.Status.OK).build();
    }
}
