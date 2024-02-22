package hr.fer.oprpp1.hw04.db.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

/**
 * Helper class that provides useful IO methods for working with queries.
 *
 * @author Danijel Barišić
 */
public class QueryIOUtils {

    /**
     * Reads query text from terminal.
     *
     * @return query text <b>after</b> the "query" command, or returns "exit" if input is "exit"
     * @throws IllegalArgumentException if command is invalid
     */
    public static String prompt() {
        System.out.print("> ");

        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();

        if (line.trim().equals("exit")) {
            System.out.println("Goodbye!");
            return "exit";
        }

        if (!line.trim().matches("query.*")) {
            throw new IllegalArgumentException("Invalid command.");
        }

        return line.replaceFirst("query", "");
    }

    /**
     * Loads rows(/records) of the database as strings.
     *
     * @param path filepath of the textual database
     * @return list of database rows as strings
     * @throws IOException if an IOError occurs from reading the file
     */
    public static List<String> loadDatabase(String path) throws IOException {
        return Files.readAllLines(
                Paths.get(path),
                StandardCharsets.UTF_8
        );
    }
}
