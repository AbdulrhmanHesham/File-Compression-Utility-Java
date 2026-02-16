package file_compression_uillity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Prototype Pattern Implementation
 * 
 * This class represents a file selection that can be cloned/duplicated.
 * The Prototype pattern allows creating new objects by copying existing ones.
 * 
 * Use case: Users can save their current file selection and clone it
 * to create variations without rebuilding from scratch.
 */
public class FileSelection implements Cloneable {

    private String selectionName;
    private List<File> selectedFiles;
    private String compressionType;
    private String outputDirectory;

    // Default constructor
    public FileSelection() {
        this.selectedFiles = new ArrayList<>();
        this.selectionName = "Untitled Selection";
        this.compressionType = "ZIP";
        this.outputDirectory = "";
    }

    // Constructor with name
    public FileSelection(String name) {
        this();
        this.selectionName = name;
    }

    /**
     * Prototype Pattern - Clone method
     * Creates a deep copy of this FileSelection object.
     * 
     * @return A new FileSelection with the same data
     */
    @Override
    public FileSelection clone() {
        try {
            FileSelection cloned = (FileSelection) super.clone();
            
            // Deep copy the file list (not just reference copy)
            cloned.selectedFiles = new ArrayList<>();
            for (File file : this.selectedFiles) {
                cloned.selectedFiles.add(new File(file.getAbsolutePath()));
            }
            
            // Update the name to indicate it's a copy
            cloned.selectionName = this.selectionName + " (Copy)";
            
            return cloned;
            
        } catch (CloneNotSupportedException e) {
            // This should never happen since we implement Cloneable
            throw new RuntimeException("Clone failed", e);
        }
    }

    // --- Getters and Setters ---

    public String getSelectionName() {
        return selectionName;
    }

    public void setSelectionName(String selectionName) {
        this.selectionName = selectionName;
    }

    public List<File> getSelectedFiles() {
        return selectedFiles;
    }

    public void setSelectedFiles(List<File> selectedFiles) {
        this.selectedFiles = selectedFiles;
    }

    // Convenience method to set files from array
    public void setSelectedFiles(File[] files) {
        this.selectedFiles = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                this.selectedFiles.add(file);
            }
        }
    }

    public void addFile(File file) {
        if (file != null) {
            this.selectedFiles.add(file);
        }
    }

    public void removeFile(File file) {
        this.selectedFiles.remove(file);
    }

    public void clearFiles() {
        this.selectedFiles.clear();
    }

    public String getCompressionType() {
        return compressionType;
    }

    public void setCompressionType(String compressionType) {
        this.compressionType = compressionType;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public int getFileCount() {
        return selectedFiles.size();
    }

    public File[] getFilesAsArray() {
        return selectedFiles.toArray(new File[0]);
    }

    public String[] getFilePathsAsArray() {
        String[] paths = new String[selectedFiles.size()];
        for (int i = 0; i < selectedFiles.size(); i++) {
            paths[i] = selectedFiles.get(i).getAbsolutePath();
        }
        return paths;
    }

    @Override
    public String toString() {
        return selectionName + " (" + selectedFiles.size() + " files, " + compressionType + ")";
    }
}
