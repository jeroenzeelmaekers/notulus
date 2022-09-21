package notulus.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thedeanda.lorem.LoremIpsum;
import lombok.SneakyThrows;
import notulus.controllers.v1.NoteController;
import notulus.dtos.note.CreateNoteDto;
import notulus.dtos.note.PagedNotesDto;
import notulus.entities.Note;
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

@ActiveProfiles(value = "test")
@WebMvcTest(NoteController.class)
public class NoteControllerTests {

    @MockBean
    private NoteService noteService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void init(WebApplicationContext context) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @SneakyThrows
    public void create_ShouldSuccessfullyCreateANNewNote() {

        CreateNoteDto createNoteDto = CreateNoteDto.builder().content("Test note").build();
        Note note = Note.builder().id(1L).content("Test note").build();

        when(noteService.create(any(CreateNoteDto.class))).thenReturn(note);

        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/note/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtil.toJson(createNoteDto)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(JsonUtil.toJson(note)));
        } catch (JsonProcessingException e) {
            fail("Failed to convert to json");
        } catch (Exception e) {
            e.printStackTrace();
        }

        verify(noteService, times(1)).create(createNoteDto);
    }

    @Test
    public void getAll_ShouldReturnAllNotesInTheDb() {
        List<Note> notes = new ArrayList<>();

        LoremIpsum lorum = LoremIpsum.getInstance();
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            notes.add(Note.builder().content(lorum.getWords(random.nextInt(5, 20))).build());
        }

        Page<Note> notePage = new PageImpl<>(notes);
        PagedNotesDto dto = new PagedNotesDto(0, 1, 5, 5, notes);

        when(noteService.getAll(any(Pageable.class))).thenReturn(notePage);

        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/note"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(JsonUtil.toJson(dto)));
        } catch (JsonProcessingException e) {
            fail("Failed to convert to json");
        } catch (Exception e) {
            e.printStackTrace();
        }

        verify(noteService, times(1)).getAll(any(Pageable.class));
    }

}
