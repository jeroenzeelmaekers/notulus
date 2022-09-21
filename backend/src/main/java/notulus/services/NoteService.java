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

    /**
     * @param pageSize the size of the list that is returned
     * @param pageNo   the number of the required mage
     * @param sortBy   the property of the note that is used to sort all the notes
     * @return a list of all the notes from that page
     */
    List<Note> getAll(int pageSize, int pageNo, String sortBy);

    /**
     * @param createNoteDto Dto object that contains the needed properties of the to
     *                      created note
     * @return the newly created note
     */
    Note create(CreateNoteDto createNoteDto);

    /**
     * @param id      of the note that will be updated
     * @param content the updated content of the note
     * @return the update note
     * @throws NoNoteFoundException when an invalid note ID is given
     */
    Note updateNote(Long id, String content) throws NoNoteFoundException;

    /**
     * @param id of the note that needs to be deleted
     * @throws NoNoteFoundException when the given ID of the note is not an ID of a
     *                              existing note
     */
    void deleteNote(Long id) throws NoNoteFoundException;
}
