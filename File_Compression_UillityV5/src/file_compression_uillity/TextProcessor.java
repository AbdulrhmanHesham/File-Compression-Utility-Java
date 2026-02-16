package file_compression_uillity;

import java.nio.charset.StandardCharsets;

/**
 * Text File Processor - Factory Pattern Implementation
 * Optimizes text files for better compression
 */
public class TextProcessor implements FileTypeProcessor {

    @Override
    public byte[] prepareForCompression(byte[] data) {
        try {
            // Convert bytes to string
            String text = new String(data, StandardCharsets.UTF_8);

            // Text optimizations for better compression:
            // 1. Normalize line endings to \n (Unix style)
            text = text.replace("\r\n", "\n").replace("\r", "\n");

            // 2. Remove trailing whitespace from each line
            String[] lines = text.split("\n");
            StringBuilder optimized = new StringBuilder();
            for (String line : lines) {
                optimized.append(line.replaceAll("\\s+$", "")).append("\n");
            }

            // 3. Remove multiple consecutive blank lines (keep max 2)
            String result = optimized.toString().replaceAll("\n{3,}", "\n\n");

            return result.getBytes(StandardCharsets.UTF_8);

        } catch (Exception e) {
            System.out.println("TextProcessor: Error processing text, returning original data");
            return data; // Return original if processing fails
        }
    }

    @Override
    public String getFileType() {
        return "Text";
    }
}
