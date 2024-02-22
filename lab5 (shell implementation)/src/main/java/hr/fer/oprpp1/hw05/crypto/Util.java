package hr.fer.oprpp1.hw05.crypto;

/**
 * Utility class that contains utility methods to help cryptographic operations.
 *
 * @author Danijel Barišić
 */
public class Util {

    /**
     * Converts hex string to byte array.
     *
     * @param keyText hex string to convert
     * @return byte array corresponding to the hex text
     */
    public static byte[] hexToByte(String keyText) {

        if (keyText.length() % 2 != 0 || !keyText.matches("^[A-Fa-f0-9]*$")) {
            throw new IllegalArgumentException("Invalid key text.");
        }

        byte[] result = null;

        String lowercaseKeyText = keyText.toLowerCase();
        if (lowercaseKeyText.length() > 0) {
            result = new byte[lowercaseKeyText.length() / 2];

            for (int i = 0; i < result.length; i++) {
                result[i] = 0;

                char ms = lowercaseKeyText.charAt(i * 2);
                if (Character.isLetter(ms)) {
                    result[i] += (ms - 'a' + 10) << 4;
                } else {
                    result[i] += (ms - '0') << 4;
                }

                char ls = lowercaseKeyText.charAt(i * 2 + 1);
                if (Character.isLetter(ls)) {
                    result[i] += (ls - 'a' + 10);
                } else {
                    result[i] += (ls - '0');
                }
            }
        }
        return result;
    }

    /**
     * Converts byte array to hex string.
     *
     * @param byteArray byte array to convert
     * @return hex string corresponding to the byte array
     */
    public static String byteToHex(byte[] byteArray) {

        StringBuilder sb = new StringBuilder();
        if (byteArray.length > 0) {
            for (byte b : byteArray) {

                int ms = ((int) b << 24) >>> 28;
                int ls = ((int) b << 28) >>> 28;

                ms = (ms >= 10) ? ('a' + ms - 10) : ('0' + ms);
                ls = (ls >= 10) ? ('a' + ls - 10) : ('0' + ls);

                sb.append((char) ms);
                sb.append((char) ls);
            }
        }
        return sb.toString();
    }
}
