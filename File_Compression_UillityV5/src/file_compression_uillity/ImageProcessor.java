package file_compression_uillity;

import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * Image File Processor - Factory Pattern Implementation
 * Handles image files with metadata extraction
 */
public class ImageProcessor implements FileTypeProcessor {

    @Override
    public byte[] prepareForCompression(byte[] data) {
        try {
            // Read image to extract metadata
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            BufferedImage image = ImageIO.read(bais);

            if (image != null) {
                int width = image.getWidth();
                int height = image.getHeight();
                int pixels = width * height;

                System.out.println("ImageProcessor: Processing image - "
                        + width + "x" + height + " (" + pixels + " pixels)");

                // For images, we don't modify the data as image formats are already optimized
                // But we could add metadata or watermarking here in the future
            } else {
                System.out.println("ImageProcessor: Could not read image metadata, processing as binary");
            }

            return data; // Return original image data

        } catch (Exception e) {
            System.out.println("ImageProcessor: Error reading image, processing as binary");
            return data; // Return original if processing fails
        }
    }

    @Override
    public String getFileType() {
        return "Image";
    }
}
