package security;

import entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * This class is used for generate and validate the jwtToken
 */
@ApplicationScoped
public class JwtTokenService {

    private static String SECRET_KEY = "292FA9DD89C4F62FDB82B955E77AC292FA9DD89C4F62FDB82B955E77AC292FA9DD89C4F62FDB82B955E77AC292FA9DD89C4F62FDB82B955E77AC292FA9DD89C4F62FDB82B955E77AC";

    @PersistenceContext
    EntityManager em;

    /**
     * generate a JWT-Token by given User-Object
     * @param user
     * @return A String which hold the JWT-Token
     */
    public String generateJwtToken(User user) {

        final LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("UTC"));
        final LocalDateTime nextTime = currentTime.plusHours(24);
        final Instant instant2 = nextTime.toInstant(ZoneOffset.UTC);
        final Date expiryDate = Date.from(instant2);

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        final String token = Jwts.builder()
                .setSubject(""+user.getId())
                .claim("userId", user.getId())
                .claim("valide","ja")
                .signWith(SignatureAlgorithm.HS512, signingKey)
                .setExpiration(expiryDate)
                .compact();

        return token;
    }

    /**
     * Validate users JWT-Token by given token
     */
    public void validate(String token) {
        getUserByToken(token);
    }

    /**
     * Validate users JWT-Token and return the user-object of the Token
     * Throws an Exception if token is invalide
     * @param token
     * @return the user of the token
     */
    public User getUserByToken(String token) {
        try {
            token = token.split(" ")[1];
            final Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                    .parseClaimsJws(token)
                    .getBody();
            final long userId = (long) claims.get("userId", Long.class);
            User u = em.find(User.class,userId);
            if (u.getId()==userId) {
                return u;
            } else {
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
            }
        } catch (Exception e) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }
}
