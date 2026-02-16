package file_compression_uillity;

/**
 * Video File Processor - Factory Pattern Implementation
 * Handles video files with format validation
 */
public class VideoProcessor implements FileTypeProcessor {

    @Override
    public byte[] prepareForCompression(byte[] data) {
        try {
            // Check video file signatures (magic numbers)
            String fileType = detectVideoFormat(data);

            if (fileType != null) {
                System.out.println("VideoProcessor: Processing " + fileType + " video file");
                System.out.println("VideoProcessor: File size: " + data.length + " bytes");
            } else {
                System.out.println("VideoProcessor: Unknown video format, processing as binary");
            }

            // For videos, we don't modify the data as video formats are already compressed
            // Video re-encoding would require complex libraries and would be lossy
            return data;

        } catch (Exception e) {
            System.out.println("VideoProcessor: Error processing video, returning original data");
            return data;
        }
    }

    /**
     * Detects video format based on file signature (magic numbers)
     */
    private String detectVideoFormat(byte[] data) {
        if (data.length < 12)
            return null;

        // MP4 signature: "ftyp" at bytes 4-7
        if (data.length > 8 &&
                data[4] == 'f' && data[5] == 't' &&
                data[6] == 'y' && data[7] == 'p') {
            return "MP4";
        }

        // AVI signature: "RIFF" at start and "AVI " at bytes 8-11
        if (data[0] == 'R' && data[1] == 'I' &&
                data[2] == 'F' && data[3] == 'F' &&
                data[8] == 'A' && data[9] == 'V' &&
                data[10] == 'I' && data[11] == ' ') {
            return "AVI";
        }

        // MKV signature: "matroska" in header
        String header = new String(data, 0, Math.min(40, data.length));
        if (header.contains("matroska")) {
            return "MKV";
        }

        return "Unknown Video";
    }

    @Override
    public String getFileType() {
        return "Video";
    }
}
