import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.swing.JFrame;

// class to handle reconstructing the inventory
public class ReconstructHandler extends JFrame implements ActionListener{

   SearchGUI sg; // main inventory GUI
   FlagParser fp; // command line flag parser
   
   // constructor
   public ReconstructHandler(SearchGUI gui, FlagParser flagParser) {
      sg = gui;
      fp = flagParser;
   } // close constructor
   
   // method to perform reconstruction
   @Override
   public void actionPerformed(ActionEvent e) {
      try {
         addFromCache();
      } catch (NumberFormatException | IOException e1) {
         e1.printStackTrace();
      } // close try catch
      sg.displayInventory();
      try {
         sg.addInventoryToOutput();
      } catch (FileNotFoundException e1) {
         e1.printStackTrace();
      } catch (UnsupportedEncodingException e1) {
         e1.printStackTrace();
      } // close try catch
   } // close actionPerformed
   
   // method to add items from the cache to the inventory
   public void addFromCache() throws NumberFormatException, MalformedURLException, IOException {
      BufferedReader reader = new BufferedReader(new FileReader(fp.cache));
      String line;
      while ((line = reader.readLine()) != null)   {
         addLineItem(sg.inventory, line);
      } // close while
      reader.close();  
   } // close addFromCache

   // method takes in a line from the parsed input file and adds it to the inventory
   public void addLineItem(HashMap<String, Product> inventory, String line) throws NumberFormatException, MalformedURLException, IOException {
      String[] lineArray = line.split("\\|");
      if (lineArray.length == 11) {
         Product item = new Product(lineArray[0], lineArray[2], lineArray[3], lineArray[4], Integer.parseInt(lineArray[5]), lineArray[6], lineArray[7], new URL(lineArray[8]), lineArray[1], lineArray[9], lineArray[10]);
         if (!inventory.containsKey(item.id)){
            inventory.put(item.id, item);
         } // close if
      } // close if
   } // close addLineItem

} // close RecontructHandler
