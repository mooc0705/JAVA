package Exceptions;

@SuppressWarnings("serial")
public class HistoryStackInsufficientException extends Exception{

  public HistoryStackInsufficientException() {
    super();
  }

  public HistoryStackInsufficientException(String msg) {
    super(msg);
  }
  
}
