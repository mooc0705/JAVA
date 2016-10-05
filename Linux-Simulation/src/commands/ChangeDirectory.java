package commands;

import Exceptions.DirectoryNotFoundException;
import Exceptions.InputFormatException;
import fileSystem.Directory;

/**
 * This class is responsible for change the directory.
 * 
 * @author Mike Ma
 */
public class ChangeDirectory implements Command {

  /**
   * This static final variable will be set once the command instance is
   * initiated. This stores the command name.
   */
  private static final String COMMAND_NAME = "cd";

  /**
   * This static final variable will be set once the command instance is
   * initiated. This stores the command descriptions.
   */
  private static final String COMMAND_DESCRIPTION =
      "Change directory to DIR, which may be relative to the current\n "
          + "directory or may be a full path. As with Unix, .. means a\n "
          + "parent directory and a . means the current directory. The\n "
          + "directory must be /, the forward slash. The foot of the file\n "
          + "system is a single\n slash: /. \n"
          + "The format should be 鈥渃d DIR[PATH]鈥�.";

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
   * This method will change the current directory. The inputArray is [..] or
   * [PATH] or[NAME]].
   * 
   * @param rearrangedInputArray The inputArray is [..] or [PATH] or[NAME]].
   * @return It will return error message or return null if the directory change
   *         was successful.
   */
  @Override
  public String performAction(String[] rearrangedInputArray) {
    try {
      if (rearrangedInputArray.length == 0) {
        throw new InputFormatException("cd needs at least 1 argument.\n");
      }
      String pathOrName = rearrangedInputArray[0];
      if (Capturer.checkIfCapture(rearrangedInputArray)) {
        // capture, do not create file
        return Capturer.capture(rearrangedInputArray, COMMAND_NAME);
      }
      if (rearrangedInputArray.length > 1) {
        throw new InputFormatException("cd can take at most 1 argument.\n");
      }
      // proceed normal, no capture
      changeToPath(pathOrName);
      return null;
    } catch (DirectoryNotFoundException | InputFormatException e) {
      return COMMAND_NAME + "*: " + e.getMessage();
    }
  }

  /**
   * This helper method will change the directory to the directory that user
   * requested. It will throw DirectoryNotFoundException if the directory was
   * not found.
   * 
   * @param path the directory to change to
   * @throws DirectoryNotFoundException when the directory is not found
   */
  private void changeToPath(String path) throws DirectoryNotFoundException {
    if (path.equals("..")) {
      // go to parent DIR. It can only be "cd .."
      Directory.changeCurrentDirectory(
          Directory.getCurrentDirectory().getParentDirectory());
    } else if (path.equals(".")) {
    } else {
      Directory
          .changeCurrentDirectory(Directory.getDirectoryHybridByString(path));
    }
  }
}
