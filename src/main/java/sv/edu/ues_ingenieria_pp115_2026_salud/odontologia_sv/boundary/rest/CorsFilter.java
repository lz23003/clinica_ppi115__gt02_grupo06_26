package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.rest;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Set;

@Provider
@Priority(Priorities.HEADER_DECORATOR)
public class CorsFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Set<String> ALLOWED_ORIGINS = Set.of(
            "http://localhost:5500",
            "http://127.0.0.1:5500"
    );

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String origin = requestContext.getHeaderString("Origin");
        String requestedMethod = requestContext.getHeaderString("Access-Control-Request-Method");

        if (!"OPTIONS".equalsIgnoreCase(requestContext.getMethod()) || requestedMethod == null) {
            return;
        }

        if (origin == null || !ALLOWED_ORIGINS.contains(origin)) {
            requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
            return;
        }

        requestContext.abortWith(Response.ok().build());
    }

    @Override
    public void filter(
            ContainerRequestContext requestContext,
            ContainerResponseContext responseContext
    ) throws IOException {
        String origin = requestContext.getHeaderString("Origin");
        if (origin == null || !ALLOWED_ORIGINS.contains(origin)) {
            return;
        }

        responseContext.getHeaders().putSingle("Access-Control-Allow-Origin", origin);
        responseContext.getHeaders().putSingle("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        responseContext.getHeaders().putSingle("Access-Control-Allow-Headers", "Accept, Authorization, Content-Type");
        responseContext.getHeaders().putSingle("Access-Control-Expose-Headers", "X-Total-Count");
        responseContext.getHeaders().putSingle("Access-Control-Max-Age", "600");
        responseContext.getHeaders().putSingle("Vary", "Origin");
    }
}
