package docsite;

import java.io.*;
import java.util.*;

public class ResourceUtil {


    public static List<String> getResourceFiles(String path) throws IOException {
        List<String> filenames = new ArrayList<>();
        try (
            InputStream in = getResourceAsStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(in))
        ) {
            String resource;
            while ((resource = br.readLine()) != null) {
                filenames.add(resource);
            }
        }
        return filenames;
    }


    private static InputStream getResourceAsStream(String resource) {
        InputStream in = classLoader().getResourceAsStream(resource);
        return in == null ? ResourceUtil.class.getResourceAsStream(resource) : in;
    }


    private static ClassLoader classLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

}
