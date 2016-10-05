package driver;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import commands.History;
import commands.InputProcessor;
import fileSystem.Directory;

public class TestInputs {


  public static void main(String[] args) throws IOException {

    FileInputStream fStream = new FileInputStream(
        "/Users/mikema/documents/workspace/csc207assignment2b/src/driver/test.txt");
    @SuppressWarnings("resource")
    BufferedReader br = new BufferedReader(new InputStreamReader(fStream));

    InputProcessor inputProcessor = new InputProcessor();
    // setUp home directory, start the file system and command processor
    Directory.createHomeDirectory();

    while (true) {
      if (Directory.getCurrentDirectory().getDirectoryName().equals("/")) {
        System.out.print("/# ");
      } else {
        System.out
            .print(Directory.getCurrentDirectory().getDirectoryName() + "/# ");
      }
      String input = br.readLine();
      // record history whenever there is input.
      System.out.println(input);
      History.recordHistory(input);
      // the following chunk of code is for dealing with empty input
      // obtain result when input is not empty
      String result = inputProcessor.startValidating(input);
      if (result == null) {
        // if nothing need to be printed.
        continue;
      } else {
        System.out.print(result);
      }
    }
  }
}


