package commands;

import Exceptions.HistoryStackInsufficientException;

/**
 * This class is responsible for recalling the history commands.
 * 
 * @author Mike Ma
 */
public class RecallHistoryCommand implements Command {

  /**
   * This static final variable will be set once the command instance is
   * initiated. This stores the command name.
   */
  private static final String COMMAND_NAME = "!";

  /**
   * This static final variable will be set once the command instance is
   * initiated. This stores the command descriptions.
   */
  private static final String COMMAND_DESCRIPTION =
      "This command will recall any of previous history by its number.\n"
          + "The format should be 鈥�!NUMBER鈥�.\n";

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
   * This method will re-do the command get from the history stack.
   * 
   * @param rearrangedInputArray the input array
   * @return the result
   */
  @Override
  public String performAction(String[] rearrangedInputArray) {
    InputProcessor inputProcessor = new InputProcessor();
    try {
      if (Capturer.checkIfCapture(rearrangedInputArray)) {
        return Capturer.capture(rearrangedInputArray, COMMAND_NAME);
      }
      // parse the integer or throw exception when its not integer
      int number = Integer.parseInt(rearrangedInputArray[0]);
      if (number <= 0) {
        throw new HistoryStackInsufficientException(
            "The number should not be larger than 0.\n");
      }
      if (number <= History.getHistoryStack().size()) {
        // when the number is acceptable
        // return the command input
        String command = History.returnHistoryWithNumber(number);
        // record the command again
        History.recordHistory(command);
        // return the output again
        return inputProcessor.startValidating(command);
      } else {
        throw new HistoryStackInsufficientException(
            "The history stack size is smaller than" + " "
                + Integer.toString(number) + ".\n");
      }
    } catch (NumberFormatException e) {
      return COMMAND_NAME + "*: Redo can only take an integer.\n";
    } catch (HistoryStackInsufficientException e) {
      return COMMAND_NAME + "*: " + e.getMessage();
    }
  }
}
