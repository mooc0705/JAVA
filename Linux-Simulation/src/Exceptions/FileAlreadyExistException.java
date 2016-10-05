package Exceptions;

@SuppressWarnings("serial")
public class FileAlreadyExistException extends Exception {

  public FileAlreadyExistException() {
    super();
  }

  public FileAlreadyExistException(String msg) {
    super(msg);
  }

}
