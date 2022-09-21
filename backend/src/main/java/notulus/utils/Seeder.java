package notulus.utils;

import notulus.entities.Note;
import notulus.entities.Role;
import notulus.entities.User;
import notulus.repositories.NoteRepository;
import notulus.services.RoleService;
import notulus.services.UserService;
import com.thedeanda.lorem.LoremIpsum;
import lombok.SneakyThrows;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Jeroen Zeelmaekers
 * @version 0.0.1
 * @since 23/08/2022
 */
@Component
public record Seeder(NoteRepository noteRepository, RoleService roleService, UserService userService) {

    @EventListener
    public void seeding(ContextRefreshedEvent ignoreEvent) {
        seedNotes();
        seedRoles();
        seedUsers();
    }

    private void seedNotes() {
        List<Note> notes = generateListOfNotes();
        noteRepository.saveAll(notes);
    }

    private List<Note> generateListOfNotes() {
        List<Note> notes = new ArrayList<>();

        LoremIpsum lorum = LoremIpsum.getInstance();
        Random random = new Random();

        for (int i = 0; i < 100; i++) {
            notes.add(Note.builder().content(lorum.getWords(random.nextInt(5, 20))).build());
        }

        return notes;
    }

    private void seedRoles() {
        roleService.save(new Role(null, "ROLE_USER"));
        roleService.save(new Role(null, "ROLE_ADMIN"));
    }

    @SneakyThrows
    private void seedUsers() {
        userService.save(new User(null, "Jeroen Zeelmaekers", "jeroen", "password1234", new ArrayList<>()));
        userService.save(new User(null, "Jeroen Zeelmaekers 2", "jeroen 2", "password1234", new ArrayList<>()));

        userService.addRole("jeroen", "ROLE_USER");
        userService.addRole("jeroen 2", "ROLE_ADMIN");
    }

}
