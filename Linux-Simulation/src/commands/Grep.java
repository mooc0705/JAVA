package commands;

import java.util.regex.*;

import Exceptions.DirectoryNotFoundException;
import Exceptions.FileNotFoundException;
import Exceptions.InputFormatException;
import fileSystem.Directory;
import fileSystem.File;

/**
 * This class is responsible for search REGEX in files with or without
 * recursion.
 * 
 */
public class Grep implements Command {

  /**
   * This static final variable will be set once the command instance is
   * initiated. This stores the command name.
   */
  private static final String COMMAND_NAME = "grep";

  /**
   * This static final variable will be set once the command instance is
   * initiated. This stores the command descriptions.
   */
  private static final String COMMAND_DESCRIPTION =
      "If –R is not supplied, print any lines containing REGEX in PATH, which\n"
          + " must be a file. If –R is supplied, and PATH is a directory,\n"
          + " recursively traverse the directory and, for all lines in all\n"
          + " files that contain REGEX, print the path to the file (including\n"
          + " the filename), then a colon, then the line that contained REGEX."
          + "\n The input should be in format of \"grep [-r]/[-R] REGEX "
          + "PATH\".";

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
   * This method will perform the action of 'grep'.
   * 
   * @param rearrangedInputArray this array is in the form of
   *        "grep [-R]/[-r] PATH..."
   * @return return null if 'grep' was successful or error message
   */
  @Override
  public String performAction(String[] rearrangedInputArray) {
    if (Capturer.checkIfCapture(rearrangedInputArray)) {
      return Capturer.capture(rearrangedInputArray, COMMAND_NAME);
    }
    if (checkIfRecursive(rearrangedInputArray)) {
      // user ask for recursion
      try {
        return performActionWithR(rearrangedInputArray) + "\n";
      } catch (InputFormatException e) {
        return COMMAND_NAME + "*: " + e.getMessage();
      }
    } else {
      // user did not ask for recursion
      try {
        return performActionWithoutR(rearrangedInputArray) + "\n";
      } catch (InputFormatException e) {
        return COMMAND_NAME + "*: " + e.getMessage();
      }
    }
  }

  /**
   * This helper method will check if the use input asks for recursive 'grep'.
   * 
   * @param array the input array
   * @return true if it ask for recursive else false
   */
  private boolean checkIfRecursive(String[] array) {
    if (array[0].equals("-R") || array[0].equals("-r")) {
      return true;
    }
    return false;
  }

  /**
   * This helper method will perform the GREP action with recursive function.
   * 
   * @param array the input array
   * @return the result or error message
   * @throws InputFormatException if the input array is wrong
   */
  private String performActionWithR(String[] array)
      throws InputFormatException {
    if (array.length >= 3) {
      String result = "";
      String regex = array[1];
      regex = array[1].substring(1, array[1].length() - 1);
      for (int i = 2; i < array.length; i++) {
        // they are all path
        try {
          Directory theDir = Directory.getDirectoryHybridByString(array[i]);
          result += recursiveGrepTheDirectory(regex, theDir);
          if (!(i + 1 == array.length)) {
            result += "\n";
          }
        } catch (DirectoryNotFoundException e) {
          result += COMMAND_NAME + "*: "
              + e.getMessage().substring(0, e.getMessage().length() - 1);
        }
      }
      return result;
    }
    throw new InputFormatException(
        "Missing arguments. Check manual for instruction!\n");
  }

  /**
   * This helper method will perform the GREP action without recursive function.
   * 
   * @param array the input array
   * @return the result or error message
   * @throws InputFormatException if the input array is wrong
   */
  private String performActionWithoutR(String[] array)
      throws InputFormatException {
    if (array.length >= 2) {
      String result = "";
      for (int i = 1; i < array.length; i++) {
        // loop through all the files
        result += singleResultFormatter(array, i);
        if (!(i + 1 == array.length)) {
          result += "\n";
        }
      }
      return result;
    } else {
      throw new InputFormatException(
          "Missing arguments. Check manual for instruction!\n");
    }
  }

  /**
   * This helper function will format the 'grep' result.
   * 
   * @param array the input array
   * @param i the loop index
   * @return the result
   * @throws InputFormatException when the REGEX is not surrounded by quotes
   */
  private String singleResultFormatter(String[] array, int i)
      throws InputFormatException {
    String result = "";
    String regex = array[0];
    regex = array[0].substring(1, array[0].length() - 1);
    File fileToCheck;
    String strToCheck;
    try {
      // get the file or throw exception
      fileToCheck = Directory.getFileHybrid(array[i]);
      // get the string to check
      strToCheck = fileToCheck.display();
      if (!regexChecker(regex, strToCheck).equals("")) {
        result += fileToCheck.getMyFileFullPath() + ":\n"
            + regexChecker(regex, strToCheck);
        if (!(i + 1 == array.length)) {
          result += "\n\n";
        }
      } else {
        // if string does not contain the REGEX
        throw new InputFormatException(fileToCheck.getMyFileName()
            + " does not contain REGEX \"" + regex + "\".\n");
      }
    } catch (FileNotFoundException | DirectoryNotFoundException
        | InputFormatException e) {
      result = COMMAND_NAME + "*: "
          + e.getMessage().substring(0, e.getMessage().length() - 1);
    }
    return result;
  }

  /**
   * This helper method will recursively loop through all the files under the
   * directory given and give back the lines that contain the REGEX.
   * 
   * @param theRegex the REGEX to search
   * @param dir the directory to search
   * @return the lines that contain the REGEX
   */
  private String recursiveGrepTheDirectory(String theRegex, Directory dir) {
    if (dir.getFileListUnderCurrentDirectory().isEmpty()) {
      // base case
      return "";
    } else {
      String result = "";
      for (File file : dir.getFileListUnderCurrentDirectory()) {
        // loop all the files under the directory
        String fileResult = regexChecker(theRegex, file.display());
        if (!fileResult.equals("")) {
          // fileResult is not empty i.e. file contains the REGEX
          result += file.getMyFileFullPath() + ": \n" + fileResult;
          if (!(dir.getFileListUnderCurrentDirectory().indexOf(file) + 1 == dir
              .getFileListUnderCurrentDirectory().size())) {
            result += "\n\n";
          }
        }
      }
      for (Directory singleDir : dir.getChildDirecotryList()) {
        // loop all the sub-directories
        result += "\n" + recursiveGrepTheDirectory(theRegex, singleDir);
      }
      if (!result.equals("")) {
        return result.substring(0, result.length());
      }
      return result;
    }
  }

  /**
   * This helper method will return the line of the string that contain the
   * REGEX.
   * 
   * @param theRegex the REGEX to search
   * @param strToBeChecked the string to be examined
   * @return the lines that contain the REGEX
   */
  private String regexChecker(String theRegex, String strToBeChecked) {
    Pattern checkRegex = Pattern.compile(theRegex);
    String[] lines = strToBeChecked.split("\n");
    // break by lines
    String result = "";
    for (int i = 0; i < lines.length; i++) {
      // loop through lines
      Matcher regexMatcher = checkRegex.matcher(lines[i]);
      if (regexMatcher.find()) {
        // if the line were found containing the REGEX
        if (i < lines.length - 1) {
          // if not at the end of the list, then we add new line
          result += lines[i] + "\n";
        } else {
          // at the end, do not add new line
          result += lines[i];
        }
      }
    }
    return result;
  }
}
