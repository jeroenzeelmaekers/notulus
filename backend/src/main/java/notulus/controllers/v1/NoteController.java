package notulus.controllers.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import notulus.dtos.note.CreateNoteDto;
import notulus.entities.Note;
import notulus.exception.NoNoteFoundException;
import notulus.services.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Jeroen Zeelmaekers
 * @version 0.0.1
 * @since 23/08/2022
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/note")
@SecurityRequirement(name = "Bearer Authentication")
public class NoteController {

    private final NoteService noteService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @Operation(summary = "Get all saved notes")
    protected ResponseEntity<?> getAll(@RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "id") String sortBy) {
        List<Note> notes = noteService.getAll(pageSize, pageNo, sortBy);
        return ResponseEntity.ok(notes);
    }

    @PostMapping("/{noteId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    protected ResponseEntity<?> update(@PathVariable(value = "noteId") final Long id,
            @RequestBody final String content) {
        Note note;

        try {
            note = noteService.updateNote(id, content);
        } catch (NoNoteFoundException e) {
            return ResponseEntity.ok(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(note);
    }

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    protected ResponseEntity<?> create(@RequestBody final CreateNoteDto createNoteDto) {
        Note note;

        try {
            note = noteService.create(createNoteDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(note);
    }

    @DeleteMapping("/{noteId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    protected ResponseEntity<?> update(@PathVariable(value = "noteId") final Long id) {

        try {
            noteService.deleteNote(id);
        } catch (NoNoteFoundException e) {
            return ResponseEntity.ok(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

}
