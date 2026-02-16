package file_compression_uillity;

/**
 * Default File Processor - Factory Pattern Implementation
 * Handles unknown/generic file types
 */
public class DefaultProcessor implements FileTypeProcessor {

    @Override
    public byte[] prepareForCompression(byte[] data) {
        System.out.println("DefaultProcessor: Processing generic binary file (" + data.length + " bytes)");
        // No preprocessing for unknown files
        return data;
    }

    @Override
    public String getFileType() {
        return "Generic";
    }
}
