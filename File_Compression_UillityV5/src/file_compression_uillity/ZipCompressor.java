package file_compression_uillity;

import java.io.*;
import java.util.zip.*;

public class ZipCompressor implements Compressor{
       
   
    
    @Override
    public boolean compress(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("File does not exist: " + filePath);
                return false;
            }
            
            // Create zip file in the SAME directory with .zip extension
            String zipFilePath = filePath.substring(0, filePath.lastIndexOf('.')) + ".zip";
            
            System.out.println("Compressing: " + filePath + " to " + zipFilePath);
            System.out.println("File size: " + file.length() + " bytes");
            
            try (FileOutputStream fos = new FileOutputStream(zipFilePath);
                 ZipOutputStream zos = new ZipOutputStream(fos);
                 FileInputStream fis = new FileInputStream(file)) {
                
                // Create zip entry with the original filename
                ZipEntry zipEntry = new ZipEntry(file.getName());
                zos.putNextEntry(zipEntry);
                
                // Copy file content to zip
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, bytesRead);
                }
                
                zos.closeEntry();
            }
            
            // Verify the zip file was created
            File zipFile = new File(zipFilePath);
            if (zipFile.exists()) {
                System.out.println("Zip file created successfully: " + zipFilePath);
                System.out.println("Zip file size: " + zipFile.length() + " bytes");
                return true;
            } else {
                System.out.println("Zip file was not created!");
                return false;
            }
            
        } catch (IOException e) {
            System.err.println("Compression error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    
    @Override
   public boolean decompress(String filePath) {
    try {
        File zipFile = new File(filePath);
        String outputDir = zipFile.getParent();
        
        // Get zip file name without extension
        String zipFileName = zipFile.getName();
        String folderName = zipFileName.substring(0, zipFileName.lastIndexOf('.'));
        
        // Create folder with the same name as the zip file
        File extractFolder = new File(outputDir + File.separator + folderName);
        extractFolder.mkdir();
        
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry zipEntry = zis.getNextEntry();
            
            while (zipEntry != null) {
                String fileName = zipEntry.getName();
                File newFile = new File(extractFolder + File.separator + fileName);
                
                // Create parent directories if needed
                new File(newFile.getParent()).mkdirs();
                
                // Skip directories
                if (!zipEntry.isDirectory()) {
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
                
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
        }
        
        System.out.println("Files extracted to: " + extractFolder.getAbsolutePath());
        return true;
        
    } catch (IOException e) {
        e.printStackTrace();
        return false;
    }
}


    
    @Override
    public String getExtension() {
        return ".zip";
    }

    @Override
    public boolean compressMultiple(String[] filePaths, String outputArchivePath) {
        try {
            if (filePaths == null || filePaths.length == 0) {
                System.out.println("No files provided for compression");
                return false;
            }
            
            System.out.println("Compressing " + filePaths.length + " files to: " + outputArchivePath);
            
            try (FileOutputStream fos = new FileOutputStream(outputArchivePath);
                 ZipOutputStream zos = new ZipOutputStream(fos)) {
                
                for (String filePath : filePaths) {
                    File file = new File(filePath);
                    
                    if (!file.exists()) {
                        System.out.println("Skipping non-existent file: " + filePath);
                        continue;
                    }
                    
                    // Create unique entry name (use just filename, or full path if needed)
                    String entryName = file.getName();
                    
                    System.out.println("Adding: " + file.getName() + " (" + file.length() + " bytes)");
                    
                    try (FileInputStream fis = new FileInputStream(file)) {
                        ZipEntry zipEntry = new ZipEntry(entryName);
                        zos.putNextEntry(zipEntry);
                        
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, bytesRead);
                        }
                        
                        zos.closeEntry();
                    }
                }
            }
            
            // Verify the zip file was created
            File zipFile = new File(outputArchivePath);
            if (zipFile.exists()) {
                System.out.println("Multi-file zip created successfully: " + outputArchivePath);
                System.out.println("Archive size: " + zipFile.length() + " bytes");
                return true;
            } else {
                System.out.println("Archive was not created!");
                return false;
            }
            
        } catch (IOException e) {
            System.err.println("Multi-file compression error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

}
