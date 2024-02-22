package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;
import hr.fer.oprpp1.hw05.shell.Util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Command that makes a specified directory structure.
 * @author Danijel Barišić
 */
public class MkdirShellCommand implements ShellCommand {

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
    public MkdirShellCommand() {
        commandName = "mkdir";
        commandDescription = new ArrayList<>();
        commandDescription.add("Creates specified directory structure.");
    }

    /**
     * Executes command.
     *
     * @param env command environment
     * @param arguments arguments of the command
     * @return CONTINUE if everything is alright, TERMINATE otherwise
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {

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
            Files.createDirectories(Paths.get(directoryPathname));
        } catch (IOException e) {
            env.writeln("Error: couldn't create directory");
            return ShellStatus.CONTINUE;
        }

        env.writeln("Directory succesfully created.");

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
