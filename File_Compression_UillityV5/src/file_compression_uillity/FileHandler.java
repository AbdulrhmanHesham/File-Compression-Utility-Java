package file_compression_uillity;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

//Singleton Pattern
//Lazy Singleton Pattern
public class FileHandler {
    
    private static FileHandler instance;

    private FileHandler() {}

    public static FileHandler getInstance() {
        if (instance == null) {
            instance = new FileHandler();
        }
        return instance;
    }

    // Read file into bytes
    public byte[] readFile(String filePath) {
        try {
            return Files.readAllBytes(Path.of(filePath));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Write bytes to a file
    public boolean writeFile(String filePath, byte[] data) {
        try {
            Files.write(Path.of(filePath), data);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Check if file exists
    public boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }
}
