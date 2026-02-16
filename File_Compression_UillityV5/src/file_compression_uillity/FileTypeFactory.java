package file_compression_uillity;

/**
 * Factory Pattern for creating appropriate FileTypeProcessor
 * Returns different processors based on file extension
 */
public class FileTypeFactory {

    /**
     * Factory method to get the appropriate processor for a file type
     * 
     * @param fileType File extension or type
     * @return FileTypeProcessor instance for the file type
     */
    public static FileTypeProcessor getProcessor(String fileType) {
        if (fileType == null) {
            return new DefaultProcessor(); // Return default instead of null
        }

        switch (fileType.toLowerCase()) {
            // Text files
            case "text":
            case "txt":
            case "log":
            case "md":
            case "xml":
            case "json":
            case "csv":
            case "html":
            case "css":
            case "js":
            case "java":
            case "py":
            case "c":
            case "cpp":
            case "h":
                return new TextProcessor();

            // Image files
            case "image":
            case "png":
            case "jpg":
            case "jpeg":
            case "gif":
            case "bmp":
            case "tiff":
            case "webp":
                return new ImageProcessor();

            // Video files
            case "video":
            case "mp4":
            case "avi":
            case "mkv":
            case "mov":
            case "wmv":
            case "flv":
            case "webm":
                return new VideoProcessor();

            // Unknown/generic files
            default:
                return new DefaultProcessor();
        }
    }

    /**
     * Gets file extension from a file path
     * 
     * @param filePath Full path to the file
     * @return File extension without the dot
     */
    public static String getFileExtension(String filePath) {
        if (filePath == null || !filePath.contains(".")) {
            return "";
        }
        return filePath.substring(filePath.lastIndexOf(".") + 1);
    }
}
