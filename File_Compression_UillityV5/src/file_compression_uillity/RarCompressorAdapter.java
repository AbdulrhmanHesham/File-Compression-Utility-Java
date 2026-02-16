package file_compression_uillity;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * Adapter Pattern Implementation
 * 
 * This adapter wraps external RAR command-line tools (rar.exe/unrar.exe)
 * and adapts them to work with our existing Compressor interface.
 * 
 * The Adapter pattern allows incompatible interfaces to work together.
 * Here, the external RAR tool has a command-line interface, but our
 * application expects a Compressor interface with compress/decompress methods.
 */
public class RarCompressorAdapter implements Compressor {

    // Path to the RAR executable (can be configured)
    private String rarExecutablePath;
    private String unrarExecutablePath;

    // Default constructor - uses standard WinRAR installation path on Windows
    public RarCompressorAdapter() {
        this.rarExecutablePath = "C:\\Program Files\\WinRAR\\Rar.exe";
        this.unrarExecutablePath = "C:\\Program Files\\WinRAR\\UnRAR.exe";
    }

    // Constructor with custom paths to executables
    public RarCompressorAdapter(String rarPath, String unrarPath) {
        this.rarExecutablePath = rarPath;
        this.unrarExecutablePath = unrarPath;
    }

    /**
     * Adapts the external RAR command-line tool to our compress interface.
     * Translates: compress(filePath) -> "rar a archive.rar file"
     */
    @Override
    public boolean compress(String filePath) {
        try {
            File inputFile = new File(filePath);
            if (!inputFile.exists()) {
                System.err.println("File does not exist: " + filePath);
                return false;
            }

            // Build output archive path (same location, .rar extension)
            String outputPath = filePath + ".rar";

            // Build the RAR command: rar a -ep <archive.rar> <file>
            // "a" command means "add to archive"
            // "-ep" excludes paths from names (stores only filenames, not full paths)
            ProcessBuilder processBuilder = new ProcessBuilder(
                    rarExecutablePath, "a", "-ep", outputPath, filePath);

            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Read and log the output
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[RAR]: " + line);
            }

            int exitCode = process.waitFor();
            return exitCode == 0;

        } catch (Exception e) {
            System.err.println("RAR compression failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Adapts the external UNRAR command-line tool to our decompress interface.
     * Translates: decompress(filePath) -> "unrar e archive.rar <folder>/"
     */
    @Override
    public boolean decompress(String filePath) {
        try {
            File inputFile = new File(filePath);
            if (!inputFile.exists()) {
                System.err.println("Archive does not exist: " + filePath);
                return false;
            }

            // Get the directory where the archive is located
            String parentDir = inputFile.getParent();
            if (parentDir == null) {
                parentDir = ".";
            }

            // Get RAR file name without extension to create output folder
            String rarFileName = inputFile.getName();
            String folderName = rarFileName.substring(0, rarFileName.lastIndexOf('.'));

            // Create folder with the same name as the RAR file (like ZIP does)
            File extractFolder = new File(parentDir + File.separator + folderName);
            extractFolder.mkdir();

            // Build the UNRAR command: unrar e <archive.rar> <output_folder>/
            // "e" command means "extract without full path" (flat extraction)
            ProcessBuilder processBuilder = new ProcessBuilder(
                    unrarExecutablePath, "e", "-y", filePath, extractFolder.getAbsolutePath() + File.separator);

            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Read and log the output
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[UNRAR]: " + line);
            }

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("Files extracted to: " + extractFolder.getAbsolutePath());
            }

            return exitCode == 0;

        } catch (Exception e) {
            System.err.println("RAR decompression failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String getExtension() {
        return ".rar";
    }

    /**
     * Adapts multi-file compression to RAR command-line.
     * Translates: compressMultiple(files, output) -> "rar a archive.rar file1 file2
     * ..."
     */
    @Override
    public boolean compressMultiple(String[] filePaths, String outputArchivePath) {
        try {
            // Validate input files
            for (String filePath : filePaths) {
                File file = new File(filePath);
                if (!file.exists()) {
                    System.err.println("File does not exist: " + filePath);
                    return false;
                }
            }

            // Build the RAR command with multiple files
            // Command format: rar a -ep <archive.rar> <file1> <file2> ...
            // "-ep" excludes paths from names (stores only filenames, not full paths)
            java.util.List<String> command = new java.util.ArrayList<>();
            command.add(rarExecutablePath);
            command.add("a");
            command.add("-ep");
            command.add(outputArchivePath);
            for (String filePath : filePaths) {
                command.add(filePath);
            }

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Read and log the output
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[RAR]: " + line);
            }

            int exitCode = process.waitFor();
            return exitCode == 0;

        } catch (Exception e) {
            System.err.println("RAR multi-file compression failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
