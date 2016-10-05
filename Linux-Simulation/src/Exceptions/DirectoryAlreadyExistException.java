package Exceptions;

@SuppressWarnings("serial")
public class DirectoryAlreadyExistException extends Exception {

  public DirectoryAlreadyExistException() {
    super();
  }

  public DirectoryAlreadyExistException(String nameOfDir) {
    super("Directory " + "\"" + nameOfDir + "\"" + " already exist\n");
  }

}
