package commands;

import Exceptions.FileAlreadyExistException;
import Exceptions.FileNotFoundException;
import Exceptions.IllegalCharacterException;
import Exceptions.InputFormatException;
import fileSystem.Directory;
import fileSystem.File;

/**
 * This class will append content to an existing file or create a new file if
 * the file does not exist.
 * 
 */
public class AppendContents implements Command {

  /**
   * This static final variable will be set once the command instance is
   * initiated. This stores the command name.
   */
  private static final String COMMAND_NAME = "echo(append)";

  /**
   * This static final variable will be set once the command instance is
   * initiated. This stores the command descriptions.
   */
  private static final String COMMAND_DESCRIPTION =
      "If OUTFILE is not provided, print STRING on the shell. Otherwise, put \n"
          + "STRING into file OUTFILE. STRING is a string of characters \n"
          + "surrounded by double quotation marks. This creates a new file \n"
          + "if OUTFILE does not exists and append to the old contents if \n"
          + "OUTFILE already exists. In either case, the only thing in \n"
          + "OUTFILE should be STRING. The format should be "
          + "\"echo CONTENT [>][>>]\".";

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
   * This method will append content to a file or create a file if the file does
   * not exist. **NOTE**: The format of the input array is [content, >>,
   * fileName].
   * 
   * @param rearrangedInputArray This array is in the form of [content, >>,
   *        fileName].
   * @return Return null if append was successful or error message.
   */
  @Override
  public String performAction(String[] rearrangedInputArray) {
    String fileName = rearrangedInputArray[2];
    String content = rearrangedInputArray[0];
    try {
      if (checkIfQuoteValid(content)) {
        if (Directory.getCurrentDirectory().checkIfFileNameExist(fileName)) {
          // file exist
          Directory.getCurrentDirectory().getFileUnderCurrentDirectory(fileName)
              .append(content);
          return null;
        } else {
          // file not exist
          InputProcessor.containIllegal(fileName);
          // file name is legal, create a new file with the filename
          File newFile =
              File.createNewFile(fileName, Directory.getCurrentDirectory());
          // append the content.
          newFile.append(content);
          return null;
        }
      } else {
        throw new InputFormatException(
            "WRONG FORMATT! Use man echo for more information.\n");
      }
    } catch (FileNotFoundException | InputFormatException
        | IllegalCharacterException | FileAlreadyExistException e) {
      return COMMAND_NAME + "*: " + e.getMessage();
    }
  }

  /**
   * This helper method check if a String start with a quote and end with a
   * quote.
   * 
   * @param toExamine A String to be examined.
   * @return True if the string has valid quote, false otherwise.
   */
  private boolean checkIfQuoteValid(String toExamine) {
    if (toExamine.substring(0, 1).equals("\"")
        && toExamine.substring(toExamine.length() - 1).equals("\"")) {
      return true;
    } else {
      return false;
    }
  }
}
