package commands;

import Exceptions.FileAlreadyExistException;
import Exceptions.FileNotFoundException;
import Exceptions.IllegalCharacterException;
import fileSystem.Directory;
import fileSystem.File;

/**
 * This class is responsible for overwriting a file or create a file if the file
 * does not exist.
 * 
 * @author Mike Ma
 */
public class OverwriteContent implements Command {

  /**
   * This static final variable will be set once the command instance is
   * initiated. This stores the command name.
   */
  private static final String COMMAND_NAME = "echo(overWrite)";

  /**
   * This static final variable will be set once the command instance is
   * initiated. This stores the command descriptions.
   */
  private static final String COMMAND_DESCRIPTION =
      "If OUTFILE is not provided, print STRING on the shell. Otherwise, \n"
          + "put STRING into file OUTFILE. STRING is a string of characters \n"
          + "surrounded by double quotation marks. This creates a new file if\n"
          + "OUTFILE does not exists and erases the old contents if OUTFILE \n"
          + "already exists. In either case, the only thing in OUTFILE \n"
          + "should be STRING.";

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
   * This method will overwrite a file if the file exist, otherwise it will
   * create a new file. **NOTE**: The format of input array is [content, >,
   * fileName].
   * 
   * @param rearrangedInputArray this array will be in format of [content, >,
   *        fileName]
   * @return null or error message
   */
  @Override
  public String performAction(String[] rearrangedInputArray) {
    String fileName = rearrangedInputArray[2];
    String content = rearrangedInputArray[0];
    if (checkIfQuoteValid(content)) {
      try {
        if (Directory.getCurrentDirectory().checkIfFileNameExist(fileName)) {
          // fileName exist
          Directory.getCurrentDirectory().getFileUnderCurrentDirectory(fileName)
              .overwrite(content.substring(1, content.length() - 1));
          return null;
        } else {
          // fileName not exist then create new file and add to Directory.
          InputProcessor.containIllegal(fileName);
          // fileName is legal
          File newFile =
              File.createNewFile(fileName, Directory.getCurrentDirectory());
          newFile.overwrite(content.substring(1, content.length() - 1));
        }
      } catch (FileNotFoundException | IllegalCharacterException
          | FileAlreadyExistException e) {
        return COMMAND_NAME + "*: " + e.getMessage();
      }
      return null;
    } else {
      return COMMAND_NAME + "*: " + "wrong formatt in overwrite\n";
    }
  }

  /**
   * This helper method check if a String start with a quote and end with a
   * quote.
   * 
   * @param content A String to be examined.
   * @return True if the string has valid quote, false otherwise.
   */
  private boolean checkIfQuoteValid(String content) {
    if (content.substring(0, 1).equals("\"")
        && content.substring(content.length() - 1).equals("\"")) {
      return true;
    } else {
      return false;
    }
  }
}
