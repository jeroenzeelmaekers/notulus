package notulus.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static notulus.security.JWTConstants.AUTHORITIES;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

/**
 * @author Jeroen Zeelmaekers
 * @version 0.0.1
 * @since 25/08/2022
 */
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    public String generateAccessToken(HttpServletRequest request, User user) {
        List<String> claims = getClaimsFromUser(user);
        return JWT.create()
                .withIssuer(request.getRequestURL().toString())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(30)))
                .withSubject(user.getUsername())
                .withClaim(AUTHORITIES, claims)
                .sign(HMAC512(secret));
    }

    public String generateRefreshToken(HttpServletRequest request, User user) {
        return JWT.create()
                .withIssuer(request.getRequestURL().toString())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(200)))
                .withSubject(user.getUsername())
                .sign(HMAC512(secret));
    }

    public List<SimpleGrantedAuthority> getAuthorities(String token) {
        List<String> claims = getClaimsFromToken(token);
        return claims.stream().map(SimpleGrantedAuthority::new).toList();
    }

    public Authentication getAuthentication(String username, List<SimpleGrantedAuthority> authorities, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthToken =
                new UsernamePasswordAuthenticationToken(username, null, authorities);
        usernamePasswordAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return usernamePasswordAuthToken;
    }

    public String getSubject(String token) {
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getSubject();
    }

    private List<String> getClaimsFromToken(String token) {
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getClaim(AUTHORITIES).asList(String.class);
    }

    private JWTVerifier getJWTVerifier() {
        JWTVerifier verifier;

        try {
            Algorithm algorithm = HMAC512(secret);
            verifier = JWT.require(algorithm).build();
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException("Token cannot be verified");
        }

        return verifier;
    }

    private List<String> getClaimsFromUser(User user) {
        return user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

}
