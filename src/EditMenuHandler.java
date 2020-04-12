import javax.swing.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.time.Instant;
import java.util.Collections;

// class to handle GUI menu buttons
@SuppressWarnings("serial")
public class EditMenuHandler extends JFrame implements ActionListener {
   
   SearchGUI sg; // main search GUI
   FlagParser fp; // command line flag parser
   
   // constructor
   public EditMenuHandler(SearchGUI jf, FlagParser flagParser){
    sg = jf;   
    fp = flagParser;
   } // close constructor
   
   // method to give buttons actions
   public void actionPerformed(ActionEvent event) {
      String menuName = event.getActionCommand();
      if (menuName.equals("Insert")) {
         try {
            insertItem();
         } catch (IOException e) {
            e.printStackTrace();
         } // close try catch
      } // close if 
      if (menuName.equals("Delete")) {
         try {
            deleteItem();
         } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
         } catch (IOException e) {
            e.printStackTrace();
         } // close try catch
      } // close if
      if (menuName.equals("Modify")) {
         try {
            modifyItem();
         } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
         } catch (IOException e) {
            e.printStackTrace();
         } // close try catch
      } // close if 
   } // close actionPerformed
   
   // method for insert button
   private void insertItem() throws IOException {
      if (!sg.login.username.equals("admin")) {
         errorMessage();
         return;
      }
      String id = JOptionPane.showInputDialog("Enter the new item's ID");
      String item = JOptionPane.showInputDialog("Enter a new item's specific name");
      String itemSearched = JOptionPane.showInputDialog("What category is " + item + "?");
      String condition = JOptionPane.showInputDialog("What condition is " + item  + " in?");
      String biddingPrice = JOptionPane.showInputDialog("What is " + item  + " 's bidding price?");
      int bids = Integer.parseInt(JOptionPane.showInputDialog("How many bids have there been for " + item  + "?"));
      String buyNowPrice = JOptionPane.showInputDialog("What is " + item  + " 's buy now price?");
      String timeLeft = JOptionPane.showInputDialog("How much time is left to bid on " + item  + "?");
      String imageURL = JOptionPane.showInputDialog("What is the URL for the image of " + item  + "?");
      URL image = new URL(imageURL);
      String timestamp = Instant.now().toString();
      String username = sg.login.username;
      Product newItem = new Product(item, itemSearched, condition, biddingPrice, bids, buyNowPrice, timeLeft, image, id, timestamp, username);
      sg.inventory.put(item, newItem);
      addToOutput(newItem);
      sg.displayInventory(); 
      addToLog("insert", newItem, null, null);
   } // close insertItem
   
   // method for delete button
   public void deleteItem() throws IOException {
      String id = JOptionPane.showInputDialog("Enter an Item's id");
      addToLog("delete",sg.inventory.get(id), null, null);
      sg.inventory.remove(id);
      sg.displayInventory();
      sg.addInventoryToOutput();
   } // close deleteItem
   
   // method for modifyItem
   public void modifyItem() throws IOException {
      String id = JOptionPane.showInputDialog("Enter the id of the item you would like to modify");
      if (!authorized(id)) {
         errorMessage();
         return;
      } // close if
      String type = JOptionPane.showInputDialog("Are you modifying 'condition', 'bidding price', 'bids', 'buy now price', or 'time left'?");
      String value = JOptionPane.showInputDialog("What is the new " + type + " of the item with ID " + id);
      addToLog("modify", sg.inventory.get(id), type, value);
      changeValue(type, value, id);
      sg.displayInventory();
      sg.addInventoryToOutput();
   } // close modifyItem
   
   // method to check if the user is authorized to perform an action
   public boolean authorized(String id) {
      if (sg.login.username.equals("admin")) {
         return true;
      } // close if
      String currentUser = sg.login.username;
      String itemUser = sg.inventory.get(id).username;
      if (currentUser.equals(itemUser)) {
         return true;
      } // close if
      return false;   
   } // close authorized
   
   // method to display message to tell user they are unauthorized
   public void errorMessage() {
      JOptionPane.showMessageDialog(new JFrame(), "You are not authorized to perform that action.");
   } // close errorMessage
   
   // method to change a value in the inventory
   public void changeValue(String type, String value, String id) {
      if (type.equals("condition")) {
         sg.inventory.get(id).condition = value;
      } // close if
      if (type.equals("bidding price")) {
         sg.inventory.get(id).biddingPrice = value;
      } // close if  
      if (type.equals("bids")) {
         sg.inventory.get(id).bids = Integer.parseInt(value);
      } // close if
      if (type.equals("buy now price")) {
         sg.inventory.get(id).buyNowPrice = value;
      } // close if  
      if (type.equals("time left")) {
         sg.inventory.get(id).timeLeft = value;
      } // close if  
   }  // close changeValue
   
   // method to add actions to the logging file
   public void addToLog(String type, Product item, String modification, String value) throws IOException {
      FileWriter fw = new FileWriter(fp.loggingFile,true);
      fw.write(type.toUpperCase() + "\n");
      fw.write( "Item:" +  item.name + "|" + item.id + "|" + item.itemSearched + "|" + item.condition + "|" + item.biddingPrice + "|" + item.bids + "|" + item.buyNowPrice + "|" + item.timeLeft + "|" + item.imageURL + "|" + item.timestamp + "|" + item.username + "\n");
      if (type.equals("modify")) {
         logModify(fw, modification, value);
      } // close if
      fw.write("Transaction occured at: " + Instant.now() +"\n\n");
      fw.close();
   } // close addToLog
   
   public void logModify(FileWriter fw, String modification, String value) throws IOException {
      fw.write("New " + modification  + ": " + value +"\n");
   } // close logModify
   
   // method to add a new item to the output file
   public void addToOutput(Product item) throws IOException {
      FileWriter fw = new FileWriter(fp.outputFile, true);
      fw.write(item.name + "|" + item.id + "|" + item.itemSearched + "|" + item.condition + "|" + item.biddingPrice + "|" + item.bids + "|" + item.buyNowPrice + "|" + item.timeLeft + "|" + item.imageURL + "|" + item.timestamp + "|" + item.username + "\n");
      fw.close();
   } // close addToOutput
      
} // close EditMenuHandler