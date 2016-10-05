package commands;

/**
 * This class is responsible for printing the user input.
 * 
 * @author Mike Ma
 */
public class PrintInput implements Command {

  /**
   * This static final variable will be set once the command instance is
   * initiated. This stores the command name.
   */
  private static final String COMMAND_NAME = "echo";

  /**
   * This static final variable will be set once the command instance is
   * initiated. This stores the command descriptions.
   */
  private static final String COMMAND_DESCRIPTION =
      "Print the input by echo STRING where STRING is a string of characters\n"
          + "surrounded by double quotation marks. \n "
          + "The format should be \"echo CONTENT\".";

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
   * This method will return the input string. **NOTE**: The input array should
   * be [input].
   * 
   * @param rearrangedInputArray this will contain the input information to be
   *        printed
   * @return the input information or error message
   */
  @Override
  public String performAction(String[] rearrangedInputArray) {
    if (rearrangedInputArray[0].equals("")) {
      // the argument is empty.
      return COMMAND_NAME + "*: Echo needs at least 1 argument.\n";
    } else {
      String Input;
      try {
        Input = rearrangedInputArray[0];
        // return the input
        return Input.substring(Input.indexOf("\"") + 1, Input.lastIndexOf("\""))
            + "\n";
      } catch (Exception e) {
        return COMMAND_NAME
            + "*: Echo STRING is the correct format and STRING need to be "
            + "surrounded by quotes.\n";
      }
    }
  }
}
