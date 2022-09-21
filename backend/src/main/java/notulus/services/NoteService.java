package notulus.services;

import notulus.dtos.note.CreateNoteDto;
import notulus.entities.Note;
import notulus.exception.NoNoteFoundException;

import java.util.List;

/**
 * @author Jeroen Zeelmaekers
 * @version 0.0.1
 * @since 23/08/2022
 */
public interface NoteService {

    List<Note> getAll();

    Note create(CreateNoteDto createNoteDto);

    Note updateNote(Long id, String content) throws NoNoteFoundException;

    void deleteNote(Long id) throws NoNoteFoundException;
}
