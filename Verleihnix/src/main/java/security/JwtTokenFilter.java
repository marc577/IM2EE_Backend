package security;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.Optional;


/**
 * Authenticates the user according to annotation roles data
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
@RequiresWebToken
public class JwtTokenFilter implements ContainerRequestFilter {

    @Inject
    private JwtTokenService jwtTokenService;

    @Context
    private ResourceInfo resourceInfo;

    private static final String AUTH_HEADER_BEARER_PREFIX = "Bearer ";

    /**
     * Extract and validate json web token
     *
     * @param requestContext {@link ContainerRequestContext}
     */
    @Override
    public void filter(final ContainerRequestContext requestContext) {

        final String authorization = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if ((authorization != null) && (authorization.startsWith(AUTH_HEADER_BEARER_PREFIX))) {
            String token = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
            try {
                jwtTokenService.validate(token);
            } catch (final Exception e) {
                throw new NotAuthorizedException("no valid web token provided");
            }
        }
        else
        {
            throw new NotAuthorizedException("no valid authorization header present");
        }

    }
}
