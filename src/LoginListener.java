import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JTextField;

// class to perform actions after login is completed
public class LoginListener implements ActionListener {
   
   HashMap<String, Product> inventory;
   FlagParser fp; // command line flag parser
   JTextField usernameBox; // input box for username
   JTextField passwordBox; // input box for password
   String username; // username
   String password; // password
   Login login; // login GUI
   ArrayList<String> initialSearches = new ArrayList<String>(); // list of items to search on startup
   
   // constructor
   public LoginListener(Login loginBox, JTextField user, JTextField pass, HashMap<String, Product> inv, FlagParser flagParser) {
      inventory = inv;
      fp = flagParser;
      usernameBox = user;
      passwordBox = pass;
      login = loginBox;
   } // close constructor

   // method for action to perform upon login
   @Override
   public void actionPerformed(ActionEvent e) {
      authenticate();
      try {
         fillInventory(inventory, fp.outputFile, username);
      } catch (IOException e1) {
         e1.printStackTrace();
      } // close try catch
      try {
         createSearchGUI();
      } catch (IOException e1) {
         e1.printStackTrace();
      } catch (ParseException e1) {
         e1.printStackTrace();
      } // close try catch
   } // close actionPerformed
   
   // method to either start up the GUIS or proceed without GUIs
   public void createSearchGUI() throws IOException, ParseException {
      if (fp.openGUI == true) {
         SearchGUI gui = new SearchGUI(fp, inventory, login, initialSearches);
      } // close if
      else {
         NoGUI ng = new NoGUI(fp, inventory, login, initialSearches);
         System.exit(0);
      } // close else
   } // close createSearchGUI
   
   // method to authenticate user
   public void authenticate() {
      username = usernameBox.getText();
      password = passwordBox.getText();
      login.username = username;
      login.password = password;
      login.frame.setVisible(false);
   } // close authenticate
   
   // method to fill the inital search array with items from the input file
   public void fillInventory(HashMap<String, Product> inventory, String cache, String username) throws IOException{
      inputFileSearch();
   } // close fillInventory
   
   // method to read through input file for initial searches
   public void inputFileSearch() throws NumberFormatException, MalformedURLException, IOException {
      BufferedReader reader = new BufferedReader(new FileReader(fp.inputFile));
      String line;
      while ((line = reader.readLine()) != null)   {
         initialSearches.add(line.trim());
      } // close while
      reader.close();
   } // close inputFileSearch
   
   // method to add items from the ouput file
   public void outputFileSearch(String cache) throws NumberFormatException, MalformedURLException, IOException {
      BufferedReader reader = new BufferedReader(new FileReader(cache));
      String line;
      while ((line = reader.readLine()) != null)   {
         addLineItem(inventory, line);
      } // close while
      reader.close();  
   } // close outputFileSearch

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

} // close LoginListener
