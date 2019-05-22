package services;

import entities.User;
import org.mindrot.jbcrypt.BCrypt;
import proxies.ChangePasswordProxy;
import proxies.RegisterProxy;
import proxies.UserProxy;
import security.RequiresWebToken;

import javax.json.JsonObject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;




@Path("/user")
public class UserService extends SuperService{

    @Path("/register")
    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(@Valid RegisterProxy registerObject) {
        if (!emailExists(registerObject.getEmail())) {
            User user = new User(registerObject.getFirstName(),registerObject.getLastName(), registerObject.getEmail(), registerObject.getPassword());
            em.persist(user);
            return Response.status(Response.Status.OK).entity(user).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("User already exists").build();
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
            if (BCrypt.checkpw(password, u.getPassword())) {
                UserProxy up = new UserProxy(u.getId(),jwtTokenService.generateJwtToken(u), u.getLastName(), u.getFirstName(), u.getEmail() );
                return Response.status(Response.Status.OK).entity(up).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("Wrong password").build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }
    }

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RequiresWebToken
    public Response edit(@Valid UserProxy userProxy) {
        try {
            User user = this.getUserByHttpToken();
            User uneu = em.find(User.class, user.getId());
            if(user.getId() == uneu.getId()){
                uneu.setFirstName(userProxy.getFirstName());
                uneu.setLastName(userProxy.getLastName());
                if (!uneu.getEmail().equals(userProxy.getEmail())) {
                    if(!this.emailExists(userProxy.getEmail())) {
                        uneu.setEmail(userProxy.getEmail());
                    } else {
                        return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Email is already in use").build();
                    }

                }
                em.persist(uneu);
                return Response.status(Response.Status.OK).entity(userProxy).build();
            }else{
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        } catch (NotAuthorizedException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @GET
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RequiresWebToken
    public Response get() {
        try {
            User user = this.getUserByHttpToken();
            UserProxy up = new UserProxy();
            up.setId(user.getId());
            up.setToken(user.getToken());
            up.setFirstName(user.getFirstName());
            up.setLastName(user.getLastName());
            up.setEmail(user.getEmail());
            return Response.status(Response.Status.OK).entity(up).build();

        } catch (NotAuthorizedException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @Path("/changePassword")
    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RequiresWebToken
    public Response changePassword(@Valid ChangePasswordProxy changePasswordProxy) {

        try {
            User user = this.getUserByHttpToken();
            if(user.validate(changePasswordProxy.getPasswordConfirmation())){
                user.setPassword(changePasswordProxy.getNewPassword());
                em.persist(user);
                return Response.status(Response.Status.OK).build();
            }else{
                return Response.status(Response.Status.BAD_REQUEST).entity("Password confirmation does not match").build();
            }
        } catch (NotAuthorizedException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
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

}

