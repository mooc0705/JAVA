package commands;

import Exceptions.DirectoryNotFoundException;
import Exceptions.InputFormatException;
import fileSystem.Directory;

/**
 * This class is responsible for pushing the current directories and go to the
 * directory that user desires.
 * 
 * @author Mike Ma
 */
public class PushDirectory implements Command {

  /**
   * This static final variable will be set once the command instance is
   * initiated. This stores the command name.
   */
  private static final String COMMAND_NAME = "pushd";

  /**
   * This static final variable will be set once the command instance is
   * initiated. This stores the command descriptions.
   */
  private static final String COMMAND_DESCRIPTION =
      "Save the current working directory by pushing onto \n"
          + "directory stack and then changes the new current working \n"
          + "directory to DIR\n" +"The format should be \" pushd DIR\".\n";

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
   * This method will push current directory into directory stack and go to
   * another directory that typed. **NOTE**: The input array should be
   * [pathToGo].
   * 
   * @param rearrangedInputArray This array contains the directory to go to.
   *        It's either a full path or a relative path.
   * @return The string representation of the path pushed into the stack and the
   *         string representation of the path to go to.
   */
  @Override
  public String performAction(String[] rearrangedInputArray) {
    try {
      if (rearrangedInputArray.length == 0) {
        throw new InputFormatException("pushd needs at least 1 argument.\n");
      } else {
        // got the Directory
        String dirToGoToString = rearrangedInputArray[0];
        String currentDirFullPath =
            Directory.getCurrentDirectory().getAbsoluteDirectoryPath();
        // check if need to capture, or proceed normally
        if (Capturer.checkIfCapture(rearrangedInputArray)) {
          // capture, do nothing, do not create file.
          return Capturer.capture(rearrangedInputArray, COMMAND_NAME);
        }
        // push path or throw exception if path does not exist
        pushDirectoryPath(dirToGoToString);
        return dirToGoToString + " " + currentDirFullPath + "\n";
      }
    } catch (InputFormatException | DirectoryNotFoundException e) {
      return COMMAND_NAME + "*: " + e.getMessage();
    }
  }

  /**
   * This method will push directory for full path.
   * 
   * @param fullPath the path to go
   * @throws DirectoryNotFoundException when the directory is not found
   */
  private void pushDirectoryPath(String fullPath)
      throws DirectoryNotFoundException {
    // get the directory or throw exception when the directory does not exist
    Directory dirToGoTo = Directory.getDirectoryHybridByString(fullPath);
    // add current directory into stack
    Directory.pushDirStack(Directory.getCurrentDirectory());
    // change current directory
    Directory.changeCurrentDirectory(dirToGoTo);
  }
}
