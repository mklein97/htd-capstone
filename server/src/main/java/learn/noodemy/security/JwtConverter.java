package learn.noodemy.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtConverter {

    // Secret key, used to configure signing key
    private static final String SECRET_KEY = "ecc6665e80e155613f04465d0a3d68dfed537cc36820ba6c7ec10a919f3bfc05d24f2c7920d74e698f7510560ef11e54ef1df7b682475d557b938934646a8171";

    // Configurable constraints for token
    private final String ISSUER = "Noodemy";
    private final int EXPIRATION_MINUTES = 60;
    private final int EXPIRATION_MILLIS = EXPIRATION_MINUTES * 60 * 1000;

    // Convert a Spring Security User (org.springframework.security) into a token
    public String getTokenFromUser(User user) {

        String authorities = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // Use JJWT classes to BUILD a token.
        return Jwts.builder()
                .setIssuer(ISSUER)
                .setSubject(user.getUsername())
                .claim("authorities", authorities)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MILLIS))
                .signWith(getSigningKey())
                .compact();
    }

    // Extract data from a token to create a Spring Security User (org.springframework.security)
    public User getUserFromToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return null;
        }

        // Use JJWT classes to READ a token.
        Jws<Claims> jws = Jwts.parserBuilder()
                .requireIssuer(ISSUER)
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token.substring(7));

        String username = jws.getBody().getSubject();
        String authStr = (String) jws.getBody().get("authorities");
        List<GrantedAuthority> authorities = Arrays
                .stream(authStr.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new User(username, username, authorities);
    }

    // Generate a SecretKey using the provided key
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
