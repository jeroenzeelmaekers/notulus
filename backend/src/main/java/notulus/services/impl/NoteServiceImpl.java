package notulus.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import notulus.dtos.note.CreateNoteDto;
import notulus.dtos.note.UpdateNoteDto;
import notulus.entities.Note;
import notulus.exception.NoNoteFoundException;
import notulus.repositories.NoteRepository;
import notulus.services.NoteService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = {"notes"})
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    @Override
    @SneakyThrows
    @Cacheable
    public Page<Note> getAll(Pageable pageable) {
        log.info("Getting all notes...");

        return noteRepository.findAll(pageable);
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public Note create(CreateNoteDto createNoteDto) {
        log.info("Create new note:{}", createNoteDto);

        Note note = Note.builder().content(createNoteDto.content()).build();

        return noteRepository.save(note);
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public Note update(UpdateNoteDto updateNoteDto) throws NoNoteFoundException {
        log.info("Updating note:{} with content:{}", updateNoteDto.id(), updateNoteDto.content());

        Note note = noteRepository.findById(updateNoteDto.id()).orElseThrow(() -> new NoNoteFoundException("No note found with id:" + updateNoteDto.id()));
        note.setContent(updateNoteDto.content());

        return noteRepository.save(note);
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public void delete(Long id) throws NoNoteFoundException {
        noteRepository.findById(id).orElseThrow(() -> new NoNoteFoundException("No note found with id:" + id));
        log.info("Deleting note with id:{}", id);

        noteRepository.deleteById(id);
    }

}
