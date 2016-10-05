package commands;

import Exceptions.DirectoryAlreadyExistException;
import Exceptions.DirectoryNotFoundException;
import Exceptions.FileAlreadyExistException;
import Exceptions.FileNotFoundException;
import Exceptions.InputFormatException;
import fileSystem.Directory;
import fileSystem.File;

/**
 * This class is responsible for copying a directory to another directory.
 * 
 */
public class Copy implements Command {

  /**
   * This static final variable will be set once the command instance is
   * initiated. This stores the command name.
   */
  private static final String COMMAND_NAME = "cp";

  /**
   * This static final variable will be set once the command instance is
   * initiated. This stores the command descriptions.
   */
  private static final String COMMAND_DESCRIPTION =
      "Copy the OLD_DIRECTORY to the NEW_DIRECTORY.\n The format should "
      + "be \"cp OLDPATH NEWPATH\".";

  /**
   * a string stores old path
   */
  private String oldPath;

  /**
   * a string stores destination path
   */
  private String destination;

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
   * This method will copy old directory to a new directory.
   * 
   * @param rearrangedInputArray the input array [OLDPATH, NEWPATH]
   * @return error message or null
   */
  @Override
  public String performAction(String[] rearrangedInputArray) {
    try {
      if (rearrangedInputArray.length != 2
          && rearrangedInputArray.length != 4) {
        throw new InputFormatException("cp needs 2 argument.\n");
      } else if (rearrangedInputArray.length == 4) {
        if (Capturer.checkIfCapture(rearrangedInputArray)) {
          return Capturer.capture(rearrangedInputArray, COMMAND_NAME);
        }
        throw new InputFormatException(
            "Wrong input format for cp, please refer to the manual!\n");
      } else {
        // get old and new path information
        setUpInfomation(rearrangedInputArray);
        if (Directory.validateDirectoryHybrid(oldPath)) {
          // if its a directory
          copyDir();
        } else {
          // try if it's a file
          copyFile();
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
  private void setUpInfomation(String[] rearrangedInputArray)
      throws DirectoryNotFoundException {
    oldPath = rearrangedInputArray[0];
    destination = rearrangedInputArray[1];
    destinationDirectory = Directory.getDirectoryHybridByString(destination);
  }

  /**
   * This helper method will 1. get old file 2. get a copy of the old file 3.
   * Check if the file name exist 4. Change the parent directory of the copy of
   * the file.
   * 
   * @throws FileNotFoundException when the old file does not exist
   * @throws DirectoryNotFoundException when the directory to put the copy of
   *         the file does not exist
   * @throws FileAlreadyExistException when the filename of the old file already
   *         exist in the directory to go to
   */
  private void copyFile() throws FileNotFoundException,
      DirectoryNotFoundException, FileAlreadyExistException {
    File oldFile = Directory.getFileHybrid(oldPath);
    File copyOfFile = oldFile.getCopyOfFile();
    checkIfFileNameExist(copyOfFile, destinationDirectory);
    copyOfFile.changeFileDirectory(destinationDirectory);
  }

  /**
   * This helper method will 1. get old directory 2. get a copy of the old
   * directory 3. Check if the directory name exist 4. Change the parent
   * directory of the copy of the directory.
   * 
   * @throws DirectoryNotFoundException when the old directory is not found
   * @throws DirectoryAlreadyExistException when the directory to move already
   *         exist in the directory to move to
   * @throws InputFormatException when its trying to copy a directory into it's
   *         own sub-directory
   */
  private void copyDir() throws DirectoryNotFoundException,
      DirectoryAlreadyExistException, InputFormatException {
    // get old and new directory
    Directory oldDir = Directory.getDirectoryHybridByString(oldPath);
    // get a copy of the old directory
    Directory copyOfDir = oldDir.getCopyOfDirectory();
    // check if name duplicates
    checkIfDirectoryNameExist(copyOfDir, destinationDirectory);
    // move the copy of the directory under the new directory
    if (oldDir.checkIfSubDirectory(destinationDirectory)) {
      throw new InputFormatException(
          "Cannot copy a directory to it's own sub-directory!\n");
    }
    copyOfDir.changeParentDirectory(destinationDirectory);
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
      // loop through all files
      if (singleFile.getMyFileName().equals(oldFile.getMyFileName())) {
        // if file name duplicates
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
            subDir.getAbsoluteDirectoryPath());
      }
    }
    return false;
  }
}
