package file_compression_uillity;

public interface Compressor {
    
    boolean compress(String filePath);
    boolean decompress(String filePath);
    String getExtension();
    
    // Multi-file compression - compress multiple files into a single archive
    boolean compressMultiple(String[] filePaths, String outputArchivePath);
}
