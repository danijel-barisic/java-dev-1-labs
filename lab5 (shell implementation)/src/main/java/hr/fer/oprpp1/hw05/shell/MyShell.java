package hr.fer.oprpp1.hw05.shell;

import hr.fer.oprpp1.hw05.shell.commands.*;

import java.util.*;

/**
 * Terminal program.
 *
 * @author Danijel Barišić
 */
public class MyShell {

    /**
     * Shell.
     *
     * @param args
     */
    public static void main(String[] args) {

        System.out.println("Welcome to MyShell v 1.0");

        SortedMap<String, ShellCommand> commands = new TreeMap<>();
        commands.put("exit", new ExitShellCommand());
        commands.put("charsets", new CharsetsShellCommand());
        commands.put("cat", new CatShellCommand());
        commands.put("ls", new LsShellCommand());
        commands.put("tree", new TreeShellCommand());
        commands.put("copy", new CopyShellCommand());
        commands.put("mkdir", new MkdirShellCommand());
        commands.put("hexdump", new HexdumpShellCommand());
        commands.put("help", new HelpShellCommand());
        commands.put("symbol", new SymbolShellCommand());

        Environment env = new EnvironmentImpl(commands);
        env.setPromptSymbol('>');
        env.setMorelinesSymbol('\\');
        env.setMultilineSymbol('|');

        ShellStatus status = ShellStatus.CONTINUE;
        do {
            try {
                env.write(env.getPromptSymbol() + " ");

                List<String> lines = new ArrayList<>();

                String line;
                while (!(line = env.readLine()).equals("")) {
                    line = line.strip();

                    if (line.endsWith(env.getMorelinesSymbol().toString())) {
                        lines.add(line.substring(0, line.length() - 1).strip());
                        env.write(env.getMultilineSymbol() + " ");
                    } else {
                        lines.add(line);
                        break;
                    }
                }

                String firstLine = lines.get(0);
                int positionOfFirstSpace = firstLine.indexOf(" ");
                int commandNameEndIndex = positionOfFirstSpace != -1 ? positionOfFirstSpace : firstLine.length();

                String commandName = firstLine.substring(0, commandNameEndIndex);

                StringBuilder sb = new StringBuilder();
                lines.forEach(l -> sb.append(l).append(" "));
                String arguments = sb.toString().replaceFirst(commandName, "").strip();

                ShellCommand command = commands.get(commandName);
                if(command==null){
                    env.writeln("Error: unknown command.");
                    continue;
                }
                status = command.executeCommand(env, arguments);

            } catch (ShellIOException e) {
                status = ShellStatus.TERMINATE;
            }
        } while (status != ShellStatus.TERMINATE);
    }
}
