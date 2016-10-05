package Exceptions;

@SuppressWarnings("serial")
public class FileNotFoundException extends Exception {
  
  public FileNotFoundException() {
    super();
  }

  public FileNotFoundException(String fileName) {
    super("File \"" + fileName + "\" is not found.\n");
  }
}
