package commands;

import Exceptions.DirectoryAlreadyExistException;
import Exceptions.DirectoryNotFoundException;
import Exceptions.FileAlreadyExistException;
import Exceptions.FileNotFoundException;
import Exceptions.InputFormatException;
import fileSystem.Directory;
import fileSystem.File;

/**
 * This class is responsible for moving directory from a old path to a new path.
 * 
 */
public class Move implements Command {

  /**
   * This static final variable will be set once the command instance is
   * initiated. This stores the command name.
   */
  private static final String COMMAND_NAME = "mv";

  /**
   * This static final variable will be set once the command instance is
   * initiated. This stores the command descriptions.
   */
  private static final String COMMAND_DESCRIPTION =
      "Move the OLD_DIRECTORY to the NEW_DIRECTORY.";

  /**
   * a string stores old path
   */
  private String oldPath;

  /**
   * a string stores new path
   */
  private String destinationPath;

  /**
   * a new Directory to append or modify
   */
  private Directory destinationDirectory;

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
   * This method will move old directory to a new directory.
   * 
   * @param rearrangedInputArray the input array [OLDPATH, NEWPATH]
   * @return error message or null
   */
  @Override
  public String performAction(String[] rearrangedInputArray) {
    try {
      if (rearrangedInputArray.length != 2
          && rearrangedInputArray.length != 4) {
        throw new InputFormatException(
            "mv needs 2 arguments or 4 aruguments.\n");
      } else if (rearrangedInputArray.length == 4) {
        if (Capturer.checkIfCapture(rearrangedInputArray)) {
          return Capturer.capture(rearrangedInputArray, COMMAND_NAME);
        }
        throw new InputFormatException(
            "Wrong input format for mv, please refer to the manual!\n");
      } else {
        // get old and new path
        setUpInformation(rearrangedInputArray);
        if (Directory.validateDirectoryHybrid(oldPath)) {
          // its a directory
          moveDirectory();
        } else {
          // its a file
          moveFile();
        }
        return null;
      }
    } catch (InputFormatException | DirectoryNotFoundException
        | DirectoryAlreadyExistException | FileNotFoundException
        | FileAlreadyExistException e) {
      return COMMAND_NAME + "*: " + e.getMessage();
    }
  }

  /**
   * This helper method will get all the information needed before proceed.
   * 
   * @param rearrangedInputArray the input array
   * @throws DirectoryNotFoundException when the destination directory to does
   *         not exist
   */
  private void setUpInformation(String[] rearrangedInputArray)
      throws DirectoryNotFoundException {
    oldPath = rearrangedInputArray[0];
    destinationPath = rearrangedInputArray[1];
    destinationDirectory =
        Directory.getDirectoryHybridByString(destinationPath);
  }

  /**
   * This helper method will 1. get old directory 2. Check if the directory name
   * exist 4. Change the parent directory of the directory.
   * 
   * @throws DirectoryNotFoundException when the old directory is not found
   * @throws DirectoryAlreadyExistException when the directory to move already
   *         exist in the destination directory
   * @throws InputFormatException when try to move a directory to it's
   *         sub-directory
   */
  private void moveDirectory() throws DirectoryNotFoundException,
      DirectoryAlreadyExistException, InputFormatException {
    // get old directory and new directory
    Directory oldDir = Directory.getDirectoryHybridByString(oldPath);
    Directory copyOfOldDir = oldDir.getCopyOfDirectory();
    // check if name exist
    checkIfDirectoryNameExist(oldDir, destinationDirectory);
    if (oldDir.checkIfSubDirectory(destinationDirectory)) {
      throw new InputFormatException(
          "Cannot move a directory to it's own sub-directory!\n");
    }
    // change parent directory of the old directory and all the
    // sub-directories.
    oldDir.changeParentDirectory(destinationDirectory);
    Directory.getCurrentDirectory().addChildDirectory(copyOfOldDir);
  }

  /**
   * This helper method will 1. get old file 2. Check if the directory name
   * exist 3. Change the parent directory of the file.
   * 
   * @throws FileNotFoundException when the old file does not exist
   * @throws DirectoryNotFoundException when the directory to be moved does not
   *         exist
   * @throws FileAlreadyExistException when the filename of the old file already
   *         exist in the destination directory
   */
  private void moveFile() throws FileNotFoundException,
      DirectoryNotFoundException, FileAlreadyExistException {
    File oldFile = Directory.getFileHybrid(oldPath);
    checkIfFileNameExist(oldFile, destinationDirectory);
    oldFile.changeFileDirectory(destinationDirectory);
  }

  /**
   * This helper method will check if the File wish to move has already exist in
   * the new directory. If so, it will throw exception.
   * 
   * @param oldFile the old file
   * @param newDir the new directory
   * @return false if the file exist under the newDir
   * @throws FileAlreadyExistException when the File already exist
   */
  private boolean checkIfFileNameExist(File oldFile, Directory newDir)
      throws FileAlreadyExistException {
    for (File singleFile : newDir.getFileListUnderCurrentDirectory()) {
      if (singleFile.getMyFileName().equals(oldFile.getMyFileName())) {
        throw new FileAlreadyExistException(oldFile.getMyFileName());
      }
    }
    return false;
  }

  /**
   * This helper method will check if the directory wish to move has already
   * exist in the new directory. If so, it will throw exception.
   * 
   * @param oldDir the old directory
   * @param newDir the new directory
   * @return false if it does not exist
   * @throws DirectoryAlreadyExistException when the directory already exist
   */
  private boolean checkIfDirectoryNameExist(Directory oldDir, Directory newDir)
      throws DirectoryAlreadyExistException {
    for (Directory subDir : newDir.getChildDirecotryList()) {
      if (subDir.getDirectoryName().equals(oldDir.getDirectoryName())) {
        throw new DirectoryAlreadyExistException(
            oldDir.getAbsoluteDirectoryPath());
      }
    }
    return false;
  }
}
