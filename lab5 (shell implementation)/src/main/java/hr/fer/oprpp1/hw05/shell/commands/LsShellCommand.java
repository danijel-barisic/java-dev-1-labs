package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;
import hr.fer.oprpp1.hw05.shell.Util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Command that prints directory listing.
 *
 * @author Danijel Barišić
 */
public class LsShellCommand implements ShellCommand {

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
    public LsShellCommand() {
        commandName = "ls";
        commandDescription = new ArrayList<>();
        commandDescription.add("Writes a directory listing.");
    }

    /**
     * Executes command.
     *
     * @param env command environment
     * @param arguments arguments of the command
     * @return CONTINUE if everything is alright, TERMINATE otherwise
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) { //TODO check if given file and not directory

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

        File directory = new File(directoryPathname);
        if (!(directory.exists())) {
            env.writeln("Error: directory doesn't exist.");
        }
        File[] children = directory.listFiles();
        if (children == null) {
            return ShellStatus.CONTINUE;
        }

        for (File file : children) {
            env.write((file.isDirectory() ? "d" : "-") + (file.canRead() ? "r" : "-") +
                    (file.canWrite() ? "w" : "-") + (file.canExecute() ? "x" : "-") + " ");

            long fileSize = file.length();
            env.write(" ".repeat(10 - Long.toString(fileSize).length()) + fileSize + " ");

            BasicFileAttributeView faView = Files.getFileAttributeView(
                    file.toPath(), BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS
            );
            BasicFileAttributes attributes;

            try {
                attributes = faView.readAttributes();
            } catch (IOException e) {
                env.writeln("Error: cannot read file attributes.");
                return ShellStatus.CONTINUE;
            }

            FileTime fileTime = attributes.creationTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = sdf.format(new Date(fileTime.toMillis()));
            env.write(formattedDateTime + " ");

            env.writeln(file.getName());
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
