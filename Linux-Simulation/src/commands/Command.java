package commands;

/**
 * This interface is for commands that exist inside the JShell file system. It
 * provides the basic structure for a command class.
 * 
 */
public interface Command {

  /**
   * This method will return the name of the command.
   * 
   * @return The name of the command.
   */
  public abstract String getCommandName();

  /**
   * This method will return the description of the command.
   * 
   * @return The name of the command.
   */
  public abstract String getCommandDescription();

  /**
   * This method will perform the proper function of the command.
   * 
   * @param rearrangedInputArray The rearranged array of input.
   * @return The result or error message.
   */
  public abstract String performAction(String[] rearrangedInputArray);

}
