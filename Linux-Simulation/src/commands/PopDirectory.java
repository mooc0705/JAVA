package commands;

import Exceptions.HistoryStackInsufficientException;
import fileSystem.Directory;

/**
 * This class is responsible for pop a directory from directory stack and change
 * the current directory to the popped directory.
 * 
 * @author Mike Ma
 */
public class PopDirectory implements Command {

  /**
   * This static final variable will be set once the command instance is
   * initiated. This stores the command name.
   */
  private static final String COMMAND_NAME = "popd";

  /**
   * This static final variable will be set once the command instance is
   * initiated. This stores the command descriptions.
   */
  private static final String COMMAND_DESCRIPTION =
      "Remove the top entry from the directory stack, and cd into it.\n"+
          "The format should be \"popd\".\n";

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
   * This method will pop directory from directory stack if there is any inside
   * of it. **NOTE**: The input array does not matter.
   */
  @Override
  public String performAction(String[] rearrangedInputArray) {
    try {
      // this is for checking the DirectoryStack is empty or not
      if (!Directory.stackIsEmpty()) {
        // the stack is not empty then pop from stack
        // if we need to capture
        if (Capturer.checkIfCapture(rearrangedInputArray)) {
          // capture, do nothing, do not create file.
          return Capturer.capture(rearrangedInputArray, COMMAND_NAME);
        }
        Directory topEntryDir = Directory.popDirStack();
        // pop and change the currentDirectory
        Directory.changeCurrentDirectory(topEntryDir);
        return topEntryDir.getAbsoluteDirectoryPath() + "\n";
      } else {
        // when its empty, throw exception
        throw new HistoryStackInsufficientException(
            "Directory stack is empty.\n");
      }
    } catch (HistoryStackInsufficientException e) {
      return COMMAND_NAME + "*: " + e.getMessage();
    }
  }
}
