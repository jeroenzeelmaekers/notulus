package notulus.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import notulus.dtos.note.CreateNoteDto;
import notulus.entities.Note;
import notulus.exception.NoNoteFoundException;
import notulus.repositories.NoteRepository;
import notulus.services.NoteService;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jeroen Zeelmaekers
 * @version 0.0.1
 * @since 23/08/2022
 */
@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = { "notes" })
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    /*
     * (non-Javadoc)
     * 
     * @see notulus.services.NoteService#getAll(int, int, java.lang.String)
     */
    @Override
    @Cacheable
    @SneakyThrows
    public List<Note> getAll(int pageSize, int pageNo, String sortBy) {
        log.info("Getting all notes...");

        // Sleep 1 sec to notice if the respose was pulled from cache or not
        Thread.sleep(1000);

        PageRequest paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<Note> pagedResult = noteRepository.findAll(paging);

        if (pagedResult.hasContent())
            return pagedResult.getContent();

        return new ArrayList<Note>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see notulus.services.NoteService#create(notulus.dtos.note.CreateNoteDto)
     */
    @Override
    @Transactional
    public Note create(CreateNoteDto createNoteDto) {
        log.info("Create new note:{}", createNoteDto);

        Note note = Note.builder().content(createNoteDto.content()).build();

        return noteRepository.save(note);
    }

    /*
     * (non-Javadoc)
     * 
     * @see notulus.services.NoteService#updateNote(java.lang.Long,
     * java.lang.String)
     */
    @Override
    @Transactional
    public Note updateNote(Long id, String content) throws NoNoteFoundException {
        log.info("Updating note:{} with content:{}", id, content);

        Note note = noteRepository.findById(id).orElseThrow(NoNoteFoundException::new);
        note.setContent(content);

        return noteRepository.save(note);
    }

    /*
     * (non-Javadoc)
     * 
     * @see notulus.services.NoteService#deleteNote(java.lang.Long)
     */
    @Override
    @Transactional
    public void deleteNote(Long id) {
        log.info("Deleting note with id:{}", id);

        noteRepository.deleteById(id);
    }

}
