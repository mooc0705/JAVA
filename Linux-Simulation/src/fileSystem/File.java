package fileSystem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import Exceptions.FileAlreadyExistException;

/**
 * This class is responsible for a type file in a file system.
 * 
 * @author Peikun Chen
 * @author Mike Ma
 */
public class File implements Serializable {

  /**
   * This is the default serial ID for File, for deep copy.
   */
  private static final long serialVersionUID = 1L;

  /**
   * This is the directory that the file belongs to.
   */
  private Directory myDirectory;

  /**
   * This is the name of the file.
   */
  private String fileName;

  /**
   * This is the content of the file.
   */
  private String content = "";

  /**
   * This is the full path of the file.
   */
  private String thisFileFullPath;

  /**
   * This constructor will create a File with a name and the directory it
   * belongs to.
   * 
   * @param name The fileName.
   * @param dir The directory it belongs to.
   */
  private File(String name, Directory dir) {
    this.myDirectory = dir;
    this.fileName = name;
    if (dir.getAbsoluteDirectoryPath().equals("/")) {
      thisFileFullPath = dir.getAbsoluteDirectoryPath() + name;
    } else {
      thisFileFullPath = dir.getAbsoluteDirectoryPath() + "/" + name;
    }
  }

  /**
   * This private constructor will complete the action of create the file and
   * add it under the Directory.
   * 
   * @param name The file name.
   * @param dir The directory it belongs to.
   * @return The File just created.
   * @throws FileAlreadyExistException
   */
  public static File createNewFile(String name, Directory dir)
      throws FileAlreadyExistException {
    File temp = new File(name, dir);
    if (!dir.checkIfFileNameExist(name)) {
      dir.addFileToDirectory(temp);
      return temp;
    }
    throw new FileAlreadyExistException(
        "File named " + "\"" + name + "\" already exist!\n");
  }

  /**
   * This method will return the full path of the file.
   * 
   * @return the full path of the file
   */
  public String getMyFileFullPath() {
    return thisFileFullPath;
  }

  /**
   * This method will change the full path of the file.
   * 
   * @param newPath the new path
   */
  public void changeFileFullPath(String newPath) {
    this.thisFileFullPath = newPath;
  }

  /**
   * This method will return the Directory that the file belongs to.
   * 
   * @return The directory this file belongs to.
   */
  public Directory getMyDirectory() {
    return this.myDirectory;
  }

  /**
   * This method will overwrite the content inside the file.
   * 
   * @param newContent The new content to overwrite.
   */
  public void overwrite(String newContent) {
    content = newContent;
  }

  /**
   * This method will append the content inside the file.
   * 
   * @param newContent The new content to overwrite.
   */
  public void append(String newContent) {
    if (content == "") {
      content = newContent.substring(1, newContent.length() - 1);
    } else {
      content += ("\n" + newContent.substring(1, newContent.length() - 1));
    }
  }

  /**
   * This method will return the content inside the file.
   * 
   * @return The content inside the file.
   */
  public String display() {
    return content;
  }

  /**
   * This method will return the name of the file.
   * 
   * @return The name of the file.
   */
  public String getMyFileName() {
    return this.fileName;
  }

  // TODO
  public void changeFileDirectory(Directory dir)
      throws FileAlreadyExistException {
    myDirectory.removeFile(this);
    myDirectory = dir;
    thisFileFullPath = dir.getAbsoluteDirectoryPath() + "/" + fileName;
    if (!dir.checkIfFileNameExist(fileName)) {
      dir.addFileToDirectory(this);
    } else {
      throw new FileAlreadyExistException(fileName);
    }
  }

  /**
   * This method will return a deep copy of the Directory.
   * 
   * @return a copy of the directory
   */
  public File getCopyOfFile() {
    ObjectOutputStream oos = null;
    ObjectInputStream ois = null;
    try {
      File copy = null;
      // deep copy
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      oos = new ObjectOutputStream(bos);
      // serialize and pass the object
      oos.writeObject(this);
      oos.flush();
      ByteArrayInputStream bin = new ByteArrayInputStream(bos.toByteArray());
      ois = new ObjectInputStream(bin);
      // return the new object
      copy = (File) ois.readObject();
      oos.close();
      ois.close();
      return copy;
    } catch (Exception e) {
      System.out.println("Exception in main = " + e);
      return null;
    }
  }
}
