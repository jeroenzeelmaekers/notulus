package notulus.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.text.SimpleDateFormat;

/**
 * @author Jeroen Zeelmaekers
 * @version 0.0.1
 * @since 23/08/2022
 */
public class JsonUtil {

    private static final ObjectWriter objectWriter = new ObjectMapper()
            .setDateFormat(new SimpleDateFormat("yyyy-MM--dd"))
            .writer()
            .withDefaultPrettyPrinter();

    public static String toJson(Object object) throws JsonProcessingException {
        return objectWriter.writeValueAsString(object);
    }

}
