package hr.fer.oprpp1.hw05.shell;

import java.util.List;

/**
 * Interface that models a shell command.
 *
 * @author Danijel Barišić
 */
public interface ShellCommand {
    ShellStatus executeCommand(Environment env, String arguments);
    String getCommandName();
    List<String> getCommandDescription();
}
