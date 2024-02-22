package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;
import hr.fer.oprpp1.hw05.shell.Util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Command for copying files.
 *
 * @author Danijel Barišić
 */
public class CopyShellCommand implements ShellCommand {

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
    public CopyShellCommand() {
        commandName = "copy";
        commandDescription = new ArrayList<>();
        commandDescription.add("Copies the specified file onto the specified destination.");
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

        String directoryPathname1;

        Util.IntWrapper skipCount = new Util.IntWrapper(0);
        directoryPathname1 = Util.extractRawPathName(env, arguments, 0, skipCount);
        if (directoryPathname1 == null) {
            return ShellStatus.CONTINUE;
        }

        String directoryPathname2;

        directoryPathname2 = Util.extractRawPathName(env, arguments,
                directoryPathname1.length() + skipCount.getValue() + " ".length());
        if (directoryPathname2 == null) {
            return ShellStatus.CONTINUE;
        }

        File sourceFile = new File(directoryPathname1);
        File destinationFile = new File(directoryPathname2);

        if (destinationFile.isFile()) {
            if (destinationFile.exists()) {
                env.writeln("File \"" + destinationFile.getName() + "\" already exists.\n" +
                        "Do you want to overwrite the file? [Y/N]: ");
                String choice = env.readLine();

                if (choice.equals("Y")) {
                    try {
                        copy(sourceFile, destinationFile);
                    } catch (IOException e) {
                        env.writeln("Error: unable to read/write files.");
                        return ShellStatus.CONTINUE;
                    }
                } else if (choice.equals("N")) {
                    return ShellStatus.CONTINUE;
                } else {
                    env.writeln("Error: invalid choice.");
                    return ShellStatus.CONTINUE;
                }
            } else {
                try {
                    copy(sourceFile, destinationFile);
                } catch (IOException e) {
                    env.writeln("Error: unable to read/write files.");
                    return ShellStatus.CONTINUE;
                }
            }
        } else {
            try {
                copy(sourceFile, destinationFile);
            } catch (IOException e) {
                env.writeln("Error: unable to read/write files.");
                return ShellStatus.CONTINUE;
            }
        }

        env.writeln("Successfully copied.");
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
     * Copies the specified file to the specified location.
     *
     * @param sourceFile      file to copy
     * @param destinationFile location to copy to (file/directory)
     * @throws IOException if failing to do IO operations
     */
    private void copy(File sourceFile, File destinationFile) throws IOException {
        Path destinationPath = destinationFile.isDirectory() ?
                Paths.get(destinationFile.getCanonicalPath() + sourceFile.getName()) :
                Paths.get(destinationFile.getCanonicalPath());

        try (InputStream is = Files.newInputStream(sourceFile.toPath(), StandardOpenOption.READ);
             OutputStream os = Files.newOutputStream(destinationPath)) {
            byte[] buff = new byte[4096];

            while (true) {
                int r = is.read(buff);
                if (r < 1) break;

                os.write(buff, 0, r);
            }
        }
    }
}
