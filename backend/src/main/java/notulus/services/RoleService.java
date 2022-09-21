package notulus.services;

import notulus.entities.Role;

/**
 * @author Jeroen Zeelmaekers
 * @version 0.0.1
 * @since 23/08/2022
 */
public interface RoleService {
    /**
     * @param role the to save role
     * @return the newly saved role
     */
    Role save(Role role);
}
