package notulus.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.text.SimpleDateFormat;

public class JsonUtil {

    private static final ObjectWriter objectWriter = new ObjectMapper()
            .setDateFormat(new SimpleDateFormat("yyyy-MM--dd"))
            .writer()
            .withDefaultPrettyPrinter();

    public static String toJson(Object object) throws JsonProcessingException {
        return objectWriter.writeValueAsString(object);
    }

}
