package hr.fer.oprpp1.hw05.shell;

import java.util.Scanner;
import java.util.SortedMap;

/**
 * Standard system I/O implementation of command environment.
 *
 * @author Danijel Barišić
 */
public class EnvironmentImpl implements Environment {

    /**
     * Scanner of system input.
     */
    Scanner sc;

    /**
     * Shell prompt symbol.
     */
    Character promptSymbol;

    /**
     * Symbol of multiple lines.
     */
    Character multilineSymbol;

    /**
     * Symbol for more lines.
     */
    Character morelinesSymbol;

    /**
     * Map containing all supported commands in the environment.
     */
    SortedMap<String, ShellCommand> commands;

    /**
     *
     * @param commands commands to support
     */
    public EnvironmentImpl(SortedMap<String, ShellCommand> commands) {
        this.commands = commands;
        sc = new Scanner(System.in);
    }

    @Override
    public String readLine() throws ShellIOException {
        String result;

        try {
            result = sc.nextLine();
        } catch (Exception e) {
            throw new ShellIOException(e);
        }

        return result;
    }

    @Override
    public void write(String text) throws ShellIOException {
        System.out.print(text);
    }

    @Override
    public void writeln(String text) throws ShellIOException {
        System.out.println(text);
    }

    @Override
    public SortedMap<String, ShellCommand> commands() {
        return commands;
    }

    @Override
    public Character getMultilineSymbol() {
        return multilineSymbol;
    }

    @Override
    public void setMultilineSymbol(Character symbol) {
        multilineSymbol = symbol;
    }

    @Override
    public Character getPromptSymbol() {
        return promptSymbol;
    }

    @Override
    public void setPromptSymbol(Character symbol) {
        promptSymbol = symbol;
    }

    @Override
    public Character getMorelinesSymbol() {
        return morelinesSymbol;
    }

    @Override
    public void setMorelinesSymbol(Character symbol) {
        morelinesSymbol = symbol;
    }

}
