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
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Rest-Service for the User
 */
@Path("/user")
public class UserService extends SuperService{

    /**
     * Users registration
     * @param registerObject
     * @return
     */
    @Path("/register")
    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(@Valid RegisterProxy registerObject) {

        Pattern regexPattern = Pattern.compile("^[(a-zA-Z-0-9-\\_\\+\\.)]+@[(a-z-A-z)]+\\.[(a-zA-z)]{2,3}$");
        Matcher regMatcher   = regexPattern.matcher(registerObject.getEmail());
        if (!regMatcher.matches()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Wrong email-address").build();
        }

        if (!emailExists(registerObject.getEmail())) {
            User user = new User(registerObject.getFirstName(),registerObject.getLastName(), registerObject.getEmail(), registerObject.getPassword());
            em.persist(user);
            return Response.status(Response.Status.OK).entity(user).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("User already exists").build();
    }

    /**
     * Rest-Service for login
     * @param jsonObject
     * @return
     */
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

    /**
     * Rest-Service for editing user information
     * @param userProxy
     * @return
     */
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

    /**
     * rest-Service for get an user object
     * @return
     */
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

    /**
     * Rest-Service for changing users password
     * @param changePasswordProxy
     * @return
     */
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

    /**
     * Rest-Service for deleting User
     * @return
     */
    @DELETE
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    @RequiresWebToken
    public Response deleteUser() {
        String token = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        User u = jwtTokenService.getUserByToken(token);
        if(u == null){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        this.deletionHelper.deleteUser(u);
        return Response.status(Response.Status.OK).build();
    }

    private boolean emailExists(final String email) {
        List resultList = em.createNativeQuery("SELECT COUNT(*) " +
                "FROM user " +
                "WHERE email= ?")
                .setParameter(1,email)
                .getResultList();
        return (resultList.get(0) != BigInteger.valueOf(0));
    }

}

