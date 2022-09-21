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

    /**
     * @param user the new user that needs to be saved
     * @return the newly saved user
     */
    User save(User user);

    /**
     * @param username the username to which the role needs to be added to
     * @param roleName the rolename of the role that needs to be added to the user
     * @throws NoUserFoundException if the given username is of an non-existing user
     * @throws NoRoleFoundException if the given rolename is of an non-existing role
     */
    void addRole(String username, String roleName) throws NoUserFoundException, NoRoleFoundException;

    /**
     * @param username the username of the user that needs to be returned
     * @return the found user based upon the given username
     * @throws NoUserFoundException if the given username is of an non-existing user
     */
    User getByUsername(String username) throws NoUserFoundException;
}
