package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;
import hr.fer.oprpp1.hw05.shell.Util;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Command for printing out file's contents.
 *
 * @author Danijel Barišić
 */
public class CatShellCommand implements ShellCommand {

    /**
     * Name of the command.
     */
    private String commandName;

    /**
     * Description of the command.
     */
    private List<String> commandDescription;

    /**
     * Initialises command.
     */
    public CatShellCommand() {
        commandName = "cat";
        commandDescription = new ArrayList<>();
        commandDescription.add("Prints contents of the specified file.");
    }

    /**
     * Executes command.
     *
     * @param env       command environment
     * @param arguments arguments of the command
     * @return CONTINUE if everything is alright, TERMINATE otherwise
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {

        List<String> argumentList = new ArrayList<>();

        String directoryPathname;

        Util.IntWrapper skipCount = new Util.IntWrapper(0);
        directoryPathname = Util.extractRawPathName(env, arguments, 0, skipCount);
        if (directoryPathname == null) {
            return ShellStatus.CONTINUE;
        }

        argumentList.add(directoryPathname);

        if (arguments.length() != argumentList.get(0).length() + skipCount.getValue()) { //if there's second argument, i.e. charset
            argumentList.add(arguments.substring(
                    argumentList.get(0).length() + skipCount.getValue() + " ".length()).strip());
        }

        if (argumentList.size() < 1 || argumentList.size() > 2) {
            env.writeln("Error: invalid number of arguments.");
            return ShellStatus.CONTINUE;
        }

        Charset cs;
        try {
            cs = argumentList.size() == 1 ? Charset.defaultCharset() : Charset.forName(argumentList.get(1));
        } catch (IllegalArgumentException e) {
            env.writeln("Error: unsupported character encoding.");
            return ShellStatus.CONTINUE;
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new BufferedInputStream(
                                new FileInputStream(argumentList.get(0))), cs))) {
            br.lines().forEach(env::writeln);
        } catch (IOException e) {
            env.writeln("Error: unable to read file.");
        }

        return ShellStatus.CONTINUE;
    }

    /**
     * @return command name
     */
    @Override
    public String getCommandName() {
        return commandName;
    }

    /**
     * @return command description
     */
    @Override
    public List<String> getCommandDescription() {
        return commandDescription;
    }
}