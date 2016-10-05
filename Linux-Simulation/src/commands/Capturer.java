package commands;

import Exceptions.FileNotFoundException;
import fileSystem.Directory;
import fileSystem.File;

/**
 * This class is responsible for capturing the out put of a command and append
 * or overwrite the out put to a new file.
 * 
 */
public class Capturer {

  /**
   * This static member is for the newFile created.
   */
  private static File newFile;

  /**
   * This static member is for the existing file.
   */
  private static File oldFile;

  /**
   * This static member is for the captured content to append or overwrite.
   */
  private static String content;

  /**
   * This is for storing the current directory for file creating.
   */
  private static Directory currentDir;

  /**
   * Check if user want to capture output.
   * 
   * @param rearrangedInputArray the input array
   * @return true if it requires capture else false
   */
  public static boolean checkIfCapture(String[] array) {
    for (int i = 0; i < array.length; i++) {
      if (i + 2 == array.length) {
        if (array[i].equals(">") || array[i].equals(">>")) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * This method will capture the STOUT and create a new file or overwrite a
   * file.
   * 
   * @param array the input array
   * @param commandName the name of the command to perform action
   * @return null or error message
   */
  public static String capture(String[] array, String commandName) {
    String commandClass = InputProcessor.getHashTable().get(commandName);
    String[] recursive = new String[array.length - 2];
    String arrow = array[array.length - 2];
    String fileName = array[array.length - 1];
    // reload the array
    for (int i = 0; i < array.length - 2; i++) {
      recursive[i] = array[i];
    }
    // get the old file for editing
    getFile(fileName);
    // capture the result
    try {
      currentDir = Directory.getCurrentDirectory();
      callCommandAndSetContents(commandClass, commandName, recursive);
      newFile = File.createNewFile(fileName, currentDir);
      overWriteOrAppend(arrow);
      // reset them
      newFile = null;
      oldFile = null;
      return null;
    } catch (Exception e) {
      // remove the new file if captured STDERR
      if (newFile != null) {
        Directory.getCurrentDirectory().removeFile(newFile);
      }
      // reset them
      newFile = null;
      oldFile = null;
      // return the error message
      return e.getMessage();
    }
  }

  /**
   * This helper method will append to the file.
   * 
   * @param arrow append, '>>', or overwrite '>'
   */
  private static void overWriteOrAppend(String arrow) {
    File toEdit = null;
    // set the file to be edited
    if (newFile != null) {
      toEdit = newFile;
    } else if (oldFile != null) {
      toEdit = oldFile;
    }
    if (arrow.equals(">")) {
      toEdit.overwrite(content);
    }
    if (arrow.equals(">>")) {
      toEdit.append("\"" + content + "\"");
    }
  }

  /**
   * This helper will get and return the file or create a new file if the file
   * name does not exist under current directory.
   * 
   * @param fileName the file name
   */
  private static void getFile(String fileName) {
    try {
      oldFile = Directory.getCurrentDirectory()
          .getFileUnderCurrentDirectory(fileName);
    } catch (FileNotFoundException e) {
      // if file is not found, do nothing
    }
  }

  /**
   * This helper method will call the command and return the content to append
   * or overwrite.
   * 
   * @param commandClass the command class full name
   * @param commandName the command name
   * @param recursive the array to perform action again
   * @throws Exception throw the exception when output is STDERR
   */
  private static void callCommandAndSetContents(String commandClass,
      String commandName, String[] recursive) throws Exception {
    try {
      Object cmdInstance =
          Class.forName("commands." + commandClass).newInstance();
      // perform the action with rearranged array
      content = ((Command) cmdInstance).performAction(recursive);
      if (content == null || content.startsWith(commandName + "*: ")
          || content.equals("")) {
        // if the result is null or if the output is error message
        if (content.equals("")) {
          throw new Exception(content);
        }
        throw new Exception(content + "\n");
      }
      // remove the "\n"
      content = content.substring(0, content.length() - 1);
    } catch (InstantiationException | IllegalAccessException
        | ClassNotFoundException e) {
      // wont't happen
      e.printStackTrace();
    }
  }
}
