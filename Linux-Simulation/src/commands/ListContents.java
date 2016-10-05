package commands;

import java.util.ArrayList;
import java.util.List;

import Exceptions.DirectoryNotFoundException;
import Exceptions.FileNotFoundException;
import Exceptions.InputFormatException;
import fileSystem.Directory;
import fileSystem.File;


/**
 * This class is responsible for listing all the child directories and files
 * under a directory.
 * 
 * @author Mike Ma
 */
public class ListContents implements Command {

  /**
   * This static final variable will be set once the command instance is
   * initiated. This stores the command name.
   */
  private static final String COMMAND_NAME = "ls";

  /**
   * This static final variable will be set once the command instance is
   * initiated. This stores the command descriptions.
   */
  private static final String COMMAND_DESCRIPTION =
      "List the content of the current directory.\nThe format should be "
          + "\"ls [-r]/[-R] [DIR…] [FILE...]\".";

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
   * This method will list content under directory. **NOTE**: [nothing] or
   * [[name]].
   * 
   * @param rearrangedInputArray This array will contain either nothing or names
   *        of directories or paths of directories.
   * @return The concatenated contents information.
   */
  @Override
  public String performAction(String[] rearrangedInputArray) {
    try {
      List<String> files = new ArrayList<String>();
      List<String> dirs = new ArrayList<String>();
      List<String> errors = new ArrayList<String>();
      if (rearrangedInputArray.length == 0) {
        // list current directory content
        return listCurrentDirectoryContent();
      } else {
        // if we were provided with arguments
        if (Capturer.checkIfCapture(rearrangedInputArray)) {
          return Capturer.capture(rearrangedInputArray, COMMAND_NAME);
        }
        if (checkIfRecursiveHelper(rearrangedInputArray)) {
          // if it ask for recursive
          return recursiveListing(rearrangedInputArray, files, errors) + "\n";
        }
        nonRecursiveListing(rearrangedInputArray, files, dirs, errors);
        // concatenate all of them in a format(according to Linux command)
        return outPut(files, dirs, errors) + "\n";
      }
    } catch (InputFormatException e) {
      return COMMAND_NAME + "*: " + e.getMessage();
    }
  }

  /**
   * This helper method will list all sub-directories and files in the current
   * directory or the directory given.
   * 
   * @param array the input directory
   * @param files the list of files information for displaying
   * @param dirs the list of directory information for displaying
   * @param errors the list of errors information for displaying
   */
  private void nonRecursiveListing(String[] array, List<String> files,
      List<String> dirs, List<String> errors) {
    // loop all directories or files requested
    for (int i = 0; i < array.length; i++) {
      String pathInfo = array[i];
      try {
        if (Directory.validateDirectoryHybrid(pathInfo)) {
          // its a full path
          addToArrayListForPath(pathInfo, dirs);
          continue;
        }
        Directory.getFileHybrid(pathInfo);
        // add to files name array
        files.add(pathInfo);
      } catch (DirectoryNotFoundException | FileNotFoundException e) {
        // any directory or file does not exist will be added to errors
        // also delete the last "\n" token
        errors.add(COMMAND_NAME + "*: No such file or directory named \""
            + pathInfo + "\"!");
      }
    }
  }

  /**
   * This helper method will recursively list every directories and their
   * sub-directories as well as adding files. This method can deal with
   * full/relative path directory information and single directory name.
   * 
   * @param array the input array
   * @param files the ArrayList storing files results for output formatting
   * @param errors the errors storing files results for output formatting
   * @return the concatenated string result of directory information
   */
  private String recursiveListing(String[] array, List<String> files,
      List<String> errors) {
    String dirContents = "";
    String singleResult;
    for (int i = 1; i < array.length; i++) {
      String pathInfo = array[i];
      try {
        if (Directory.validateDirectoryHybrid(pathInfo)) {
          // its a full path
          singleResult = recursiveListingHelper(
              Directory.getDirectoryHybridByString(pathInfo));
          if (i + 1 == array.length) {
            dirContents += singleResult.substring(0, singleResult.length() - 2);
            continue;
          } else {
            dirContents += singleResult;
            continue;
          }
        }
        Directory.getFileHybrid(pathInfo);
        // add to files name array
        files.add(pathInfo);
        // contents += "\n"+pathInfo;
      } catch (DirectoryNotFoundException | FileNotFoundException e) {
        errors.add(COMMAND_NAME + "*: No such file or directory named \""
            + pathInfo + "\"!");
      }
    }
    return outputForRecursiveListing(errors, errors, dirContents);
  }

  /**
   * This helper method will format the output for recursive listing.
   * 
   * @param files the ArrayList storing files results for output formatting
   * @param errors the ArrayList storing errors results for output formatting
   * @param dirContents the directory contents
   * @return the result
   */
  private String outputForRecursiveListing(List<String> files,
      List<String> errors, String dirContents) {
    String toAdd = outPut(files, errors);
    if (!toAdd.equals("")) {
      if (!dirContents.equals("")) {
        return toAdd + "\n" + dirContents;
      } else {
        return toAdd;
      }
    } else if (!dirContents.equals("")) {
      return dirContents;
    }
    return null;
  }

  /**
   * This helper method will recursively collect all the directories and
   * sub-directories inside input directories.
   * 
   * @param dir the directory to be listed
   * @return all the listing contents inside one string
   */
  private String recursiveListingHelper(Directory dir) {
    String result = "";
    if (dir.getChildDirecotryList().isEmpty()
        && dir.getFileListUnderCurrentDirectory().isEmpty()) {
      // base case
      return dir.getAbsoluteDirectoryPath() + ":\n\n";
    } else {
      result += dir.getAbsoluteDirectoryPath() + ":\n";
      // add all files to a row
      for (File singleFile : dir.getFileListUnderCurrentDirectory()) {
        result += singleFile.getMyFileName() + "    ";
      }
      // add all subDir to the same row
      for (Directory subDir : dir.getChildDirecotryList()) {
        result += subDir.getDirectoryName() + "    ";
      }
      result += "\n\n";
      // calculating the subDir contents
      String subDirContent = "";
      for (Directory subDir : dir.getChildDirecotryList()) {
        String temp = recursiveListingHelper(subDir);
        if (!temp.equals("")) {
          subDirContent += temp;
        }
      }
      if (!subDirContent.equals("")) {
        result += subDirContent;
      } else {
        result = result.substring(0, result.length() - 2);
      }
    }
    return result;
  }

  /**
   * This helper method will check if the user input request recursive listing
   * contents.
   * 
   * @param array the input array
   * @return true if the user requested recursive listing otherwise false
   * @throws InputFormatException when recursive display input format is wrong
   */
  private boolean checkIfRecursiveHelper(String[] array)
      throws InputFormatException {
    if (array[0].equals("-R") || array[0].equals("-r")) {
      if (array.length < 2) {
        throw new InputFormatException(
            "Recursive display need at least 1 directory\n");
      }
      return true;
    }
    return false;
  }

  /**
   * This helper method will list all the contents under current directory.
   * 
   * @return the content under current directory
   */
  private String listCurrentDirectoryContent() {
    String contents =
        listContentsOfSingleDir(Directory.getCurrentDirectory()) + "\n";
    if (contents.equals("\n")) {
      contents = "";
    }
    return contents;
  }

  /**
   * This helper method can list contents that under a single directory.
   * 
   * @param dir the directory to be listed
   * @return the contents of the directory in a String
   */
  private String listContentsOfSingleDir(Directory dir) {
    String result = "";
    for (int i = 0; i < dir.getChildDirecotryList().size(); i++) {
      // list all sub-directories and align in a row
      result += dir.getChildDirecotryList().get(i).getDirectoryName() + "    ";
    }
    for (int j = 0; j < dir.getFileListUnderCurrentDirectory().size(); j++) {
      // list all files and align in a row
      result += dir.getFileListUnderCurrentDirectory().get(j).getMyFileName()
          + "    ";
    }
    return result;
  }

  /**
   * This helper method will add content info into an array list.
   * 
   * @param path the path to be extracted
   * @param arraylist the array list to add to
   * @throws DirectoryNotFoundException when the directory is not found
   */
  private void addToArrayListForPath(String path, List<String> arraylist)
      throws DirectoryNotFoundException {
    Directory theDir = Directory.getDirectoryHybridByString(path);
    String toAdd = theDir.getAbsoluteDirectoryPath() + ":";
    if (!listContentsOfSingleDir(theDir).equals("")) {
      toAdd += "\n";
    }
    toAdd += listContentsOfSingleDir(theDir);
    arraylist.add(toAdd);
  }

  /**
   * This helper method will format the output of the three lists.
   * 
   * @param files the file list
   * @param dirs the directory list
   * @param errors the error list
   * @return the formatted output
   */
  private String outPut(List<String> files, List<String> errors) {
    String result = "";
    for (String item : errors) {
      result += item;
      if (files.size() != 0 || errors.indexOf(item) < errors.size() - 1) {
        // if there is more thing, then add two new lines
        result += "\n\n";
      }
    }
    for (String item : files) {
      result += item + "    ";
      if (files.indexOf(item) == files.size() - 1) {
        // at the end of the list
        result += "\n";
        if (files.indexOf(item) < files.size() - 1) {
          result += "\n";
        }
      }
    }
    return result;
  }

  /**
   * This helper method will format the output of the three lists.
   * 
   * @param files the file list
   * @param dirs the directory list
   * @param errors the error list
   * @return the formatted output
   */
  private String outPut(List<String> files, List<String> dirs,
      List<String> errors) {
    String result = "";
    // add all errors
    for (String item : errors) {
      result += item;
      if (files.size() != 0 || dirs.size() != 0
          || errors.indexOf(item) < errors.size() - 1) {
        // if there is more thing, then add two new lines
        result += "\n\n";
      }
    }
    // add all files
    for (String item : files) {
      result += item + "    ";
      if (files.indexOf(item) == files.size() - 1) {
        // at the end of the list, then we add the new line
        result += "\n";
        if (dirs.size() != 0) {
          result += "\n";
        }
      }
    }
    // add all directories
    for (String item : dirs) {
      result += item;
      if (dirs.indexOf(item) < dirs.size() - 1) {
        result += "\n\n";
      }
    }
    return result;
  }
}
