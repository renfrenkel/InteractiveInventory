import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

// class to parse command line arguments
public class FlagParser {

   String inputFile = "";  // input file name
   String outputFile = ""; // output file name
   String loggingFile = ""; // logging file name
   String cache = ""; // cache file name
   String path = ""; // file path
   boolean openGUI = true; // should the GUIs be started
   boolean email = false; // should an email be sent if GUI's aren't opened
   String[] args; // command line arguments
   
   // constructor
   public FlagParser(String[] arguments) {
      args = arguments;
      parseFlags();
   } // close constructor
   
   // method to parse flags
   public void parseFlags() {
      for (int i=0; i<args.length; i++) {
         if (args[i].startsWith("-i") || args[i].startsWith("-I")) {
            inputFile = getFileName(args, i);
         } // close if
         if (args[i].startsWith("-o") || args[i].startsWith("-O")) {
            outputFile = getFileName(args, i);
         } // close if
         else if (args[i].startsWith("-l") || args[i].startsWith("-L")) {
            loggingFile = getFileName(args, i);
         }  // close else if
         else if (args[i].startsWith("-c") || args[i].startsWith("-C")) {
            cache = getFileName(args, i);
         }  // close else if
         else if (args[i].startsWith("-p") || args[i].startsWith("-P")) {
            openGUI = false;
            if (args[i].contains("_email")) {
               email = true;
            } // close if
         }  // close else if
         else if (args[i].startsWith("-d") || args[i].startsWith("-D")) {
            path = getFileName(args, i);
            if (!inputFile.equals("")) {
               inputFile = path + inputFile;
            } // close if
            if (!outputFile.equals("")) {
               outputFile = path + outputFile;
            } // close if
            if (!loggingFile.equals("")) {
               loggingFile = path + loggingFile;
            } // close if
            if (!cache.equals("")) {
               cache = path + cache;
            } // close if
         } // close else if
      } // close for
      if (inputFile.equals("")) {
         inputFile = chooseFile(inputFile, "input");
      } // close if
      if (outputFile.equals("")) {
         outputFile = chooseFile(outputFile, "output");
      } // close if
      if (loggingFile.equals("")) {
         loggingFile = chooseFile(loggingFile, "log");
      } // close if
      if (cache.equals("")) {
         cache = chooseFile(cache, "log");
      } // close if
   } // close ParseFlags
   
   // method to get file name from the flag
   public static String getFileName(String[] args, int index) {
      String name = "";
      if((index+1)<args.length && !args[index+1].startsWith("-")) {
         name = args[index+1];
      } // close if
      return name;
   } // close getFileName
   
   // method to choose a file
   public static String chooseFile(String file, String type) {
      String fileName = "";
      JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
      jfc.setDialogTitle("Choose " + type + " File");
      int returnValue = jfc.showOpenDialog(null);
      if (returnValue == JFileChooser.APPROVE_OPTION) {
         File selectedFile = jfc.getSelectedFile();
         fileName = selectedFile.getAbsolutePath();
      } // close if
      return fileName;
   } // close chooseFile
   
} // close FlagParser
