package docsite;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class ImageResolver {

    private final Map<String,String> images = new HashMap<>();
    private final Path imageFolder;
    private final Path source;
    private final Logger logger;


    public ImageResolver(Logger logger, Path imageFolder, Path source) {
        this.logger = logger;
        this.imageFolder = imageFolder;
        this.source = source;
    }

    public ImageResolver(Logger logger, Path imageFolder) {
        this(logger, imageFolder, null);
    }


    public String imageFile(String image) {
        try {
            if (source == null) {
                return imageFileFromWorkingDir(image);
            } else {
                return imageFileFromRelativeSource(image);
            }
        } catch (IOException e) {
            logger.warn("Cannot retrieve image {} : {}", image, e.getMessage());
            logger.debug("",e);
            return "";
        }
    }


    private String imageFileFromWorkingDir(String image) throws IOException{
        if (!images.containsKey(image)) {
            String extension = image.substring(image.lastIndexOf('.')+1);
            String id = "image-" + (images.size() + 1) + "." + extension;
            String actualFile = imageFolder.getParent().relativize(imageFolder.resolve(id)).toString();
            ResourceUtil.copyExternalFileWithAnotherName(Path.of(image), imageFolder, id);
            images.put(image, actualFile);
            return actualFile;
        } else {
            return images.get(image);
        }
    }



    private String imageFileFromRelativeSource(String image) throws IOException{
        if (!images.containsKey(image)) {
            String extension = image.substring(image.lastIndexOf('.')+1);
            String id = "image-" + (images.size() + 1) + "." + extension;
            String actualFile = imageFolder.getParent().getParent().relativize(imageFolder.resolve(id)).toString();
            Path relativeSource = source.getParent().resolve(image);
            ResourceUtil.copyExternalFileWithAnotherName(relativeSource, imageFolder, id);
            images.put(image, actualFile);
            return actualFile;
        } else {
            return images.get(image);
        }
    }



}
