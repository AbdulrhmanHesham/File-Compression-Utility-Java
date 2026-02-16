package file_compression_uillity;

public class CompressionFactory {
    // Factory Pattern
    public static Compressor createCompressor(String type) {
        if (type == null) {
            return null;
        }

        switch (type.toUpperCase()) {
            case "ZIP":
                return new ZipCompressor();
            case "RAR":
                // Adapter Pattern - wraps external RAR command-line tool
                return new RarCompressorAdapter();
            default:
                throw new IllegalArgumentException("Unknown compression type: " + type);
        }
    }

}
