package notulus.services;

import notulus.entities.User;
import notulus.exception.NoRoleFoundException;
import notulus.exception.NoUserFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author Jeroen Zeelmaekers
 * @version 0.0.1
 * @since 23/08/2022
 */
public interface UserService extends UserDetailsService {
    User save(User user);

    void addRole(String username, String roleName) throws NoUserFoundException, NoRoleFoundException;

    User getByUsername(String username) throws NoUserFoundException;
}
