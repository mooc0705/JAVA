package commands;

import Exceptions.DirectoryAlreadyExistException;
import Exceptions.DirectoryNotFoundException;
import Exceptions.IllegalCharacterException;
import fileSystem.Directory;

/**
 * This class is responsible for making a new directory or directories.
 * 
 * @author Mike Ma
 */
public class MakeDirectory implements Command {

  /**
   * This static final variable will be set once the command instance is
   * initiated. This stores the command name.
   */
  private static final String COMMAND_NAME = "mkdir";

  /**
   * This static final variable will be set once the command instance is
   * initiated. This stores the command descriptions.
   */
  private static final String COMMAND_DESCRIPTION =
      "Create directories, each of which may be relative to the current\n"
          + "directory or may be a full path.\n The format should be "
          + "\"mv OLDPATH NEWPATH\".";

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
   * This method will make a new directory. **NOTE**: The rearrangedInputArray
   * is [[PATH] or [DIR_NAME]...]. It will return an error message if directory
   * was not successfully created or null, when the directory was successfully
   * created.
   * 
   * @param rearrangedInputArray an array of [[PATH] or [DIR_NAME]...]
   * @return the error message or null
   */
  @Override
  public String performAction(String[] rearrangedInputArray) {
    if (rearrangedInputArray.length == 0) {
      return COMMAND_NAME + "*: mkdir needs at least 1 argument.\n";
    } else {
      String result = "";
      if (Capturer.checkIfCapture(rearrangedInputArray)) {
        return Capturer.capture(rearrangedInputArray, COMMAND_NAME);
      }
      for (String pathOrName : rearrangedInputArray) {
        // try create directory for all path or names entered
        String oneTryResult;
        if (pathOrName.contains(".")) {
          // special case contain special string
          oneTryResult = specialCaseOfPath(pathOrName);
        } else if (pathOrName.contains("/")) {
          // its a path
          if (pathOrName.indexOf("/") == 0) {
            oneTryResult = this.makeDirectoryForFullPath(pathOrName);
          } else {
            oneTryResult = this.makeDirectoryForRelativeFullPath(pathOrName);
          }
        } else {
          // not a path, its just a name
          oneTryResult = this.makeDirectoryForName(pathOrName);
        }
        if (oneTryResult != null) {
          result += oneTryResult;
        }
      }
      return result;
    }
  }

  /**
   * This method will eliminate the last item in the String array.
   * 
   * @param original the original array
   * @return the original array without first item
   */
  private String[] eliminateNewDirName(String[] original) {
    String[] result = new String[original.length - 1];
    for (int i = 0; i < original.length - 1; i++) {
      result[i] = original[i];
    }
    return result;
  }

  /**
   * This helper method will return a String representation of a full path from
   * a String array of path.
   * 
   * @param pathArray the path to convert
   * @return the string representation of the path
   */
  private String reformStringPath(String[] pathArray) {
    String result = "";
    for (String path : pathArray) {
      result += path + "/";
    }
    return result;
  }

  /**
   * This helper method deals with the special case where the path contain
   * special character "." or "..". It will return an error message if directory
   * was not successfully created or null, when the directory was successfully
   * created.
   * 
   * @param specialPath the path to examine and create
   * @return the error message or null
   */
  private String specialCaseOfPath(String specialPath) {
    String[] PathWithNewDirName = specialPath.split("/");
    String newDirName = PathWithNewDirName[PathWithNewDirName.length - 1];
    try {
      InputProcessor.containIllegal(newDirName);
      // if the name is legal
      String[] pathThatShouldExist = eliminateNewDirName(PathWithNewDirName);
      String pathThatShouldExistStr = reformStringPath(pathThatShouldExist);
      Directory tempDir =
          Directory.getDirectoryHybridByString(pathThatShouldExistStr);
      String tempNewPath;
      if (tempDir.getAbsoluteDirectoryPath().equals("/")) {
        // if its root directory
        tempNewPath = tempDir.getAbsoluteDirectoryPath() + newDirName;
      } else {
        tempNewPath = tempDir.getAbsoluteDirectoryPath() + "/" + newDirName;
      }
      return makeDirectoryForFullPath(tempNewPath);
    } catch (DirectoryNotFoundException | IllegalCharacterException e) {
      return COMMAND_NAME + "*: " + e.getMessage();
    }
  }

  /**
   * This helper method will make directory for a string representation of full
   * path. It will return an error message if directory was not successfully
   * created or null, when the directory was successfully created.
   * 
   * @param path The path to create.
   * @return The error message or null.
   */
  private String makeDirectoryForFullPath(String path) {
    if (path.equals("/")) {
      new DirectoryAlreadyExistException(
          "Directory " + "\"/\"" + " already exist\n");
    }
    String[] pathArray = Directory.splitDirectoryPath(path);
    String[] pathThatShouldExist = eliminateNewDirName(pathArray);
    String newDirName = pathArray[pathArray.length - 1];
    try {
      // check if contain illegal character
      InputProcessor.containIllegal(newDirName);
      // get parent directory of the new directory to create
      Directory parent = Directory
          .getDirectoryHybridByString(reformStringPath(pathThatShouldExist));
      if (!Directory.validateDirectoryRelatively(newDirName, parent)) {
        // if it does not exist
        Directory.createNonHomeDirectory(newDirName, parent);
        return null;
      } else {
        throw new DirectoryAlreadyExistException(newDirName);
      }
    } catch (DirectoryAlreadyExistException | DirectoryNotFoundException
        | IllegalCharacterException e) {
      return COMMAND_NAME + "*: " + e.getMessage();
    }
  }

  /**
   * This helper method will make directory for a string representation of full
   * path. It will return an error message if directory was not successfully
   * created or null, when the directory was successfully created.
   * 
   * @param path the path to create
   * @return the error message or null
   */
  private String makeDirectoryForRelativeFullPath(String path) {
    String[] pathArray = Directory.splitDirectoryPath(path);
    String remadePath = reformStringPath(eliminateNewDirName(pathArray));
    String newDirName = pathArray[pathArray.length - 1];
    try {
      // check if contain illegal character
      InputProcessor.containIllegal(newDirName);
      Directory parent = Directory.getDirectoryHybridByString(remadePath);
      if (!Directory.validateDirectoryRelatively(newDirName, parent)) {
        // if it does not exist
        Directory.createNonHomeDirectory(newDirName, parent);
        return null;
      } else {
        throw new DirectoryAlreadyExistException(newDirName);
      }
    } catch (DirectoryNotFoundException | DirectoryAlreadyExistException
        | IllegalCharacterException e) {
      return COMMAND_NAME + "*: " + e.getMessage();
    }
  }

  /**
   * This helper method will create directory for the name under current
   * directory. It will return an error message if directory was not
   * successfully created or null, when the directory was successfully created.
   * 
   * @param name the name of the directory to create
   * @return null or error message
   */
  private String makeDirectoryForName(String name) {
    try {
      if (!Directory.validateDirectoryRelatively(name,
          Directory.getCurrentDirectory())) {
        // name does not exist
        InputProcessor.containIllegal(name);
        // name does not contain illegal character
        Directory.createNonHomeDirectory(name, Directory.getCurrentDirectory());
        return null;
      } else {
        // the directory existed
        throw new DirectoryAlreadyExistException(name);
      }
    } catch (DirectoryAlreadyExistException | IllegalCharacterException e) {
      return COMMAND_NAME + "*: " + e.getMessage();
    }
  }
}
