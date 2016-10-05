package Exceptions;

@SuppressWarnings("serial")
public class DirectoryNotFoundException extends Exception {

  public DirectoryNotFoundException() {
    super();
  }

  public DirectoryNotFoundException(String dir) {
    super("Directory \"" + dir + "\" is not found!\n");
  }

}
