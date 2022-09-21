package notulus.controllers.v1;

import notulus.entities.Role;
import notulus.services.RoleService;
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
@RequestMapping("/api/v1/role")
@SecurityRequirement(name = "Bearer Authentication")
public class RoleController {

    private final RoleService roleService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Role> add(@RequestBody Role role) {
        role = roleService.save(role);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/role/add").toUriString());

        return ResponseEntity.created(uri).body(role);
    }
}
