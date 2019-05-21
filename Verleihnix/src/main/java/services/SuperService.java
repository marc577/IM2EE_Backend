package services;

import entities.User;
import security.JwtTokenService;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

public abstract class SuperService {

    @Inject
    HttpServletRequest httpServletRequest;

    @PersistenceContext
    EntityManager em;

    @Inject
    JwtTokenService jwtTokenService;

    protected User getUserByHttpToken() throws NotAuthorizedException {
        String token = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        User u = jwtTokenService.getUserByToken(token);
        if (u == null) {
            throw new NotAuthorizedException(Response.Status.UNAUTHORIZED);
        }
        return u;
    }
}
