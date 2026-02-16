package file_compression_uillity;

/**
 * Factory Pattern Interface for File Type Processing
 * Different implementations handle Text, Image, and Video files differently
 */
public interface FileTypeProcessor {
    /**
     * Prepares file data for compression based on file type
     * @param data Raw file data
     * @return Processed data optimized for compression
     */
    byte[] prepareForCompression(byte[] data);
    
    /**
     * Gets the file type category this processor handles
     * @return File type name (e.g., "Text", "Image", "Video")
     */
    String getFileType();
}
