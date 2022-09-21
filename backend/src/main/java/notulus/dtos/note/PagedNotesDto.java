package notulus.dtos.note;

import notulus.entities.Note;

import java.util.List;

public record PagedNotesDto(Integer number, Integer totalPages, Integer totalElements, Integer size, List<Note> notes) {
}
