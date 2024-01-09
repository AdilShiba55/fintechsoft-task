package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class UtFile {
    public static String getContent(File file) throws IOException {
        return new String(Files.readAllBytes(file.toPath()));
    }
    public static List<String> getContentAsList(File file) throws IOException {
        return Files.readAllLines(file.toPath());
    }
    public static void writeWithRemove(File file, String content) throws IOException {
        FileWriter fileWriter = new FileWriter(file.toPath().toString());
        fileWriter.write(content);
        fileWriter.close();
    }
}
