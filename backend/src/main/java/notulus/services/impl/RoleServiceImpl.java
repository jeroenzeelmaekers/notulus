package notulus.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import notulus.entities.Role;
import notulus.repositories.RoleRepository;
import notulus.services.RoleService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public Role save(Role role) {
        log.info("Saving role: {}", role);
        return roleRepository.save(role);
    }
}
