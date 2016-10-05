package commands;

import Exceptions.InputFormatException;

/**
 * This class is responsible for printing the commands documentation or manuals.
 * 
 */
public class PrintCmdDocumentation implements Command {

  /**
   * This static final variable will be set once the command instance is
   * initiated. This stores the command name.
   */
  private static final String COMMAND_NAME = "man";

  /**
   * This static final variable will be set once the command instance is
   * initiated. This stores the command descriptions.
   */
  private static final String COMMAND_DESCRIPTION =
      "Print the documentation of the command. In this JShell System, \nthe "
          + "commands supported are \"mkdir\", \"cd\", \"mv\", \"cp\", \"ls\", "
          + "\n\"pwd\", \"cat\", \"get\", \"pusd\", \"popd\", \"grep\", \"!#\""
          + ", \"echo\" and \"history\".";

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
   * This method will print the proper command documentation. **NOTE**: The
   * input array is [name]
   * 
   * @param rearrangedInputArray the array will contain one name
   * @return the documentation of the command
   */
  @Override
  public String performAction(String[] rearrangedInputArray) {
    try {
      if (rearrangedInputArray.length == 0) {
        // no argument were given
        throw new InputFormatException("man needs at least 1 argument.\n");
      } else if (rearrangedInputArray.length == 1) {
        if (rearrangedInputArray[0].equals("echo")) {
          // special case of echo
          return specialMethodForEcho();
        } else {
          // if the input is not echo, then get class name
          return getOtherClassDescription(rearrangedInputArray[0]);
        }
      } else {
        // more than 1 argument were given.
        if (Capturer.checkIfCapture(rearrangedInputArray)) {
          // capture, do nothing, do not create file.
          return Capturer.capture(rearrangedInputArray, COMMAND_NAME);
        }
        throw new InputFormatException(
            "Man command can only take in 1 input of command name.\n");
      }
    } catch (InstantiationException | IllegalAccessException
        | ClassNotFoundException e) {
      return COMMAND_NAME + "*: The command \"" + rearrangedInputArray[0]
          + "\" was not found.\n";
    } catch (InputFormatException e) {
      return COMMAND_NAME + "*: " + e.getMessage();
    }
  }

  /**
   * This helper method is for get other classes' description and throw
   * exceptions if the class weren't found.
   * 
   * @param commandName the class name
   * @return the class description
   * @throws InstantiationException if the class weren't found
   * @throws IllegalAccessException if the class weren't found
   * @throws ClassNotFoundException if the class weren't found
   */
  private String getOtherClassDescription(String commandName)
      throws InstantiationException, IllegalAccessException,
      ClassNotFoundException {
    String className = InputProcessor.getHashTable().get(commandName);
    // create a new instance and execute the method
    Object a = Class.forName("commands." + className).newInstance();
    return ((Command) a).getCommandName() + ": \n" + "    "
        + ((Command) a).getCommandDescription() + "\n";
  }

  /**
   * This helper method is designed for man ECHO command, because echo has three
   * way to utilize.
   * 
   * @return all the documentation for echo related command
   */
  private String specialMethodForEcho() {
    try {
      Object echo = Class.forName("commands.PrintInput").newInstance();
      Object echoAppend =
          Class.forName("commands.AppendContents").newInstance();
      Object echoOverWrite =
          Class.forName("commands.OverwriteContent").newInstance();
      return ((PrintInput) echo).getCommandDescription() + "\n\n"
          + ((AppendContents) echoAppend).getCommandDescription() + "\n\n"
          + ((OverwriteContent) echoOverWrite).getCommandDescription() + "\n";
    } catch (InstantiationException | IllegalAccessException
        | ClassNotFoundException e) {
      // this will never happen
      return null;
    }
  }
}
