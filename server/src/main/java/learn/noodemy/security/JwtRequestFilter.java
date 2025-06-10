package learn.noodemy.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtConverter converter;

    public JwtRequestFilter(JwtConverter converter) {
        this.converter = converter;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // check for jwt authorization header
        // if present,
            // extract user info from token
            // generate an auth token using User info
            // set authentication using SecurityContextHolder and generated auth token
        // else , continue the filter chain

        String authorization = request.getHeader("Authorization");

        if (authorization != null && authorization.startsWith("Bearer ")) {

            try {
                User user = converter.getUserFromToken(authorization);
                if (user != null) {
                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                            user.getUsername(), null, user.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(token);
                }
            } catch (ExpiredJwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                System.out.println(e);
                return;
            } catch (JwtException e) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                System.out.println(e);
                return;
            }

        }

        filterChain.doFilter(request, response);
    }
}
