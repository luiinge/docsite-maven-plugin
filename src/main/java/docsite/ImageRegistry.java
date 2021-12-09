package docsite;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class ImageRegistry {

    private final Map<String,String> images = new HashMap<>();
    private final Path imageFolder;
    private final Path source;


    public ImageRegistry(Path imageFolder, Path source) {
        this.imageFolder = imageFolder;
        this.source = source;
    }

    public ImageRegistry(Path imageFolder) {
        this(imageFolder, null);
    }


    public String imageFile(String image) {
        if (source == null) {
            return imageFileFromWorkingDir(image);
        } else {
            return imageFileFromRelativeSource(image);
        }
    }


    private String imageFileFromWorkingDir(String image) {
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



    private String imageFileFromRelativeSource(String image) {
        if (!images.containsKey(image)) {
            String extension = image.substring(image.lastIndexOf('.')+1);
            String id = "image-" + (images.size() + 1) + "." + extension;
            String actualFile = imageFolder.getParent().getParent().relativize(imageFolder.resolve(id)).toString();
            try {
                Path relativeSource = source.getParent().resolve(image);
                ResourceUtil.copyExternalFileWithAnotherName(relativeSource, imageFolder, id);
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
