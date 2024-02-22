package hr.fer.oprpp1.hw05.crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Scanner;

/**
 * Main class for cryptographic operations like sha256 digest calculation,
 * AES 126bit encryption and decryption.
 *
 * @author Danijel Barišić
 */
public class Crypto {

    /**
     *
     * @param args command and its arguments
     */
    public static void main(String[] args) {

        if (args.length == 0) {
            throw new IllegalArgumentException("No command written.");
        }

        String command = args[0];

        switch (command) {
            case "checksha" -> {
                if (args.length != 2) {
                    throw new IllegalArgumentException("Invalid number of arguments for checksha command.");
                }

                String binaryFileName = args[1];

                System.out.println("Please provide expected sha-256 digest for " + binaryFileName + ":");
                System.out.print("> ");
                Scanner sc = new Scanner(System.in);
                String expectedDigest = sc.next();

                sc.close();

                MessageDigest md;
                try {
                    md = MessageDigest.getInstance("sha256");
                } catch (NoSuchAlgorithmException e) {
                    throw new IllegalArgumentException("Unknown digest algorithm");
                }

                Path p = Paths.get("src/main/resources/" + binaryFileName);
                try (InputStream is = Files.newInputStream(p, StandardOpenOption.READ)) {
                    byte[] buff = new byte[4096];

                    while (true) {
                        int r = is.read(buff); //write to buff
                        if (r < 1) break;

                        md.update(buff, 0, r);
                    }
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                    System.exit(1);
                }

                byte[] actualDigest = md.digest();

                System.out.print("Digesting completed. ");
                if (expectedDigest.equals(Util.byteToHex(actualDigest))) {
                    System.out.println("Digest of " + binaryFileName + " matches expected digest.");
                } else {
                    System.out.println("Digest of " + binaryFileName + " does not match the expected digest. " +
                            "Digest was: " + Util.byteToHex(actualDigest));
                }
            }

            case "encrypt", "decrypt" -> {
                if (args.length != 3) {
                    throw new IllegalArgumentException("Invalid number of arguments for encrypt command.");
                }

                boolean encrypt = command.equals("encrypt");

                String sourceFileName = args[1];
                String generatedFileName = args[2];

                System.out.println("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):");
                System.out.print("> ");
                Scanner sc = new Scanner(System.in);
                String keyText = sc.next();

                System.out.println("Please provide initialization vector as hex-encoded text (32 hex-digits):");
                System.out.print("> ");
                String ivText = sc.next();

                sc.close();

                SecretKeySpec keySpec = new SecretKeySpec(Util.hexToByte(keyText), "AES");
                AlgorithmParameterSpec paramSpec = new IvParameterSpec(Util.hexToByte(ivText));

                Cipher cipher;
                try {
                    cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
                    throw new RuntimeException("Invalid cipher instance.");
                }

                try {
                    cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE,
                            keySpec, paramSpec);
                } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
                    throw new RuntimeException("Invalid cipher initialization.");
                }

                Path pInput = Paths.get("src/main/resources/" + sourceFileName);
                Path pOutput = Paths.get("src/main/resources/" + generatedFileName);

                try (InputStream is = Files.newInputStream(pInput, StandardOpenOption.READ);
                     OutputStream os = Files.newOutputStream(pOutput)) {
                    byte[] buff = new byte[4096];

                    while (true) {
                        int r = is.read(buff);

                        /*
                            AES consumes 16 bytes at a time, chipping away from 4096 buffer bytes, internally in update.
                            Encrypt raw text (currently in buff) by writing an encrypted block of text
                            to output stream (block returned by .update()).
                         */

                        byte[] output;
                        if (r == 4096) {
                            output = cipher.update(buff, 0, r);
                        } else {
                            try {
                                if (r > 0) {
                                    output = cipher.doFinal(buff, 0, r);
                                } else {
                                    output = cipher.doFinal();
                                }
                                os.write(output);
                            } catch (BadPaddingException | IllegalBlockSizeException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        }
                        os.write(output);
                    }

                } catch (IOException e) {
                    System.err.println(e.getMessage());
                    System.exit(1);
                }

                System.out.println((encrypt ? "Encryption" : "Decryption") + " completed. Generated file " + generatedFileName
                        + " based on file " + sourceFileName + ".");
            }

            default -> throw new IllegalArgumentException("Unknown command.");
        }
    }
}
