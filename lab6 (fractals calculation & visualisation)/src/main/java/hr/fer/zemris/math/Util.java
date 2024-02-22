package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Utility class for input prompting and parsing of complex numbers.
 *
 * @author Danijel Barišić
 */
public class Util {

    /**
     * @param text textual representation of the complex number, with operands and middle operator separated by space
     *             (e.g. -3.2 + i4.36)
     * @return complex number parsed from text
     * @throws IllegalArgumentException if no text is provided
     * @throws NumberFormatException    if complex number is invalid (e.g. has invalid characters)
     */
    private static Complex parseComplex(String text) {
        double resRe = 0, resIm = 0;

        String[] splitText = text.split("\\s");

        if (splitText.length == 0) {
            throw new IllegalArgumentException("No complex number provided.");
        }

        try {
            if (splitText.length == 3) {
                resRe = Double.parseDouble(splitText[0]);
                resIm = Double.parseDouble((splitText[1] + splitText[2]).replace("i", ""));
            } else if (splitText.length == 1) {
                if (splitText[0].equals("i")) {
                    resIm = 1;
                } else if (splitText[0].equals("-i")) {
                    resIm = -1;
                } else {
                    resRe = Double.parseDouble(splitText[0]);
                }
            } else {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid complex number.");
        }

        return new Complex(resRe, resIm);
    }

    /**
     * Prompt function for complex roots input, which reads input until it reads "done".
     *
     * @return array of complex roots
     */
    public static Complex[] promptForRoots() {
        System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.\n" +
                "Please enter at least two roots, one root per line. Enter 'done' when done.");
        Scanner sc = new Scanner(System.in);
        String text;
        List<Complex> roots = new ArrayList<>();

        int i = 1;
        System.out.printf("Root %d> ", i++);
        while (!(text = sc.nextLine()).equals("done")) {
            System.out.printf("Root %d> ", i++);
            roots.add(parseComplex(text));
        }

        sc.close();

        return roots.toArray(new Complex[0]);
    }

}
