package notulus.repositories;

import notulus.entities.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Jeroen Zeelmaekers
 * @version 0.0.1
 * @since 23/08/2022
 */
@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
}
