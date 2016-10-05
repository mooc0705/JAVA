// **********************************************************
// Assignment2:
// Student1:
// CDF user_name: c5chenpe
// UT Student #: 1000972584
// Author: Peikun Chen
//
// Student2:
// CDF user_name: c5mamike
// UT Student #: 998293573
// Author: Mike Ma
//
// Student3:
// CDF user_name: c4duguan
// UT Student #: 1001368940
// Author: Guangyu Du
//
// Student4:
// CDF user_name: c5zouyuq
// UT Student #: 1001196635
// Author: Yuqing Zou
//
//
// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC 207 and understand the consequences.
// *********************************************************
package driver;

import java.util.Scanner;
import commands.InputProcessor;
import commands.History;
import fileSystem.Directory;

/**
 * This class is responsible for running a JShell command system.
 * 
 * @author Peikun Chen
 */
public class JShell {

  /**
   * This static string contains the introduction of the JShell system.
   */
  public static final String INTRODUCTION =
      "Welcome to the JShell System! Use \"man man\" command to get more \n"
          + "information! :) ";

  /**
   * This main method will create a new JShell and perform actions, accepting
   * inputs.
   * 
   * @param args default arguments
   */
  public static void main(String[] args) {
    JShell newJShell = new JShell();
    // set up
    newJShell.setUpBeforeStartShell();
    System.out.println(INTRODUCTION);
    while (true) {
      String result = newJShell.startShell();
      if (result == null) {
        // nothing need to be printed
        continue;
      }
      System.out.print(result);
    }
  }

  /**
   * The inputProcessor that will be responsible for processing user inputs and
   * distribution.
   */
  private InputProcessor inputProcessor;

  /**
   * The scanner used to detect user inputs.
   */
  private Scanner sc;

  /**
   * Create a new scanner, create a new InputProcessor, create a home directory
   * , and return a new scanner. **NOTE**: This method must be called before
   * start shell.
   */
  private void setUpBeforeStartShell() {
    sc = new Scanner(System.in);
    inputProcessor = new InputProcessor();
    // setUp home directory, start the file system and command processor
    Directory.createHomeDirectory();
  }

  /**
   * This method will start a shell take a scanner as input, then perform
   * different functionalities. It will return
   * 
   * @return The result or error messages.
   */
  private String startShell() {
    // prompting with current directory name
    if (Directory.getCurrentDirectory().getDirectoryName().equals("/")) {
      System.out.print("/# ");
    } else {
      System.out
          .print(Directory.getCurrentDirectory().getDirectoryName() + "/# ");
    }
    String input = sc.nextLine();
    // the following chunk of code is for dealing with empty input
    if (recordValidator(input) == -1) {
      return null;
    }
    // obtain result when input is not empty
    String result = inputProcessor.startValidating(input);
    if (result == null) {
      // if nothing need to be printed.
      return null;
    } else {
      return result;
    }
  }

  /**
   * This helper method will check if the input should be recorded. It will
   * return 1, the program shall continue to input processor. -1 the program
   * shall keep prompting.
   * 
   * @param input the input
   * @return -1 or 1
   */
  private int recordValidator(String input) {
    String[] inputArray = input.split("\\s+");
    if (inputArray.length == 0) {
      // input is none.
      return -1;
    } else if (inputArray.length == 1 && inputArray[0].equals("")) {
      // input has only spaces.
      History.recordHistory(input);
      return -1;
    }
    if (!(inputArray.length == 1 && inputArray[0].startsWith("!"))) {
      History.recordHistory(input);
      return 1;
    }
    return 1;
  }
}
