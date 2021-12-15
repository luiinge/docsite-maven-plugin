package docsite.util;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;
import docsite.DocsiteException;

public final class ResourceUtil {

    private ResourceUtil() {   }

    private static final Map<String,List<String>> STATIC_RESOURCES = Map.of(
        "css", List.of(
            "common.css",
            "font-awesome-all.css",
            "prism-light.css"
        ),
        "js", List.of(
            "prism.js"
        ),
        "webfonts", List.of(
            "fa-brands-400.eot",
            "fa-brands-400.svg",
            "fa-brands-400.ttf",
            "fa-brands-400.woff",
            "fa-brands-400.woff2",
            "fa-regular-400.eot",
            "fa-regular-400.svg",
            "fa-regular-400.ttf",
            "fa-regular-400.woff",
            "fa-regular-400.woff2",
            "fa-solid-900.eot",
            "fa-solid-900.svg",
            "fa-solid-900.ttf",
            "fa-solid-900.woff",
            "fa-solid-900.woff2"
        )
    );


    public static List<String> getResourceFiles(String path) {
        return STATIC_RESOURCES.get(path);
    }


    public static void copyResourceFolder(String folderName, Path outputFolder) throws IOException {
        Path targetFolder = outputFolder.toAbsolutePath().resolve(folderName);
        for (String file : ResourceUtil.getResourceFiles(folderName)) {
            copyResource(folderName+"/"+file, targetFolder);
        }
    }


    public static boolean existsResource(String resource) {
        return Thread.currentThread().getContextClassLoader().getResource(resource) != null;
    }


    public static void copyResource(String resource, Path outputFolder) throws IOException {
        URL resourceUrl = Thread.currentThread().getContextClassLoader().getResource(resource);
        if (resourceUrl == null) {
            throw new FileNotFoundException(resource);
        }
        copyFromURLToFolder(resourceUrl, outputFolder);
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
                    throw new DocsiteException(e);
                }
            });
        } catch (IOException e) {
            throw new DocsiteException(e);
        }
    }


    public static void deleteDirectory(Path path) {
        try(Stream<Path> walker = Files.walk(path)) {
            walker
                .sorted(Comparator.reverseOrder())
                .forEach(sourcePath -> {
                    try {
                        Files.delete(sourcePath);
                    } catch (IOException e) {
                        throw new DocsiteException(e);
                    }
                });
        } catch (IOException e) {
            throw new DocsiteException(e);
        }
    }

}
