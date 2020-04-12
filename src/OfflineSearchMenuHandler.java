import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

// class to handle offline searches
public class OfflineSearchMenuHandler extends JFrame implements ActionListener{

   SearchGUI sg; // main inventory GUI
   HashMap<String, Product> inventory; // main inventory hashmap
 
   // constructor
   public OfflineSearchMenuHandler(SearchGUI jf, HashMap<String, Product> inv) {
    sg = jf;   
    inventory = inv;
   } // close constructor

   // method to perform actions based on the search
   @Override
   public void actionPerformed(ActionEvent event) {
      String menuName = event.getActionCommand();
      if (menuName.equals("Item Query")) {
         try {
            searchOffline();
         } catch (IOException e) {
            e.printStackTrace();
         } catch (ParseException e) {
            e.printStackTrace();
         } // close try catch
      }  // close if
      if (menuName.equals("Time Frame Query")) {
         try {
            timeSearch();
         } catch (IOException e) {
            e.printStackTrace();
         } catch (ParseException e) {
            e.printStackTrace();
         } // close try catch
      }  // close if
   } // close actionPerformed
     
   // method to perform and offline search
   public void searchOffline() throws IOException, ParseException {
      String item = JOptionPane.showInputDialog("Enter an item to search for");
      ItemGUI ig = new ItemGUI(sg.fp, inventory, item, "offline", sg, ""); 
   } // close searchOffline
   
   // method to perform an offline search with a timeframe
   private void timeSearch() throws IOException, ParseException {
      String item = JOptionPane.showInputDialog("Enter an item to search for");
      String time = JOptionPane.showInputDialog("Enter the time to search from in the format YYYY.MM.DD-HH.MM.SS");
      ItemGUI ig = new ItemGUI(sg.fp, inventory, item, "time", sg, time);     
   } // close timeSearch
   
} // close OfflineSearchMenuHandler
