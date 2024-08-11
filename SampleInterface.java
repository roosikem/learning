import java.io.*;
import java.nio.file.*;

public class DirectoryReader {

    public static void main(String[] args) {
        String directoryPath = "/path/to/directory";
        String outputFilePath = "combined_output.txt";
        String encryptionKey = "YourSecretEncryptionKey";

        try {
            // Step 1: Write all files to a single file
            writeFilesToSingleFile(directoryPath, outputFilePath);

            // Step 2: Read the file content and encrypt it
            String fileContent = new String(Files.readAllBytes(Paths.get(outputFilePath)));
            String encryptedContent = AESUtils.encrypt(fileContent, encryptionKey);

            // Step 3: Save the encrypted content back to the file
            Files.write(Paths.get(outputFilePath), encryptedContent.getBytes());

            System.out.println("Files have been encrypted and written to " + outputFilePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeFilesToSingleFile(String directoryPath, String outputFilePath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));
        Files.walk(Paths.get(directoryPath))
                .filter(Files::isRegularFile)
                .forEach(filePath -> {
                    try {
                        writer.write("---START OF FILE---\n");
                        writer.write("FilePath: " + filePath.toString() + "\n");
                        writer.write("---CONTENT---\n");
                        Files.lines(filePath).forEach(line -> {
                            try {
                                writer.write(line + "\n");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        writer.write("---END OF FILE---\n\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        writer.close();
    }
}

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESUtils {

    private static final String ALGORITHM = "AES";

    public static String encrypt(String data, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    public static String decrypt(String encryptedData, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedData = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedData = cipher.doFinal(decodedData);
        return new String(decryptedData);
    }

    public static String generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(128); // AES 128-bit
        SecretKey secretKey = keyGen.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }
}


public class KeyGeneratorExample {
    public static void main(String[] args) {
        try {
            String key = AESUtils.generateKey();
            System.out.println("Generated AES Key: " + key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
