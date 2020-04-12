import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

// class to handle the search menu
@SuppressWarnings("serial")
public class SearchMenuHandler extends JFrame implements ActionListener {
   
   SearchGUI sg;
   HashMap<String, Product> inventory; // main inventory hashmap
 
   // constructor
   public SearchMenuHandler(SearchGUI jf, HashMap<String, Product> inv) {
    sg = jf;   
    inventory = inv;
   } // close constructor
   
   // method to perform actions for online searches
   public void actionPerformed(ActionEvent event) {
      String menuName = event.getActionCommand();
      if (menuName.equals("Online Search")) {
         try {
            searchOnline();
         } catch (MalformedURLException e) {
            e.printStackTrace();
         } catch (IOException e) {
            e.printStackTrace();
         } catch (ParseException e) {
            e.printStackTrace();
         } // close try catch
      } // close if
      if (menuName.equals("Advanced Search")) {
         AdvancedSearchGUI as = new AdvancedSearchGUI(sg.fp, inventory, sg.login, sg);
      } // close if
   } // close actionPerformed
   
   // method to perform an online search
   public void searchOnline() throws IOException, ParseException {
      String item = JOptionPane.showInputDialog("Enter a new Item");
      OnlineSearch search = new OnlineSearch(item, inventory, sg);
      sg.addInventoryToOutput();
   } // close searchOnline
   
}