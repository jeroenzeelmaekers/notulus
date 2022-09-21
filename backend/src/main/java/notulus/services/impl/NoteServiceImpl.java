package notulus.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import notulus.dtos.note.CreateNoteDto;
import notulus.entities.Note;
import notulus.exception.NoNoteFoundException;
import notulus.repositories.NoteRepository;
import notulus.services.NoteService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Jeroen Zeelmaekers
 * @version 0.0.1
 * @since 23/08/2022
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    @Override
    public List<Note> getAll() {
        log.trace("Getting all notes...");
        return noteRepository.findAll();
    }

    @Override
    @Transactional
    public Note create(CreateNoteDto createNoteDto) {
        log.trace("Create new note:{}", createNoteDto);
        Note note = Note.builder().content(createNoteDto.content()).build();
        return noteRepository.save(note);
    }

    @Override
    @Transactional
    public Note updateNote(Long id, String content) throws NoNoteFoundException {
        log.trace("Updating note:{} with content:{}", id, content);
        Note note = noteRepository.findById(id).orElseThrow(NoNoteFoundException::new);
        note.setContent(content);
        return noteRepository.save(note);
    }

    @Override
    @Transactional
    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }

}
