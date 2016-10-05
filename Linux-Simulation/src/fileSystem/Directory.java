package fileSystem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import Exceptions.DirectoryNotFoundException;
import Exceptions.FileNotFoundException;

/**
 * This class is responsible for the directory system in a file system. It
 * provides basic structure, methods and essentials for a file system.
 * 
 * @author Peikun Chen
 */
public class Directory implements Serializable {

  /**
   * This is the default serial ID for Directory, for deep copy.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The directory stack that helps "popd" and "pushd".
   */
  private static ArrayList<Directory> directoryStack;

  /**
   * The name of the directory.
   */
  private String directoryName;

  /**
   * The absolute path of the directory.
   */
  private String absoluteDirectoryPath;

  /**
   * The current directory.
   */
  private static Directory currentDirectory;

  /**
   * The parent directory of this directory.
   */
  private Directory parentDirectory;

  /**
   * The list of child directories under this directory.
   */
  private ArrayList<Directory> childDirectory;

  /**
   * The list of files under this directory.
   */
  private ArrayList<File> filesUnderCurrentDirectory;

  /**
   * The root directory of the directory class.
   */
  private static Directory rootDir;

  /**
   * A private constructor that needs a name as input to create a Directory.
   * 
   * @param name The name of the directory to create.
   */
  private Directory(String name) {
    directoryName = name;
    childDirectory = new ArrayList<Directory>();
    filesUnderCurrentDirectory = new ArrayList<File>();
  }

  /**
   * A static factory method that create a HomeDirectory.
   * 
   * @return A home directory.
   */
  public static Directory createHomeDirectory() {
    Directory dir = new Directory("/");
    directoryStack = new ArrayList<Directory>();
    dir.absoluteDirectoryPath = "/";
    rootDir = dir;
    currentDirectory = rootDir;
    return dir;
  }

  /**
   * A static factory method that create a non-HomeDirectory. !NOTE!: before use
   * this method, check if name exist.
   * 
   * @param name The name of the Directory.
   * @param parentDir The parent Directory relative to the directory to be
   *        created.
   * @return The new directory that created.
   */
  public static Directory createNonHomeDirectory(String name,
      Directory parentDir) {
    Directory dir = new Directory(name);
    dir.setParentDirectory(parentDir);
    //
    parentDir.addChildDirectory(dir);
    dir.setAbsoluteDirectoryPath();
    //
    return dir;
  }

  /**
   * Return the current directory.
   * 
   * @return The currentDirectory.
   */
  public static Directory getCurrentDirectory() {
    return currentDirectory;
  }

  /**
   * This method will return true if the name of directory exist under the input
   * directory. Else it will return false.
   * 
   * @param dir The parent directory that you wish to check.
   * @param name The name of the sub-directory you wish to check.
   * @return True if the name exist, false if the name does not exist.
   */
  public static boolean checkIfDirNameExist(Directory dir, String name) {
    try {
      for (Directory childDir : dir.getChildDirecotryList()) {
        if (childDir.getDirectoryName().equals(name)) {
          return true;
        }
      }
      return false;
    } catch (NullPointerException e) {
      return false;
    }
  }

  /**
   * Change the current directory to the input directory.
   * 
   * @param dir The directory to change to.
   */
  public static void changeCurrentDirectory(Directory dir) {
    currentDirectory = dir;
  }

  // TODO
  public static boolean validateDirectoryHybrid(String pathInfo) {
    if (Directory.validateDirectoryFullByString(pathInfo)
        || Directory.validateDirFromNonCompleteFullPath(pathInfo)
        || Directory.validateDirFromStringRelativeWithDots(pathInfo)) {
      return true;
    }
    return false;
  }

  /**
   * This method will return the directory for any cases, as long as it comes
   * with a string.
   * 
   * @param dir the directory path to be returned
   * @return the directory
   * @throws DirectoryNotFoundException when the directory is not found
   */
  public static Directory getDirectoryHybridByString(String dir)
      throws DirectoryNotFoundException {
    if (Directory.validateDirectoryFullByString(dir)) {
      return Directory
          .getDirectoryFullPathByStringArray(Directory.splitDirectoryPath(dir));
    } else if (Directory.validateDirFromNonCompleteFullPath(dir)) {
      return Directory.getDirFromNonCompleteFullPath(dir);
    } else if (Directory.validateDirFromStringRelativeWithDots(dir)) {
      return Directory.getDirFromStringRelativeWithDots(dir);
    } else if (Directory.validateDirectoryRelatively(dir, currentDirectory)) {
      return Directory.getDirectoryRelativeByString(dir, currentDirectory);
    }
    throw new DirectoryNotFoundException(dir);
  }


  /**
   * Search if the directory is exist by relative path.
   * 
   * @param dirName A directoryName to be validated.
   * @param dirToExamine A directory to validate the dirName
   * @return True if the directory exist and otherwise False.
   */
  public static boolean validateDirectoryRelatively(String dirName,
      Directory dirToExamine) {
    for (Directory childDir : dirToExamine.childDirectory) {
      if (childDir.getDirectoryName().equals(dirName)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Check if the directory stack is empty.
   * 
   * @return True if the stack is empty otherwise false.
   */
  public static boolean stackIsEmpty() {
    return directoryStack.isEmpty();
  }

  /**
   * Push the directory into stack.
   * 
   * @param dir The directory to be pushed.
   */
  public static void pushDirStack(Directory dir) {
    directoryStack.add(dir);
  }

  /**
   * Pop the last entry from directory Stack.
   * 
   * @return The last directory in directory stack.
   */
  public static Directory popDirStack() {
    return directoryStack.remove(directoryStack.size() - 1);
  }

  /**
   * This helper method will return the directoryStack of the Directory lass.
   * 
   * @return The directory stack.
   */
  public static ArrayList<Directory> getDirectoryStack() {
    return directoryStack;
  }

  /**
   * This method will return the root Directory.
   * 
   * @return The root Directory instance.
   */
  public static Directory getRootDirectory() {
    return rootDir;
  }

  /**
   * This method will split String representation of a path into a String array
   * of directory path. Split them by "/". Example: "/a/b" => [[ ], [a], [b]].
   * 
   * @param path The string representation of a path.
   * @return The string array that contains the path information.
   */
  public static String[] splitDirectoryPath(String path) {
    return path.split("/");
  }

  /**
   * This method will return the file for any cases, as long as it comes with a
   * string.
   * 
   * @param dir the directory path to be returned
   * @return the file
   * @throws DirectoryNotFoundException when the directory is not found
   * @throws FileNotFoundException when the file is not found
   */
  public static File getFileHybrid(String fileNameOrPath)
      throws FileNotFoundException, DirectoryNotFoundException {
    if (fileNameOrPath.contains("/")) {
      return getFileFromPath(fileNameOrPath);
    }
    return currentDirectory.getFileUnderCurrentDirectory(fileNameOrPath);
  }

  /**
   * This helper method will get file from its path while throw exceptions if
   * the file does not exist.
   * 
   * @param path the file path
   * @return the File
   * @throws DirectoryNotFoundException when the directory does not exist
   * @throws FileNotFoundException when the file does not exist
   */
  public static File getFileFromPath(String path)
      throws DirectoryNotFoundException, FileNotFoundException {
    String pathShouldExist = path.substring(0, path.lastIndexOf("/"));
    String fileName = path.substring(path.lastIndexOf("/") + 1);
    Directory dirExist = getDirectoryHybridByString(pathShouldExist);
    return dirExist.getFileUnderCurrentDirectory(fileName);
  }

  /**
   * The method will get the file that under the current directory.
   * 
   * @param fileName The file name.
   * @return The file.
   * @throws FileNotFoundException
   */
  public File getFileUnderCurrentDirectory(String fileName)
      throws FileNotFoundException {
    for (File singleFile : currentDirectory.filesUnderCurrentDirectory) {
      if (singleFile.getMyFileName().equals(fileName)) {
        return singleFile;
      }
    }
    throw new FileNotFoundException(fileName);
  }

  /**
   * This method will return true if the name of file exist under current
   * directory. Else it will return false.
   * 
   * @param name The name of the file you wish to check.
   * @return True if the name exist, false if the name does not exist.
   * @throws FileNotFoundException
   */
  public boolean checkIfFileNameExist(String name) {
    try {
      for (File singleFile : this.filesUnderCurrentDirectory) {
        if (singleFile.getMyFileName().equals(name)) {
          return true;
        }
      }
      return false;
    } catch (NullPointerException e) {
      return false;
    }
  }

  /**
   * Return the this Directory name.
   * 
   * @return The name of the this directory.
   */
  public String getDirectoryName() {
    return this.directoryName;
  }

  /**
   * Change this directory name.
   * 
   * @param newName
   */
  public void changeDirectoryName(String newName) {
    this.directoryName = newName;
  }

  /**
   * Get the list of child Directory under this Directory.
   * 
   * @return A list of childDirecotry.
   */
  public List<Directory> getChildDirecotryList() {
    List<Directory> copy = new ArrayList<Directory>();
    copy.addAll(childDirectory);
    return copy;
  }

  /**
   * Add a file to this directory.
   * 
   * @param file The file to be added.
   */
  public void addFileToDirectory(File file) {
    this.filesUnderCurrentDirectory.add(file);
  }

  /**
   * Remove a file to this directory.
   * 
   * @param file the file to be removed
   */
  public void removeFile(File file) {
    this.filesUnderCurrentDirectory.remove(file);
  }

  /**
   * Get the list of files under this directory.
   * 
   * @return A list of files.
   */
  public ArrayList<File> getFileListUnderCurrentDirectory() {
    return this.filesUnderCurrentDirectory;
  }

  /**
   * Get the parent directory of this directory.
   * 
   * @return The parent directory.
   */
  public Directory getParentDirectory() {
    if (this.equals(rootDir)) {
      return rootDir;
    }
    return this.parentDirectory;
  }

  /**
   * Get String representation of the absolute directory path.
   * 
   * @return The absolute path.
   */
  public String getAbsoluteDirectoryPath() {
    return absoluteDirectoryPath;
  }

  /**
   * Add a child directory to the directory tree.
   * 
   * @param childDirectory The directory to be added.
   */
  public void addChildDirectory(Directory childDirectory) {
    this.childDirectory.add(childDirectory);
  }

  /**
   * Change the parent directory of this directory, as well as the absolute path
   * and all its sub-directories and files under that directory.
   */
  public void changeParentDirectory(Directory newparent) {
    parentDirectory.childDirectory.remove(this);
    parentDirectory = newparent;
    newparent.addChildDirectory(this);
    this.absoluteDirectoryPath =
        newparent.absoluteDirectoryPath + "/" + this.directoryName;
    this.changeDirectoryAbsolutePathRecursively();
    this.changeFileAbsolutePathRecursively();
  }

  /**
   * This method will return a deep copy of the Directory.
   * 
   * @return a copy of the directory
   */
  public Directory getCopyOfDirectory() {
    ObjectOutputStream oos = null;
    ObjectInputStream ois = null;
    try {
      Directory copy = null;
      // deep copy
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      oos = new ObjectOutputStream(bos);
      // serialize and pass the object
      oos.writeObject(this);
      oos.flush();
      ByteArrayInputStream bin = new ByteArrayInputStream(bos.toByteArray());
      ois = new ObjectInputStream(bin);
      // return the new object
      copy = (Directory) ois.readObject();
      oos.close();
      ois.close();
      return copy;
    } catch (Exception e) {
      System.out.println("Exception in main = " + e);
      return null;
    }
  }

  /**
   * This helper method will check if a directory is a sub-directory of this
   * directory.
   * 
   * @param dir the directory to be checked
   * @return true or false
   */
  public boolean checkIfSubDirectory(Directory dir) {
    for (Directory subDir : this.childDirectory) {
      if (!subDir.equals(dir)) {
        return subDir.checkIfSubDirectory(dir);
      }
      return true;
    }
    return false;
  }

  /**
   * Validate if a directory exist by taking in the string array of the path.
   * 
   * @param dir A String array contains full path directory information.
   * @return True if the Directory exist else return False.
   */
  private static boolean validateDirectoryFullByStringArray(String[] dir) {
    // if (dir[0].equals(rootDir.getDirectoryName()) && dir.length == 1) {
    if (dir.length == 0 || dir[0].equals("") && dir.length == 1) {
      // it only contains root directory
      return true;
    } else {
      if (dir[0].equals("")) {
        // first element must be root directory.
        Directory currentDir = rootDir;
        for (int i = 1; i < dir.length; i++) {
          ArrayList<Directory> childDirs = currentDir.childDirectory;
          if (!childDirs.isEmpty()) {
            // start searching in child directories.
            inner: for (Directory singleDir : childDirs) {
              if (singleDir.getDirectoryName().equals(dir[i])) {
                if (i + 1 < dir.length) {
                  currentDir = singleDir;
                  break inner;
                } else {
                  return true;
                }
              }
            }
          } else {
            // the child directory list is empty
            return false;
          }
        }
        return false;
      } else {
        // first element is not root directory, then this is false.
        return false;
      }
    }
  }

  /**
   * This method validate directory path by a string representation of a
   * directory path.
   * 
   * @param dirPath The path to be examined.
   * @return True if the path exist otherwise false.
   */
  private static boolean validateDirectoryFullByString(String dirPath) {
    return validateDirectoryFullByStringArray(dirPath.split("/"));
  }

  /**
   * This helper method will validate if a non-complete full path string is
   * valid.
   * 
   * @param path The path to be examined.
   * @return True if the path exist, false otherwise.
   */
  private static boolean validateDirFromNonCompleteFullPath(String path) {
    String tempFullDir = currentDirectory.absoluteDirectoryPath + "/" + path;
    return validateDirectoryFullByString(tempFullDir);
  }

  /**
   * This helper method is build specially for the notation of "." and ".." used
   * in directory path. This will validate this kind of path. It will return
   * true if the directory is valid otherwise false.
   * 
   * @param path The relative path with "." or "..".
   * @return True if the directory is valid otherwise false.
   */
  private static boolean validateDirFromStringRelativeWithDots(String path) {
    String[] thePathArray = path.split("/");
    if (thePathArray[0].equals(".")) {
      // current directory
      if (thePathArray.length == 1) {
        return true;
      } else {
        // make a new temporary full directory path
        String tempFullDir =
            currentDirectory.absoluteDirectoryPath + "/" + path.substring(2);
        return validateDirectoryFullByString(tempFullDir);
      }
    } else if (thePathArray[0].equals("..")) {
      // the parent directory
      if (thePathArray.length == 1) {
        return true;
      } else {
        String tempFullDir =
            currentDirectory.parentDirectory.absoluteDirectoryPath + "/"
                + path.substring(3);
        return validateDirectoryFullByString(tempFullDir);
      }
    }
    // The format is wrong
    return false;
  }

  /**
   * Get Directory from string array. !NOTE!: HAVE TO VALIDATE DIR BEFORE USING
   * THIS METHOD.
   * 
   * @param dir A String array contains full path directory information.
   * @return The bottom Directory in the path provided.
   */
  private static Directory getDirectoryFullPathByStringArray(String[] dir) {
    Directory currentDir = rootDir;
    if (dir.length == 0 || (dir[0].equals("") && dir.length == 1)) {
      return currentDir;
    } else {
      ArrayList<Directory> childDirs = currentDir.childDirectory;
      for (int i = 1; i < dir.length; i++) {
        String dirChecking = dir[i];
        innerLoop: for (Directory singleDir : childDirs) {
          if (singleDir.getDirectoryName().equals(dirChecking)) {
            currentDir = singleDir;
            childDirs = currentDir.childDirectory;
            break innerLoop;
          }
        }
      }
      return currentDir;
    }
  }

  /**
   * This method will return the Directory instance from a String name. Finally
   * it will return that corresponding child directory.
   * 
   * @param dirName The directory name that will be checked.
   * @param dir The directory will be used for checking the name.
   * @return Null if the directory does not exist, otherwise return the
   *         directory.
   */
  private static Directory getDirectoryRelativeByString(String dirName,
      Directory dir) {
    Directory result = null;
    for (int i = 0; i < dir.getChildDirecotryList().size(); i++) {
      Directory dirChecking = dir.getChildDirecotryList().get(i);
      if (dirChecking.getDirectoryName().equals(dirName)) {
        result = dirChecking;
        break;
      }
    }
    return result;
  }

  /**
   * This method will return the directory by taking in the relative path with
   * special notation "." or "..".
   * 
   * @param path The path with dots.
   * @return The corresponding directory.
   */
  private static Directory getDirFromStringRelativeWithDots(String path) {
    String[] thePathArray = Directory.splitDirectoryPath(path);
    if (thePathArray[0].equals(".")) {
      // current directory
      Directory currentDir = currentDirectory;
      if (thePathArray.length == 1) {
        return currentDir;
      } else {
        String tempFullDir =
            currentDirectory.absoluteDirectoryPath + "/" + path.substring(2);
        return getDirectoryFullPathByStringArray(
            splitDirectoryPath(tempFullDir));
      }
    } else if (thePathArray[0].equals("..")) {
      // the parent directory
      if (currentDirectory == rootDir) {
        return rootDir;
      }
      String tempFullDir =
          currentDirectory.parentDirectory.absoluteDirectoryPath + "/"
              + path.substring(3);
      return getDirectoryFullPathByStringArray(splitDirectoryPath(tempFullDir));
    }
    // The format is wrong
    return null;
  }

  /**
   * This helper method is designed for non-complete full path.
   * 
   * @param path The relative non-complete full path.
   * @return The directory corresponding to the end of the path.
   */
  private static Directory getDirFromNonCompleteFullPath(String path) {
    String tempFullDir = currentDirectory.absoluteDirectoryPath + "/" + path;
    return getDirectoryFullPathByStringArray(splitDirectoryPath(tempFullDir));
  }

  /**
   * Set this directory's parent directory.
   * 
   * @param parentDir
   */
  private void setParentDirectory(Directory parentDir) {
    this.parentDirectory = parentDir;
  }

  /**
   * Set the absolute directory path.
   */
  private void setAbsoluteDirectoryPath() {
    if (this.parentDirectory.absoluteDirectoryPath.equals("/")) {
      this.absoluteDirectoryPath =
          this.parentDirectory.absoluteDirectoryPath + this.getDirectoryName();
    } else {
      this.absoluteDirectoryPath = this.parentDirectory.absoluteDirectoryPath
          + "/" + this.getDirectoryName();
    }
  }

  /**
   * This helper method will change all the absolute path of the
   * sub-directories.
   */
  private void changeDirectoryAbsolutePathRecursively() {
    for (Directory subDir : this.childDirectory) {
      subDir.absoluteDirectoryPath =
          this.absoluteDirectoryPath + "/" + subDir.directoryName;
      subDir.changeDirectoryAbsolutePathRecursively();
    }
  }

  /*
   * This helper method will change all the file absolute path inside the
   * directory and it's sub-directories.
   */
  private void changeFileAbsolutePathRecursively() {
    for (File singleFile : this.filesUnderCurrentDirectory) {
      singleFile.changeFileFullPath(
          absoluteDirectoryPath + "/" + singleFile.getMyFileName());
    }
    for (Directory subDir : this.childDirectory) {
      subDir.changeFileAbsolutePathRecursively();
    }
  }
}
