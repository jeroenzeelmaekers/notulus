package notulus.dtos.note;

import lombok.Builder;

/**
 * @author Jeroen Zeelmaekers
 * @version 0.0.1
 * @since 23/08/2022
 */
@Builder
public record CreateNoteDto(String content) {
}
