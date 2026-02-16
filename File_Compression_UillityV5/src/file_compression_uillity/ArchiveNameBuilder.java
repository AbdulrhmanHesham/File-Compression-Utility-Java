package file_compression_uillity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Builder Pattern Implementation
 * 
 * This builder constructs archive filenames step-by-step with optional components.
 * The Builder pattern separates the construction of a complex object from its representation.
 * 
 * Use case: Build custom output archive names with optional parts like
 * timestamp, prefix, suffix, original filename, compression type, etc.
 * 
 * Example usage:
 *   String archiveName = new ArchiveNameBuilder()
 *       .withPrefix("backup")
 *       .withOriginalFileName("myfile.txt")
 *       .withTimestamp()
 *       .withExtension(".zip")
 *       .build();
 *   // Result: "backup_myfile_20231210_143000.zip"
 */
public class ArchiveNameBuilder {

    private String prefix;
    private String originalFileName;
    private String timestamp;
    private String suffix;
    private String extension;
    private String separator;

    // Constructor - initialize with defaults
    public ArchiveNameBuilder() {
        this.prefix = "";
        this.originalFileName = "";
        this.timestamp = "";
        this.suffix = "";
        this.extension = ".zip";
        this.separator = "_";
    }

    /**
     * Add a prefix to the archive name.
     * Example: "backup" -> "backup_..."
     */
    public ArchiveNameBuilder withPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    /**
     * Include the original filename (without extension) in the archive name.
     * Example: "document.txt" -> "..._document_..."
     */
    public ArchiveNameBuilder withOriginalFileName(String fileName) {
        if (fileName != null && !fileName.isEmpty()) {
            // Remove extension from filename
            int lastDot = fileName.lastIndexOf('.');
            if (lastDot > 0) {
                this.originalFileName = fileName.substring(0, lastDot);
            } else {
                this.originalFileName = fileName;
            }
        }
        return this;
    }

    /**
     * Add current timestamp to the archive name.
     * Format: yyyyMMdd_HHmmss
     * Example: "..._20231210_143000_..."
     */
    public ArchiveNameBuilder withTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        this.timestamp = sdf.format(new Date());
        return this;
    }

    /**
     * Add a custom timestamp format.
     * Example: withCustomTimestamp("yyyy-MM-dd") -> "..._2023-12-10_..."
     */
    public ArchiveNameBuilder withCustomTimestamp(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        this.timestamp = sdf.format(new Date());
        return this;
    }

    /**
     * Add a suffix to the archive name.
     * Example: "final" -> "..._final.zip"
     */
    public ArchiveNameBuilder withSuffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    /**
     * Set the file extension.
     * Default is ".zip"
     * Example: ".tar", ".rar"
     */
    public ArchiveNameBuilder withExtension(String extension) {
        if (extension != null) {
            // Ensure extension starts with a dot
            if (!extension.startsWith(".")) {
                this.extension = "." + extension;
            } else {
                this.extension = extension;
            }
        }
        return this;
    }

    /**
     * Set a custom separator between name parts.
     * Default is "_"
     * Example: "-" -> "backup-file-timestamp.zip"
     */
    public ArchiveNameBuilder withSeparator(String separator) {
        this.separator = separator;
        return this;
    }

    /**
     * Build the final archive name from all components.
     * Combines all non-empty parts with the separator.
     * 
     * @return The constructed archive filename
     */
    public String build() {
        StringBuilder nameBuilder = new StringBuilder();

        // Add prefix
        if (!prefix.isEmpty()) {
            nameBuilder.append(prefix);
        }

        // Add original filename
        if (!originalFileName.isEmpty()) {
            if (nameBuilder.length() > 0) {
                nameBuilder.append(separator);
            }
            nameBuilder.append(originalFileName);
        }

        // Add timestamp
        if (!timestamp.isEmpty()) {
            if (nameBuilder.length() > 0) {
                nameBuilder.append(separator);
            }
            nameBuilder.append(timestamp);
        }

        // Add suffix
        if (!suffix.isEmpty()) {
            if (nameBuilder.length() > 0) {
                nameBuilder.append(separator);
            }
            nameBuilder.append(suffix);
        }

        // If nothing was added, use a default name
        if (nameBuilder.length() == 0) {
            nameBuilder.append("archive");
        }

        // Add extension
        nameBuilder.append(extension);

        return nameBuilder.toString();
    }

    /**
     * Build the full archive path including directory.
     * 
     * @param directory The output directory path
     * @return The complete archive file path
     */
    public String buildWithDirectory(String directory) {
        String fileName = build();
        
        if (directory == null || directory.isEmpty()) {
            return fileName;
        }

        // Ensure directory ends with separator
        if (!directory.endsWith(java.io.File.separator)) {
            directory = directory + java.io.File.separator;
        }

        return directory + fileName;
    }

    /**
     * Reset the builder to default state.
     * Useful for reusing the same builder instance.
     */
    public ArchiveNameBuilder reset() {
        this.prefix = "";
        this.originalFileName = "";
        this.timestamp = "";
        this.suffix = "";
        this.extension = ".zip";
        this.separator = "_";
        return this;
    }
}
