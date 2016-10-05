package commands;

import java.util.Hashtable;
import java.util.regex.Pattern;

import Exceptions.IllegalCharacterException;

/**
 * This class is responsible for processing the inputs and distribute them to
 * all other responsible classes and return results or error messages. There is
 * a special case for "echo" due to it's poly-functionality.
 * 
 * @author Mike Ma
 */
public class InputProcessor {

  /**
   * This variable is for "echo" command. The method class will not initialize
   * until we were going to call the class methods.
   */
  private AppendContents appendContents;

  /**
   * This variable is for "echo" command. The method class will not initialize
   * until we were going to call the class methods.
   */
  private PrintInput printInput;

  /**
   * This variable is for "echo" command. The method class will not initialize
   * until we were going to call the class methods.
   */
  private OverwriteContent overWriteContent;

  /**
   * This hash table is used to store key and value pair information for
   * commands and command classes.
   */
  private static Hashtable<String, String> hashtableForCommands =
      new Hashtable<String, String>();

  /**
   * The constructor will automatically add all commands into the hash table.
   */
  public InputProcessor() {
    addToHashTable("mkdir", "MakeDirectory");
    addToHashTable("cd", "ChangeDirectory");
    addToHashTable("exit", "Exit");
    addToHashTable("history", "History");
    addToHashTable("ls", "ListContents");
    addToHashTable("pushd", "PushDirectory");
    addToHashTable("popd", "PopDirectory");
    addToHashTable("man", "PrintCmdDocumentation");
    addToHashTable("cat", "DisplayFilesContents");
    addToHashTable("pwd", "PrintWorkingDirectory");
    addToHashTable("get", "GetUrl");
    addToHashTable("grep", "Grep");
    addToHashTable("!", "RecallHistoryCommand");
    addToHashTable("mv", "Move");
    addToHashTable("cp", "Copy");
  }

  /**
   * A public method that allow other class to execute commands and return
   * results.
   * 
   * @param input A String of user input.
   * @return The result or error message.
   */
  public String startValidating(String input) {
    return this.excute(input);
  }

  /**
   * This helper method will return the hash table with command name.
   * 
   * @return The hash table.
   */
  public static Hashtable<String, String> getHashTable() {
    return hashtableForCommands;
  }

  /**
   * This static method will examine if the input contain any illegal
   * characters.
   * 
   * @param toExamine The string to examine.
   * @return True if the string does contain illegal char otherwise throw.
   * @throws IllegalCharacterException when it contain illegal characters
   */
  public static boolean containIllegal(String toExamine)
      throws IllegalCharacterException {
    Pattern p = Pattern.compile(
        "[\\`\\~\\#\\$\\&\\@\\*\\+\\%\\{\\}\\(\\)\\=\\[\\]\\:\\;\\<\\>\\[\\]|"
            + "\"\\_^\\|\\?\\,]");
    if (p.matcher(toExamine).find()) {
      // if matcher finds any illegal characters
      throw new IllegalCharacterException(
          "\"" + toExamine + "\" contains illegal characters!\n");
    }
    return false;
  }

  /**
   * This helper method will execute command according to the input array.
   * 
   * @param array Array with user inputs.
   * @return Result or Error message.
   */
  private String excute(String input) {
    String[] inputArray = null;
    if (input.startsWith("!")) {
      String[] tempArray = input.split("\\s+");
      inputArray = helperMethodForRedo(tempArray);
    } else {
      inputArray = input.split("\\s+");
    }
    if (inputArray[0].equals("")) {
      // there was space before input.
      // then remove the first item.
      inputArray = this.reloadArray(inputArray);
    }
    // Get command name and get command class name.
    String commandName = inputArray[0];
    String commandClass = getFromHashTable(commandName);
    // special case for echo, need special care with the input.
    if (commandName.equals("echo")) {
      return this.echo(inputArray, input);
    }
    if (commandName.equals("grep")) {
      return this.grep(inputArray, input);
    }
    // try other classes and catch exceptions expected.
    try {
      Object cmdInstance =
          Class.forName("commands." + commandClass).newInstance();
      return ((Command) cmdInstance)
          .performAction(this.reloadArray(inputArray));
    } catch (InstantiationException | IllegalAccessException
        | ClassNotFoundException e) {
      return commandName + ": Command not found.\n";
    }
  }

  /**
   * This helper method is for 'grep', to process the quotes around the REGEX.
   * 
   * @param array the input array
   * @param input the input
   * @return the result or error message
   */
  private String grep(String[] array, String input) {
    // trim the front and the back of the input.
    String trimmed = input.trim();
    try {
      Grep grep = new Grep();
      String regex = trimmed.substring(trimmed.indexOf("\"") + 1,
          trimmed.lastIndexOf("\""));
      String reformed =
          trimmed.substring(0, trimmed.indexOf("\"")) + " \"" + regex.trim()
              + "\" " + trimmed.substring(trimmed.lastIndexOf("\"") + 1);
      String[] inputArray = reformed.split("\\s+");
      return grep.performAction(this.reloadArray(inputArray));
    } catch (StringIndexOutOfBoundsException e) {
      return "grep*: String must be surrounded by quotes.\n";
    }
  }

  /**
   * A helper method perform the functionality of echo. And perform different
   * echo functions depends on the requirement.
   * 
   * @param array Array of command input.
   * @return Error message or results.
   */
  private String echo(String[] array, String input) {
    if (array.length == 4) {
      String result;
      // trim the front and the back of the input.
      String trimmed = input.trim();
      // The instance of method class will initialize only if when we are going
      // to use them, so it won't waste space in heap.
      appendContents = new AppendContents();
      printInput = new PrintInput();
      overWriteContent = new OverwriteContent();
      try {
        if (trimmed.contains(">>")) {
          // append
          result = appendContents
              .performAction(loadInputArrayForEcho(trimmed, ">>"));
        } else if (trimmed.contains(">")) {
          // overwrite
          result = overWriteContent
              .performAction(loadInputArrayForEcho(trimmed, ">"));
        } else {
          // print input
          String[] toBePrint = new String[1];
          toBePrint[0] =
              input.substring(input.indexOf("echo") + 4, input.length());
          result = printInput.performAction(toBePrint);
        }
        return result;
      } catch (StringIndexOutOfBoundsException e) {
        return "echo*: String must be surrounded by quotes.\n";
      }
    }
    return "echo*: Too much arguments. check manual for more information.\n";
  }

  /**
   * This helper method will load array for echo command for execution.
   * 
   * @param inputString The trimmed string of input.
   * @param appendOrOverwrite The notation of ">>" or ">".
   * @return A loaded array.
   */
  private String[] loadInputArrayForEcho(String inputString,
      String appendOrOverwrite) {
    String[] result = new String[3];
    result[0] = inputString.substring(inputString.indexOf("\""),
        inputString.lastIndexOf("\"") + 1);
    result[1] = appendOrOverwrite;
    if (appendOrOverwrite.equals(">>")) {
      result[2] = inputString.substring(inputString.indexOf(">>") + 2).trim();
    } else {
      result[2] = inputString.substring(inputString.indexOf(">") + 1).trim();
    }
    return result;
  }

  /**
   * This helper method will get value from hash table given with key.
   * 
   * @param key The command name.
   * @return The command class name.
   */
  private String getFromHashTable(String key) {
    return hashtableForCommands.get(key);
  }

  /**
   * This helper method will return a new string array.
   * 
   * @param array the input array
   * @return the result
   */
  private String[] helperMethodForRedo(String[] array) {
    String[] result = new String[array.length + 1];
    for (int i = 0; i < result.length; i++) {
      if (i == 0) {
        result[i] = "!";
      } else if (i == 1) {
        result[i] = array[0].substring(1);
      } else {
        result[i] = array[i - 1];
      }
    }
    return result;
  }

  /**
   * This helper method will eliminate first item in array.
   * 
   * @param Original array.
   * @return The array that eliminated first item in the original array.
   */
  private String[] reloadArray(String[] original) {
    String[] rearranged = new String[original.length - 1];
    for (int i = 1; i < original.length; i++) {
      // reposition the array items
      rearranged[i - 1] = original[i];
    }
    return rearranged;
  }

  /**
   * This helper method will add key and value pair into the hash table.
   * 
   * @param key The key.
   * @param value The value.
   */
  private void addToHashTable(String key, String value) {
    hashtableForCommands.put(key, value);
  }
}
