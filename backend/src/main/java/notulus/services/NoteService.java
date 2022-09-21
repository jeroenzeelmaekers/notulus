package notulus.services;

import notulus.dtos.note.CreateNoteDto;
import notulus.dtos.note.UpdateNoteDto;
import notulus.entities.Note;
import notulus.exception.NoNoteFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoteService {

    Page<Note> getAll(Pageable pageable);

    Note create(CreateNoteDto createNoteDto);

    Note update(UpdateNoteDto updateNoteDto) throws NoNoteFoundException;

    void delete(Long id) throws NoNoteFoundException;
}
