package services;

import entities.Pool;
import entities.User;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.BufferedReader;
import java.math.BigInteger;
import java.util.List;


@Path("/user")
public class UserService {

    @Inject
    private HttpServletRequest httpServletRequest;

    @PersistenceContext
    EntityManager em;

    @Path("/register")
    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User register() {

        final JsonObject jsonObject = RequestHelper.httpRequestToJsonObject(httpServletRequest);

        final String firstName = jsonObject.get("firstName").getAsString();
        final String lastName = jsonObject.get("lastName").getAsString();
        final String email = jsonObject.get("email").getAsString();
        final String password = jsonObject.get("password").getAsString();


        if (!emailExists(email)) {
            //#TODO Password verschlüsseln
            User user = new User(firstName,lastName, email, password);
            em.persist(user);
            return user;
        }
        //#TODO Korrekte Info rausgeben
        System.out.println("User existiert bereits");
        return null;
    }

    public boolean emailExists(final String email) {
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
    public User login() {

        final JsonObject jsonObject = RequestHelper.httpRequestToJsonObject(httpServletRequest);

        final String email = jsonObject.get("email").getAsString();
        final String password = jsonObject.get("password").getAsString();

        List resultList = em.createNativeQuery("SELECT id " +
                "FROM user " +
                "WHERE email = ?")
                .setParameter(1, email)
                .getResultList();
        if (resultList.size()>0) {
            BigInteger uId = (BigInteger) resultList.get(0);
            User u = em.find(User.class, uId.longValue());
            if (u.getPassword().equals(password)) {
                System.out.println("Login erfolgreich");
                System.out.println(u);
                return u;
            } else {
                //#TODO Password inkorrekts
                System.out.println("Passwort ist falsch");
                return null;
            }

        } else {
            //#TODO Info, user existiert nicht
            System.out.println("User nicht gefunden");
            return null;
        }
    }





}

