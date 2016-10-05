package commands;

/**
 * This class is responsible for exiting the program.
 * 
 * @author Mike Ma
 */
public class Exit implements Command {

  /**
   * This static final variable will be set once the command instance is
   * initiated. This stores the command name.
   */
  private static final String COMMAND_NAME = "exit";
  
  /**
   * This static final variable will be set once the command instance is
   * initiated. This stores the command descriptions.
   */
  private static final String COMMAND_DESCRIPTION = "Quit the program";

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
   * This method will exit the program. **NOTE**: The array does not matter, and
   * this method will terminate the program straightly instead of return any
   * value.
   */
  @Override
  public String performAction(String[] rearrangedInputArray) {
    System.exit(0);
    return null;
  }
}
