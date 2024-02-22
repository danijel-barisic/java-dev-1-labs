package hr.fer.oprpp1.hw05.shell;

/**
 * Utility class for shell.
 *
 * @author Danijel Barišić
 */
public class Util {

    /**
     * @param quotedString string without surrounding quotes
     * @return escaped string of path name
     */
    public static String getEscapedPathName(String quotedString) {

        return quotedString.replace("\\\"", "\"")
                .replace("\\\\", "\\");
    }


    /**
     * Pass in arguments string,
     * and this returns the first complete argument in arguments after beginIndex
     * whether it's quoted or unquoted, and it's returned without quotes
     *
     * @param env        environment for command
     * @param arguments  arguments of command
     * @param beginIndex beginning of observed string snippet to extract quoted strings from, if there are quotes
     * @param skipCount  number of escaped characters and surrounding quotes
     * @return actual path name argument
     */
    public static String extractRawPathName(Environment env, String arguments, int beginIndex, IntWrapper skipCount) {

        boolean quoted = false;

        String argument;

        int pathEndIndex = 0; //index after closing '"' i.e. on space, or index after path

        if (arguments.substring(beginIndex).startsWith("\"")) {
            quoted = true;

            // extracts first quoted thing, "ignores" escaped quotes
            int currentIndex;
            for (currentIndex = beginIndex + 1; currentIndex < arguments.length(); currentIndex++) { //looking for closing "
                if (arguments.charAt(currentIndex) == '\"') { // opening quote

                    int numOfBackslash = 0;
                    for (int i = currentIndex - 1; i >= beginIndex + 1; i--) {
                        if (arguments.charAt(i) == '\\') {
                            numOfBackslash++;
                        } else {
                            break;
                        }
                    }
                    if (numOfBackslash % 2 != 0) { // if odd, quote is escaped, otherwise it's closing quote
                        continue;
                    } else {
                        //closing quote
                        //pathEndIndex pointing on space after quote
                        pathEndIndex = currentIndex + 1;
                        break;
                    }
                }
            }

            if (currentIndex >= arguments.length()) {
                env.writeln("Error: unclosed quote.");
                return null;
            }
        } else {
            int indexOfFirstSpace = arguments.substring(beginIndex).indexOf(" ");

            // either index after path, or on next space
            pathEndIndex = indexOfFirstSpace == -1 ? arguments.length() : beginIndex + indexOfFirstSpace;
        }

        String rawTextWithinQuotes = arguments.substring((quoted ? beginIndex + 1 : beginIndex),
                pathEndIndex - (quoted ? 1 : 0)).strip();

        argument = Util.getEscapedPathName(rawTextWithinQuotes);
        skipCount.setValue(countEscapes(rawTextWithinQuotes) + (quoted ? 2 : 0));

        return argument;
    }

    /**
     * Pass in arguments string,
     * and this returns the first complete argument in arguments after beginIndex
     * whether it's quoted or unquoted, and it's returned without quotes
     *
     * @param env        environment for command
     * @param arguments  arguments of command
     * @param beginIndex beginning of observed string snippet to extract quoted strings from, if there are quotes
     * @return actual path name argument
     */
    public static String extractRawPathName(Environment env, String arguments, int beginIndex) {
        IntWrapper skipCount = new IntWrapper(0);
        return extractRawPathName(env, arguments, beginIndex, skipCount);
    }

    /**
     * Wrapper for int.
     *
     * @author Danijel Barišić
     */
    public static class IntWrapper {
        private int value;

        public IntWrapper(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    /**
     * @param text text
     * @return count of escaped characters in text
     */
    public static int countEscapes(String text) {
        int count = 0;

        for (int i = 0; i < text.length() - 1; i++) {
            if (text.startsWith("\\\\", i) || text.startsWith("\\\"", i)) {
                count++;
                i++;
            }
        }
        return count;
    }

    /**
     * @param b byte to convert
     * @return byte converted to hex
     */
    public static String byteToHex(byte b) {

        StringBuilder sb = new StringBuilder();

        int ms = ((int) b << 24) >>> 28;
        int ls = ((int) b << 28) >>> 28;

        ms = (ms >= 10) ? ('A' + ms - 10) : ('0' + ms);
        ls = (ls >= 10) ? ('A' + ls - 10) : ('0' + ls);

        sb.append((char) ms);
        sb.append((char) ls);

        return sb.toString();
    }

    /**
     * @param i int to convert
     * @return int converted to hex
     */
    public static String byteToHex(int i) {

        StringBuilder sb = new StringBuilder();

        int ms = ((int) i << 24) >>> 28;
        int ls = ((int) i << 28) >>> 28;

        ms = (ms >= 10) ? ('A' + ms - 10) : ('0' + ms);
        ls = (ls >= 10) ? ('A' + ls - 10) : ('0' + ls);

        sb.append((char) ms);
        sb.append((char) ls);

        return sb.toString();
    }
}