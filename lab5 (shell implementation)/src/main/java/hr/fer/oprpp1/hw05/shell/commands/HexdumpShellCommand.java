package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;
import hr.fer.oprpp1.hw05.shell.Util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Command that prints hex-output of files.
 *
 * @author Danijel Barišić
 */
public class HexdumpShellCommand implements ShellCommand {

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
    public HexdumpShellCommand() {
        commandName = "hexdump";
        commandDescription = new ArrayList<>();
        commandDescription.add("Produces hex-output of the specified file.");
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

        int[] lineNum = new int[4];
        for (int i = 0; i < 4; i++) {
            lineNum[i] = 0;
        }
        try (InputStream is = Files.newInputStream(Paths.get(directoryPathname), StandardOpenOption.READ)) {
            byte[] buff = new byte[4096];

            while (true) {
                int r = is.read(buff);
                if (r < 1) break;

                for (int i = 0; i < Math.ceil((double) r / 16); i++) { //current buff printing, printing rows
                    for (int j = 0; j < 4; j++) {
                        env.write(Util.byteToHex(lineNum[j]));
                    }
                    env.write(": ");
                    for (int j = 0; j < 16; j++) { //current row printing
                        env.write((16 * i + j < r) ? Util.byteToHex(buff[16 * i + j]) : "  ");

                        if (j == 7) {
                            env.write("|");
                        } else {
                            env.write(" ");
                        }
                    }
                    env.write("| ");
                    for (int j = 0; j < 16; j++) {
                        byte b = !(buff[16 * i + j] < 0) && buff[16 * i + j] >= 32 ? buff[16 * i + j] : (byte) '.';
                        env.write((16 * i + j < r) ? Character.valueOf((char) b).toString() : " ");
                    }
                    env.writeln("");

                    lineNum[3] += 16;
                    if (lineNum[3] == 256) {
                        lineNum[3] = 0;
                        lineNum[2]++;

                        if (lineNum[2] == 256) {
                            lineNum[2] = 0;
                            lineNum[1]++;

                            if (lineNum[1] == 256) {
                                lineNum[1] = 0;
                                lineNum[0]++;
                            }
                        }
                    }
                }
            }
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