package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Command that shows or changes shell symbols.
 *
 * @author Danijel Barišić
 */
public class SymbolShellCommand implements ShellCommand {

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
    public SymbolShellCommand() {
        commandName = "symbol";
        commandDescription = new ArrayList<>();
        commandDescription.add("Displays current symbol or changes symbol into the specified one.");
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

        String[] argumentArray = arguments.split("\\s+");

        if (argumentArray.length < 1 || argumentArray.length > 2) {
            env.writeln("Error: invalid number of arguments.");
            return ShellStatus.CONTINUE;
        }

        switch (argumentArray[0]) {

            case "PROMPT":
                if (argumentArray.length == 1) {
                    env.writeln("Symbol for PROMPT is '" + env.getPromptSymbol() + "'");
                } else {
                    char oldSymbol = env.getPromptSymbol();
                    env.setPromptSymbol(argumentArray[1].charAt(0));
                    env.writeln("Symbol for PROMPT changed from '" + oldSymbol
                            + "' to '" + env.getPromptSymbol() + "'");
                }
                break;

            case "MORELINES":
                if (argumentArray.length == 1) {
                    env.writeln("Symbol for MORELINES is '" + env.getMorelinesSymbol() + "'");
                } else {
                    char oldSymbol = env.getMorelinesSymbol();
                    env.setMorelinesSymbol(argumentArray[1].charAt(0));
                    env.writeln("Symbol for MORELINES changed from '" + oldSymbol
                            + "' to '" + env.getMorelinesSymbol() + "'");
                }
                break;

            case "MULTILINE":
                if (argumentArray.length == 1) {
                    env.writeln("Symbol for MULTILINE is '" + env.getMultilineSymbol() + "'");
                } else {
                    char oldSymbol = env.getMultilineSymbol();
                    env.setMultilineSymbol(argumentArray[1].charAt(0));
                    env.writeln("Symbol for MULTILINE changed from '" + oldSymbol
                            + "' to '" + env.getMultilineSymbol() + "'");
                }
                break;

            default:
                env.writeln("Error: unknown symbol: " + argumentArray[0]);
                break;
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
