package docsite;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class ImageRegistry {

    private final Map<String,String> images = new HashMap<>();
    private final Path imageFolder;


    public ImageRegistry(Path imageFolder) {
        this.imageFolder = imageFolder;
    }


    public String imageFile(String image) {
        if (!images.containsKey(image)) {
            String extension = image.substring(image.lastIndexOf('.')+1);
            String id = "image-" + (images.size() + 1) + "." + extension;
            String actualFile = imageFolder.getParent().relativize(imageFolder.resolve(id)).toString();
            try {
                ResourceUtil.copyExternalFileWithAnotherName(Path.of(image), imageFolder, id);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            images.put(image, actualFile);
            return actualFile;
        } else {
            return images.get(image);
        }
    }



}
