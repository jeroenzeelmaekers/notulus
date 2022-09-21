package notulus.controllers.v1;

import notulus.dtos.user.AddRoleToUserDto;
import notulus.entities.User;
import notulus.exception.NoRoleFoundException;
import notulus.exception.NoUserFoundException;
import notulus.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * @author Jeroen Zeelmaekers
 * @version 0.0.1
 * @since 23/08/2022
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

    private final UserService userService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<User> add(@RequestBody User user) {
        user = userService.save(user);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/user/add").toUriString());

        return ResponseEntity.created(uri).body(user);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addRole(@RequestBody AddRoleToUserDto addRoleToUserDto) {
        try {
            userService.addRole(addRoleToUserDto.username(), addRoleToUserDto.roleName());
        } catch (NoUserFoundException | NoRoleFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().build();
    }
}
