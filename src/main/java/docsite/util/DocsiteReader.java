package docsite.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import docsite.Docsite;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public class DocsiteReader {

    private final ObjectMapper jsonMapper = new ObjectMapper()
        .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);


    public Docsite read(Path file) throws IOException {
        return jsonMapper.readValue(file.toFile(), Docsite.class);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Map<String,String>> readLocalization(Path file) throws IOException {
        return jsonMapper.readValue(file.toFile(), Map.class);
    }

}