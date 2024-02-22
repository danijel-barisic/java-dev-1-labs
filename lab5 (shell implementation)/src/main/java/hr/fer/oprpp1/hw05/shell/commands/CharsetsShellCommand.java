package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Command that lists available charsets.
 * @author Danijel Barišić
 */
public class CharsetsShellCommand implements ShellCommand {

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
    public CharsetsShellCommand() {
        commandName = "charsets";
        commandDescription = new ArrayList<>();
        commandDescription.add("Lists supported charsets.");
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
        env.writeln("Available charsets:");
        for (String charset : Charset.availableCharsets().keySet()) {
            env.writeln(charset);
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
