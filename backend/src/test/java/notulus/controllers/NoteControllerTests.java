package notulus.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thedeanda.lorem.LoremIpsum;
import lombok.SneakyThrows;
import notulus.controllers.v1.NoteController;
import notulus.dtos.note.CreateNoteDto;
import notulus.dtos.note.PagedNotesDto;
import notulus.dtos.note.UpdateNoteDto;
import notulus.entities.Note;
import notulus.exception.NoNoteFoundException;
import notulus.services.NoteService;
import notulus.utils.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ActiveProfiles(value = "test")
@WebMvcTest(NoteController.class)
public class NoteControllerTests {

    private final LoremIpsum loremIpsum;
    private final Random random;
    @MockBean
    private NoteService noteService;
    @Autowired
    private MockMvc mockMvc;

    public NoteControllerTests() {
        loremIpsum = LoremIpsum.getInstance();
        random = new Random();
    }

    @BeforeEach
    public void init(WebApplicationContext context) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void getAll_ShouldReturnAllNotesInTheDb() {
        List<Note> notes = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            notes.add(Note.builder().content(loremIpsum.getWords(random.nextInt(5, 20))).build());
        }

        Page<Note> notePage = new PageImpl<>(notes);
        PagedNotesDto dto = new PagedNotesDto(0, 1, 5, 5, notes);

        when(noteService.getAll(any(Pageable.class))).thenReturn(notePage);

        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/note"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(content().json(JsonUtil.toJson(dto)));
        } catch (JsonProcessingException e) {
            fail("Failed to convert to json");
        } catch (Exception e) {
            e.printStackTrace();
        }

        verify(noteService, times(1)).getAll(any(Pageable.class));
    }

    @Test
    @SneakyThrows
    public void update_ShouldCorrectlyUpdateTheGivenNote() {

        String content = loremIpsum.getWords(random.nextInt(5, 20));
        UpdateNoteDto updateNoteDto = new UpdateNoteDto(1L, content);
        Note updatedNote = new Note(1L, content);

        when(noteService.update(updateNoteDto)).thenReturn(updatedNote);

        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/note/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtil.toJson(updateNoteDto)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(content().json(JsonUtil.toJson(updatedNote)));
        } catch (JsonProcessingException e) {
            fail("Failed to convert to json");
        } catch (Exception e) {
            e.printStackTrace();
        }

        verify(noteService, times(1)).update(updateNoteDto);
    }

    @Test
    @SneakyThrows
    public void update_ShouldReturnOKWithErrorMessage_WhenAInvalidNoteIdIsGiven() {

        String content = loremIpsum.getWords(random.nextInt(5, 20));
        UpdateNoteDto updateNoteDto = new UpdateNoteDto(1L, content);

        when(noteService.update(updateNoteDto)).thenThrow(new NoNoteFoundException("No note found with id:" + updateNoteDto.id()));

        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/note/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtil.toJson(updateNoteDto)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(content().string("No note found with id:" + updateNoteDto.id()));

        } catch (JsonProcessingException e) {
            fail("Failed to convert to json");
        } catch (Exception e) {
            e.printStackTrace();
        }


        verify(noteService, times(1)).update(updateNoteDto);
    }

    @Test
    @SneakyThrows
    public void create_ShouldSuccessfullyCreateAnNewNote() {

        CreateNoteDto createNoteDto = CreateNoteDto.builder().content("Test note").build();
        Note note = Note.builder().id(1L).content("Test note").build();

        when(noteService.create(any(CreateNoteDto.class))).thenReturn(note);

        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/note/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtil.toJson(createNoteDto)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(content().json(JsonUtil.toJson(note)));
        } catch (JsonProcessingException e) {
            fail("Failed to convert to json");
        } catch (Exception e) {
            e.printStackTrace();
        }

        verify(noteService, times(1)).create(createNoteDto);
    }

    @Test
    @SneakyThrows
    public void delete_ShouldSuccessfullyDeleteANoteById() {

        Long id = 1L;
        doNothing().when(noteService).delete(id);

        try {
            mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/note/" + id))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        } catch (JsonProcessingException e) {
            fail("Failed to convert to json");
        } catch (Exception e) {
            e.printStackTrace();
        }

        verify(noteService, times(1)).delete(id);
    }

    @Test
    @SneakyThrows
    public void delete_ShouldReturnOKWithErrorMessage_WhenInvalidNoteIdIsGiven() {

        Long id = 0L;
        doThrow(new NoNoteFoundException("No note found with id:" + id)).when(noteService).delete(id);

        try {
            mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/note/" + id))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(content().string("No note found with id:" + id));
        } catch (JsonProcessingException e) {
            fail("Failed to convert to json");
        } catch (Exception e) {
            e.printStackTrace();
        }

        verify(noteService, times(1)).delete(id);
    }

}
