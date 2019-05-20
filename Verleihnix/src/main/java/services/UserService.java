package services;

import entities.User;
import security.JwtTokenService;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.util.List;



@Path("/user")
public class UserService {

    @PersistenceContext
    EntityManager em;

    @Inject
    JwtTokenService jwtTokenService;

    @Path("/register")
    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(JsonObject jsonObject) {

        final String firstName = jsonObject.getString("firstName");
        final String lastName = jsonObject.getString("lastName");
        final String email = jsonObject.getString("email");
        final String password = jsonObject.getString("password");


        if (!emailExists(email)) {
            //#TODO Password verschlüsseln
            User user = new User(firstName,lastName, email, password);
            em.persist(user);
            return Response.status(Response.Status.OK).entity(user).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("User already exists").build();
    }

    private boolean emailExists(final String email) {
        List resultList = em.createNativeQuery("SELECT COUNT(*) " +
                                                "FROM user " +
                                                "WHERE email= ?")
                    .setParameter(1,email)
                    .getResultList();
        System.out.println("Anzahl: " + resultList);
        return (resultList.get(0) != BigInteger.valueOf(0));
    }

    @Path("/login")
    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(JsonObject jsonObject) {

        final String email = jsonObject.getString("email");
        final String password = jsonObject.getString("password");

        List resultList = em.createNativeQuery("SELECT id " +
                "FROM user " +
                "WHERE email = ?")
                .setParameter(1, email)
                .getResultList();
        if (resultList.size()>0) {
            BigInteger uId = (BigInteger) resultList.get(0);
            User u = em.find(User.class, uId.longValue());
            if (u.getPassword().equals(password)) {
                u.setToken(jwtTokenService.generateJwtToken(u));
                return Response.status(Response.Status.OK).entity(u).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("Wrong password").build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }
    }








}

