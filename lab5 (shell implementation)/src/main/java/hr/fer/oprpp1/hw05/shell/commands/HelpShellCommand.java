package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Command that prints help for work with other commands.
 *
 * @author Danijel Barišić
 */
public class HelpShellCommand implements ShellCommand {

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
    public HelpShellCommand() {
        commandName = "help";
        commandDescription = new ArrayList<>();
        commandDescription.add("Prints the list of supported command or a description for the specified command.");
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

        if (arguments.equals("")) {
            env.writeln("Supported commands: ");
            for (String command : env.commands().keySet()) {
                env.writeln(command);
            }
        } else {
            if (!env.commands().containsKey(arguments)) {
                env.writeln("Error: no such command.");
                return ShellStatus.CONTINUE;
            }
            env.write("Command name: " + env.commands().get(arguments).getCommandName());
            env.writeln("");
            env.writeln("Command description:");
            env.commands().get(arguments).getCommandDescription().forEach(env::writeln);
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
