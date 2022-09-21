package notulus.services.impl;

import notulus.entities.Role;
import notulus.repositories.RoleRepository;
import notulus.services.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author Jeroen Zeelmaekers
 * @version 0.0.1
 * @since 23/08/2022
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    /*
     * (non-Javadoc)
     * 
     * @see notulus.services.RoleService#save(notulus.entities.Role)
     */
    @Override
    @Transactional
    public Role save(Role role) {
        log.info("Saving role: {}", role);
        return roleRepository.save(role);
    }
}
