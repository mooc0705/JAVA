package commands;

import java.io.BufferedReader;
import fileSystem.Directory;
import fileSystem.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import Exceptions.FileAlreadyExistException;
import Exceptions.FileNotFoundException;
import Exceptions.InputFormatException;

/**
 * This class is responsible for get URL content and create a new file
 * containing the contents under the current directory.
 * 
 * @author Mike Ma
 */
public class GetUrl implements Command {

  /**
   * This static final variable will be set once the command instance is
   * initiated. This stores the command name.
   */
  private static final String COMMAND_NAME = "get";

  /**
   * This static final variable will be set once the command instance is
   * initiated. This stores the command descriptions.
   */
  private static final String COMMAND_DESCRIPTION =
      "Retrieve the file at that URL and add it to the current working \n"
          + "directory.\n" + "The format should be 鈥済et URL鈥�.\n";

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
   * This method will retrieve the file at that URL and create a file containing
   * the content under the current working directory.
   * 
   * @param rearrangedInputArray the input array with a URL
   * @return null or error message
   */
  @Override
  public String performAction(String[] rearrangedInputArray) {
    try {
      if (rearrangedInputArray.length == 1
          || rearrangedInputArray.length == 3) {
        if (Capturer.checkIfCapture(rearrangedInputArray)) {
          // if need capture
          return Capturer.capture(rearrangedInputArray, COMMAND_NAME);
        }
        // retrieve file and create a file locally
        retrieveAndCreateFile(rearrangedInputArray[0]);
        return null;
      } else {
        // when the input format is wrong
        throw new InputFormatException(
            "get can only take in 1 argument which is an URL.\n");
      }
    } catch (InputFormatException | FileAlreadyExistException e) {
      return COMMAND_NAME + "*: " + e.getMessage();
    } catch (IOException e) {
      return COMMAND_NAME + "*: URL is not well formed!\n";
    }
  }

  /**
   * This helper method will retrieve file from the URL and create new File with
   * the file name. It will throw exception when the URL is not well formed.
   * 
   * @param url the URL to get
   * @throws IOException when URL is not well formed
   * @throws FileAlreadyExistException when the file with the name already exist
   */
  private void retrieveAndCreateFile(String url)
      throws IOException, FileAlreadyExistException {
    // form URL object
    URL oracle = new URL(url);
    BufferedReader in =
        new BufferedReader(new InputStreamReader(oracle.openStream()));
    String inputLine;
    File file;
    // create new file with URL file name
    String fileName = url.substring(url.lastIndexOf("/") + 1);
    try {
      file = Directory.getCurrentDirectory()
          .getFileUnderCurrentDirectory(fileName);
      // remove all content
      file.overwrite("");
    } catch (FileNotFoundException e) {
      file = File.createNewFile(fileName, Directory.getCurrentDirectory());
    }
    // append all the content to the new file created
    while ((inputLine = in.readLine()) != null)
      file.append("\"" + inputLine + "\"");
    // close the reader
    in.close();
  }
}
