package commands;

import Exceptions.FileNotFoundException;
import Exceptions.InputFormatException;
import fileSystem.Directory;

/**
 * This class is responsible for displaying file content.
 * 
 * @author Mike Ma
 */
public class DisplayFilesContents implements Command {

  /**
   * This static final variable will be set once the command instance is
   * initiated. This stores the command name.
   */
  private static final String COMMAND_NAME = "cat";

  /**
   * This static final variable will be set once the command instance is
   * initiated. This stores the command descriptions.
   */
  private static final String COMMAND_DESCRIPTION =
      "Display the contents of FILE1 and other files (i.e. File2 ....)\n"
          + "concatenated in the shell. You may want to use three line breaks\n"
          + "to " + "separate the contents of one file from the other file.\n"
          + "The format should be \"cat FILE...\"";

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
   * This method will return the content of the file. **NOTE**: The input string
   * array should be in format of [FileName...].
   * 
   * @param rearrangedInputArray The array should be [fileName, ...].
   * @return The file contents or error message.
   */
  @Override
  public String performAction(String[] rearrangedInputArray) {
    try {
      if (rearrangedInputArray.length == 0) {
        // no argument were given
        throw new InputFormatException("cat needs at least 1 argument.\n");
      }
      // if we need to capture
      if (Capturer.checkIfCapture(rearrangedInputArray)) {
        // capture, do nothing, do not create file.
        return Capturer.capture(rearrangedInputArray, COMMAND_NAME);
      }
      // do not need to capture, proceed normally
      return displayFiles(rearrangedInputArray);
    } catch (InputFormatException e) {
      return COMMAND_NAME + "*: " + e.getMessage();
    }
  }

  /**
   * This helper method will display files in a array and it will also throw
   * 
   * @param fileNames the array of file names
   * @return the concatenated result
   */
  private String displayFiles(String[] fileNames) {
    String result = "";
    String fileName;
    for (int i = 0; i < fileNames.length; i++) {
      // loop all filenames that to be displayed
      fileName = fileNames[i];
      try {
        result += fileName + ": \n" + Directory.getCurrentDirectory()
            .getFileUnderCurrentDirectory(fileName).display() + "\n";
      } catch (FileNotFoundException e) {
        // add to results
        if (Directory.validateDirectoryHybrid(fileName)) {
          // see if its a directory
          result +=
              COMMAND_NAME + "*: " + "\"" + fileName + "\" is a directory.\n";
        } else {
          // it's not a directory nor a fileName
          result += COMMAND_NAME + "*: "
              + e.getMessage().substring(0, e.getMessage().length() - 1);
        }
      }
    }
    return result;
  }
}
