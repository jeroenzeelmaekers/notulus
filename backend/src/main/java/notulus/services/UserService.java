package notulus.services;

import notulus.entities.User;
import notulus.exception.NoRoleFoundException;
import notulus.exception.NoUserFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User save(User user);

    void addRole(String username, String roleName) throws NoUserFoundException, NoRoleFoundException;

    User getByUsername(String username) throws NoUserFoundException;
}
