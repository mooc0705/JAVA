package commands;

import java.util.ArrayList;
import java.util.List;

import Exceptions.HistoryStackInsufficientException;
import Exceptions.InputFormatException;;

/**
 * This class is responsible for printing the history of inputs.
 * 
 */
public class History implements Command {

  /**
   * This arrayList stores all the user input attempts. It will always be the
   * same once a class instance initiated.
   */
  private static List<String> historyStack = new ArrayList<String>();

  /**
   * This static final variable will be set once the command instance is
   * initiated. This stores the command name.
   */
  private static final String COMMAND_NAME = "history";

  /**
   * This static final variable will be set once the command instance is
   * initiated. This stores the command descriptions.
   */
  private static final String COMMAND_DESCRIPTION =
      "This command will print out recent commands, one command per line."
      + "\n The format should be \"history [number]\".";

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
   * This method will take in an string array of format [] or [number]. It will
   * either print out everything in history stack or it will print out last
   * [number] entry in the history stack.
   * 
   * @param rearrangedInputArray The array is either empty or has a number in
   *        it. i.e. [] or [number]
   * @return The history stack information.
   */
  @Override
  public String performAction(String[] rearrangedInputArray) {
    try {
      if (rearrangedInputArray.length == 0) {
        // print all the input history.
        return getEverythingFromHistoryStack() + "\n";
      } else if (rearrangedInputArray.length == 1) {
        // print last n history in the stack
        return this.tryHistoryInputWithNumber(rearrangedInputArray);
      } else {
        // if we need to capture.
        if (Capturer.checkIfCapture(rearrangedInputArray)) {
          // capture, do nothing, do not create file.
          return Capturer.capture(rearrangedInputArray, COMMAND_NAME);
        }
        throw new InputFormatException(
            "Please see manual for command \"history\" for arguments' type.\n");
      }
    } catch (HistoryStackInsufficientException | InputFormatException e) {
      return COMMAND_NAME + "*: " + e.getMessage();
    }
  }

  /**
   * This method will record an input into the history stack.
   * 
   * @param input The input.
   */
  public static void recordHistory(String input) {
    historyStack.add(input);
  }

  /**
   * This method will return the "number"th history from the history stack.
   * 
   * @param number The number of the index.
   * @return the string of "number"th history.
   */
  public static String returnHistoryWithNumber(int number) {
    String history = historyStack.get(number - 1);
    return history;
  }

  /**
   * This helper method will return a copy of the history stack to prevent
   * mutating the original one.
   * 
   * @return the copy of historyStack
   */
  public static List<String> getHistoryStack() {
    List<String> copy = new ArrayList<String>();
    copy.addAll(historyStack);
    return copy;
  }

  /**
   * This method will try history command with number.
   * 
   * @param array The array with a number.
   * @return The result or error message.
   * @throws HistoryStackInsufficientException when the history stack is not
   *         enough
   * @throws InputFormatException when the input format is wrong
   */
  private String tryHistoryInputWithNumber(String[] array)
      throws HistoryStackInsufficientException, InputFormatException {
    try {
      int number = Integer.parseInt(array[0]);
      if (number == 0) {
        return null;
      }
      if (number <= historyStack.size()) {
        // if number is less than the history stack
        return getFromHistroyStack(number) + "\n";
      }
      if (number < 0) {
        throw new HistoryStackInsufficientException(
            "The number should not be negative.\n");
      } else {
        // if number is larger than the history stack
        throw new HistoryStackInsufficientException(
            "The history stack size is smaller than" + " "
                + Integer.toString(number) + ".\n");
      }
    } catch (NumberFormatException e) {
      throw new InputFormatException(
          "History can either take an integer or itself.\n");
    }
  }

  /**
   * This helper method will return everything in the history stack in a String.
   * 
   * @return Everything in history stack with number labels.
   */
  private String getEverythingFromHistoryStack() {
    String result = "";
    for (int i = 0; i < historyStack.size(); i++) {
      // loop over the while stack
      result += Integer.toString(i + 1) + "  " + historyStack.get(i);
      if (i + 1 < historyStack.size()) {
        result += "\n";
      }
    }
    return result;
  }

  /**
   * This helper method will get "number" of elements from the history stack.
   * 
   * @param number Number of elements to get.
   * @return The concatenated result.
   */
  private String getFromHistroyStack(int number) {
    String result = "";
    for (int i = historyStack.size() - number; i < historyStack.size()
        && i >= 0; i++) {
      // loop over the stack with number constrain.
      result += Integer.toString(i + 1) + "  " + historyStack.get(i);
      if (i + 1 < historyStack.size()) {
        // if it is at the end at the
        result += "\n";
      }
    }
    return result;
  }
}
