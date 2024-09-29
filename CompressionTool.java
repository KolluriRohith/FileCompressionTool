import java.io.*;
import java.util.zip.*;

public class CompressionTool {

    // Method to compress a file
    public static void compress(String sourceFilePath, String destinationZipPath) {
        try (FileOutputStream fos = new FileOutputStream(destinationZipPath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
             
            File fileToCompress = new File(sourceFilePath);
            try (FileInputStream fis = new FileInputStream(fileToCompress)) {
                ZipEntry zipEntry = new ZipEntry(fileToCompress.getName());
                zos.putNextEntry(zipEntry);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) >= 0) {
                    zos.write(buffer, 0, length);
                }
            }
            System.out.println("File compressed successfully to: " + destinationZipPath);
        } catch (IOException e) {
            System.err.println("Error while compressing: " + e.getMessage());
        }
    }

    // Method to decompress a ZIP file
    public static void decompress(String zipFilePath, String destDir) {
        File dir = new File(destDir);
        if (!dir.exists()) dir.mkdirs();
        
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                File newFile = new File(destDir, zipEntry.getName());
                
                if (zipEntry.isDirectory()) {
                    newFile.mkdirs();
                } else {
                    new File(newFile.getParent()).mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = zis.read(buffer)) >= 0) {
                            fos.write(buffer, 0, length);
                        }
                    }
                }
                zis.closeEntry();
            }
            System.out.println("File decompressed successfully to: " + destDir);
        } catch (IOException e) {
            System.err.println("Error while decompressing: " + e.getMessage());
        }
    }

    // Main method to test the functionality
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java CompressionTool <compress/decompress> <source> <destination>");
            return;
        }

        String command = args[0];
        String source = args[1];
        String destination = args[2];

        if (command.equalsIgnoreCase("compress")) {
            compress(source, destination);
        } else if (command.equalsIgnoreCase("decompress")) {
            decompress(source, destination);
        } else {
            System.out.println("Invalid command. Use 'compress' or 'decompress'.");
        }
    }
}
