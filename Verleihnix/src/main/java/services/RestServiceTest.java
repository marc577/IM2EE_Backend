package services;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;

@Path("/myService")
public class RestServiceTest {

    // http://localhost:8080/verleihnix/api/myService/sayHello
    @GET
    @Path("/sayHello")
    public String getHelloMsg() {
        System.out.println("HALLO IN HALLO WELT SERVICE");
        return "HALLO!";
    }

}
