package docsite.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import docsite.Docsite;

import java.io.IOException;
import java.nio.file.Path;

public class DocsiteReader {

    private final ObjectMapper jsonMapper = new ObjectMapper()
        .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);


    public Docsite read(Path file) throws IOException {
        return jsonMapper.readValue(file.toFile(), Docsite.class);
    }

}