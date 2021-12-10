package docsite;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public final class ResourceUtil {

    private ResourceUtil() {   }


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


    static void copyResourceFolder(String folderName, Path outputFolder) throws IOException {
        Path targetFolder = outputFolder.toAbsolutePath().resolve(folderName);
        for (String file : ResourceUtil.getResourceFiles(folderName)) {
            copyResource(folderName+"/"+file, targetFolder);
        }
    }


    static void copyResource(String resource, Path outputFolder) throws IOException {
        URL resourceUrl = Thread.currentThread().getContextClassLoader().getResource(resource);
        if (resourceUrl == null) {
            throw new FileNotFoundException(resource);
        }
        copyFromURLToFolder(resourceUrl, outputFolder);
    }

    static void copyExternalFile(Path path, Path outputFolder) throws IOException {
        copyFromURLToFolder(path.toUri().toURL(), outputFolder);
    }


    public static void copyExternalFileWithAnotherName(Path path, Path outputFolder, String newName)
    throws IOException {
        outputFolder = outputFolder.toAbsolutePath();
        Path target = outputFolder.resolve(newName);
        copyFromURL(path.toUri().toURL(), target);
    }


    private static void copyFromURLToFolder(URL url, Path outputFolder) throws IOException {
        outputFolder = outputFolder.toAbsolutePath();
        Path target = outputFolder.resolve(Path.of(url.getFile()).getFileName().toString());
        copyFromURL(url, target);
    }


    private static void copyFromURL(URL url, Path target) throws IOException {

        Files.createDirectories(target.getParent());
        Files.copy(
            url.openStream(),
            target,
            StandardCopyOption.REPLACE_EXISTING
        );
    }



    public static String read(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader( new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }


    public static InputStream open(String source) throws IOException {
        try {
            return new URL(source).openStream();
        } catch (MalformedURLException e) {
            return Files.newInputStream(Path.of(source));
        }
    }


    public static boolean existsSource(String source) {
        if (source == null || source.isBlank()) {
            return false;
        }
        if (Files.exists(Path.of(source))) {
            return true;
        }
        try (InputStream stream = new URL(source).openStream()) {
            return stream != null;
        } catch (IOException e) {
            return false;
        }
    }


    public static void copyFolder(Path siteFolder, Path outputFolder)  {
        try(Stream<Path> walker = Files.walk(siteFolder)) {
            Files.createDirectory(outputFolder);
            walker.forEach(sourcePath -> {
                try {
                    Path targetPath = outputFolder.resolve(siteFolder.relativize(sourcePath));
                    Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
