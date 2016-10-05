package commands;

import Exceptions.InputFormatException;
import fileSystem.Directory;

/**
 * This class is responsible for printing the current working directories
 * absolute path.
 * 
 * @author Mike Ma
 */
public class PrintWorkingDirectory implements Command {

  /**
   * This static final variable will be set once the command instance is
   * initiated. This stores the command name.
   */
  private static final String COMMAND_NAME = "pwd";

  /**
   * This static final variable will be set once the command instance is
   * initiated. This stores the command descriptions.
   */
  private static final String COMMAND_DESCRIPTION =
      "Print the current working directory (including the whole path).\n"
          + "The format should be 鈥減wd鈥�.";

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCommandName() {
    return COMMAND_NAME;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCommandDescription() {
    return COMMAND_DESCRIPTION;
  }

  /**
   * This method will print the current working directory.
   * 
   * @param rearrangedInputArray this array should be empty
   * @return the current working directory absolute path
   */
  @Override
  public String performAction(String[] rearrangedInputArray) {
    try {
      if (rearrangedInputArray.length == 0) {
        return Directory.getCurrentDirectory().getAbsoluteDirectoryPath()
            + "\n";
      } else {
        if (Capturer.checkIfCapture(rearrangedInputArray)) {
          // capture, do nothing, do not create file.
          return Capturer.capture(rearrangedInputArray, COMMAND_NAME);
        }
        throw new InputFormatException(
            "This command does not take any arguments.\n");
      }
    } catch (InputFormatException e) {
      return COMMAND_NAME + "*: " + e.getMessage();
    }
  }
}
