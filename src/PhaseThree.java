
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

// main class 
public class PhaseThree {
   
   static HashMap<String, Product> inventory; // main inventory hashmap
   static FlagParser fp; // command line flag parser
   static Login login; // login GUI
   
   // main method
   public static void main(String args[]) throws IOException {
      inventory = new HashMap<String, Product>();
      fp = new FlagParser(args);
      login = new Login(inventory, fp);
      //System.out.println(Collections.singletonList(inventory));
   } // close main
   
} // close PhaseThree
