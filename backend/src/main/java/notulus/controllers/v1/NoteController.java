package notulus.controllers.v1;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import notulus.dtos.note.CreateNoteDto;
import notulus.dtos.note.PagedNotesDto;
import notulus.dtos.note.UpdateNoteDto;
import notulus.entities.Note;
import notulus.exception.NoNoteFoundException;
import notulus.services.NoteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/note")
@SecurityRequirement(name = "Bearer Authentication")
public class NoteController {

    private final NoteService noteService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    protected ResponseEntity<?> getAll(@PageableDefault(sort = "id") Pageable pageable) {
        Page<Note> pages = noteService.getAll(pageable);

        PagedNotesDto dto = new PagedNotesDto(
                pages.getNumber(),
                pages.getTotalPages(),
                pages.getNumberOfElements(),
                pages.getSize(),
                pages.getContent());

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    protected ResponseEntity<?> update(@RequestBody UpdateNoteDto updateNoteDto) {
        Note note;

        try {
            note = noteService.update(updateNoteDto);
        } catch (NoNoteFoundException e) {
            return ResponseEntity.ok(e.getMessage());
        }

        return ResponseEntity.ok(note);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    protected ResponseEntity<?> create(@RequestBody CreateNoteDto createNoteDto) {
        Note note = noteService.create(createNoteDto);

        return ResponseEntity.ok(note);
    }

    @DeleteMapping("/{noteId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    protected ResponseEntity<?> delete(@PathVariable("noteId") Long id) {

        try {
            noteService.delete(id);
        } catch (NoNoteFoundException e) {
            return ResponseEntity.ok(e.getMessage());
        }

        return ResponseEntity.ok().build();
    }

}
