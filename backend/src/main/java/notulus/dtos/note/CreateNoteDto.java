package notulus.dtos.note;

import lombok.Builder;

@Builder
public record CreateNoteDto(String content) {
}
