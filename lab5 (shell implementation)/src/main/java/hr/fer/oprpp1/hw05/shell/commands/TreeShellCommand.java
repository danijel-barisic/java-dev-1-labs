package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;
import hr.fer.oprpp1.hw05.shell.Util;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Command that prints directory tree.
 *
 * @author Danijel Barišić
 */
public class TreeShellCommand implements ShellCommand {
    /**
     * Name of the command.
     */
    private String commandName;

    /**
     * Description of the command.
     */
    private List<String> commandDescription;

    /**
     * Command environment.
     */
    private Environment env;

    /**
     * Initialises command.
     */
    public TreeShellCommand() {
        commandName = "tree";
        commandDescription = new ArrayList<>();
        commandDescription.add("Prints a directory tree.");
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

        this.env = env;

        String directoryPathname;

        Util.IntWrapper skipCount = new Util.IntWrapper(0);
        directoryPathname = Util.extractRawPathName(env, arguments, 0, skipCount);
        if (directoryPathname == null) {
            return ShellStatus.CONTINUE;
        }

        if (arguments.length() != directoryPathname.length() + skipCount.getValue()) { //if there's second argument
            env.writeln("Error: invalid number of arguments.");
            return ShellStatus.CONTINUE;
        }

        try {
            printDirectoryContent(Paths.get(directoryPathname), 0);
        } catch (IOException e) {
            env.writeln("Error: unsuccessful printing of directory tree.");
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

    /**
     * Recursively prints directory content
     *
     * @param p     current path to traverse children of
     * @param depth depth level in directory traversal
     * @throws IOException if failing to access directory
     */
    private void printDirectoryContent(Path p, int depth) throws IOException {
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(p)) {
            for (Path file : ds) {
                env.writeln(" ".repeat(depth * 2) + file.getFileName());
                if (Files.isDirectory(file)) {
                    printDirectoryContent(file, depth + 1);
                }
            }
        }
    }
}
