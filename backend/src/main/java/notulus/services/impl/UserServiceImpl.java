package notulus.services.impl;

import notulus.entities.Role;
import notulus.entities.User;
import notulus.exception.NoRoleFoundException;
import notulus.exception.NoUserFoundException;
import notulus.repositories.RoleRepository;
import notulus.repositories.UserRepository;
import notulus.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * @author Jeroen Zeelmaekers
 * @version 0.0.1
 * @since 23/08/2022
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User save(User user) {
        log.trace("Saving user:{}", user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void addRole(String username, String roleName) throws NoUserFoundException, NoRoleFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NoUserFoundException("No user found with username:" + username));
        Role role = roleRepository.findByName(roleName).orElseThrow(() -> new NoRoleFoundException("No role found with name:" + roleName));

        log.trace("Adding role:{} to user:{}", roleName, username);
        user.getRoles().add(role);
    }

    @Override
    public User getByUsername(String username) throws NoUserFoundException {
        log.trace("Getting user:{}", username);
        return userRepository.findByUsername(username).orElseThrow(() -> new NoUserFoundException("No user found with username:" + username));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userRes = userRepository.findByUsername(username);
        User user;

        if (userRes.isEmpty()) {
            log.error("No user found with username:{}", username);
            throw new UsernameNotFoundException("User not found with username:" + username);
        } else {
            user = userRes.get();
            log.trace("User found with username:{}", username);
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}
