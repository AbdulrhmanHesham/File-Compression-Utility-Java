package file_compression_uillity;

//Singleton Pattern
//Lazy Singleton 

import java.io.File;

public class CompressionManager {
    private static CompressionManager instance;

    // Private constructor to prevent instantiation
    private CompressionManager() {
    }

    // Singleton instance getter
    public static CompressionManager getInstance() {
        if (instance == null) {
            instance = new CompressionManager();
        }
        return instance;
    }

    // Compression method
    public boolean compressFile(String filePath, Compressor compressor, javax.swing.JProgressBar progressBar) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return false;
            }

            // Simulate progress updates
            if (progressBar != null) {
                for (int i = 0; i <= 100; i += 10) {
                    progressBar.setValue(i);
                    Thread.sleep(100); // Simulate work
                }
            }

            // Perform actual compression
            return compressor.compress(filePath);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Decompression method
    public boolean decompressFile(String filePath, Compressor compressor, javax.swing.JProgressBar progressBar) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return false;
            }

            // Simulate progress updates
            if (progressBar != null) {
                for (int i = 0; i <= 100; i += 10) {
                    progressBar.setValue(i);
                    Thread.sleep(100); // Simulate work
                }
            }

            // Perform actual decompression
            return compressor.decompress(filePath);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Multi-file compression method
    public boolean compressFiles(String[] filePaths, String outputArchivePath, Compressor compressor,
            javax.swing.JProgressBar progressBar) {
        try {
            if (filePaths == null || filePaths.length == 0) {
                return false;
            }

            // Validate all files exist
            for (String filePath : filePaths) {
                File file = new File(filePath);
                if (!file.exists()) {
                    System.err.println("File does not exist: " + filePath);
                    return false;
                }
            }

            // Simulate progress updates
            if (progressBar != null) {
                for (int i = 0; i <= 100; i += 10) {
                    progressBar.setValue(i);
                    Thread.sleep(100); // Simulate work
                }
            }

            // Perform actual multi-file compression
            return compressor.compressMultiple(filePaths, outputArchivePath);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
