package notulus.filters;

import notulus.exception.InvalidLoginBodyException;
import notulus.utils.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static notulus.security.JWTConstants.ACCESSTOKEN;
import static notulus.security.JWTConstants.REFRESHTOKEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Jeroen Zeelmaekers
 * @version 0.0.1
 * @since 23/08/2022
 */
@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        requestBody body = getUsernamePassword(request);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(body.username,
                body.password);

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException {
        User user = (User) authResult.getPrincipal();

        String accessToken = jwtTokenProvider.generateAccessToken(request, user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(request, user);

        Map<String, String> tokens = new HashMap<>();
        tokens.put(ACCESSTOKEN, accessToken);
        tokens.put(REFRESHTOKEN, refreshToken);

        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

    private requestBody getUsernamePassword(HttpServletRequest request) throws InvalidLoginBodyException {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readValue(request.getInputStream(), requestBody.class);
        } catch (IOException e) {
            throw new InvalidLoginBodyException("Invalid login body");
        }

    }

    private record requestBody(String username, String password) {
    }
}
