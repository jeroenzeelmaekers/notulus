package notulus.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import notulus.entities.Role;
import notulus.entities.User;
import notulus.exception.NoRoleFoundException;
import notulus.exception.NoUserFoundException;
import notulus.repositories.RoleRepository;
import notulus.repositories.UserRepository;
import notulus.services.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

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
        log.info("Saving user:{}", user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void addRole(String email, String roleName) throws NoUserFoundException, NoRoleFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoUserFoundException("No user found with email:" + email));
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new NoRoleFoundException("No role found with name:" + roleName));

        log.info("Adding role:{} to user:{}", roleName, email);

        user.getRoles().add(role);
    }

    @Override
    public User getByUsername(String email) throws NoUserFoundException {
        log.info("Getting user:{}", email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NoUserFoundException("No user found with email:" + email));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userRes = userRepository.findByEmail(email);
        User user;

        if (userRes.isEmpty()) {
            log.error("No user found with email:{}", email);
            throw new UsernameNotFoundException("User not found with email:" + email);
        } else {
            user = userRes.get();
            log.info("User found with email:{}", email);
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles()
                .forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                authorities);
    }
}
