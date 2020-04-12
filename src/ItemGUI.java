import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Panel;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;

// class for a GUI to siplay search results
public class ItemGUI extends JFrame{

   FlagParser fp; // command line flag parser
   String item; // searched item
   String searchType; // is the search offline, online, or time frame 
   String inputTime; // the time put in for a time frame search
   HashMap<String, Product> inventory; // inventory hashmap
   Container contentPane; // GUI content pane
   Panel panel; // GUI panel
   HashMap<String, Product> itemInventory; // specific item inventory
   SearchGUI sg; // main inventory GUI
   String printout; // string of all the results for this item
   QueryMenuHandler qmh; // query menu handler
   
   // constructor
   public ItemGUI(FlagParser flagParser, HashMap<String, Product> inv, String searchItem, String offOrOn, SearchGUI searchGUI, String time) throws IOException, ParseException {
      super("GUI");
      item = searchItem;
      fp = flagParser;
      sg = searchGUI;
      inventory = inv;
      searchType = offOrOn;
      inputTime = time;
      itemInventory = new HashMap<String, Product>();
      setSize(700,400);
      setLocation(300,200);
      setTitle("Results for " + item);
      contentPane = getContentPane();
      panel = new Panel();
      contentPane.add(panel);
      createMenu();
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      setItemInventory();
      } // close constructor
   
   // method to create a menu for the GUI
   public void createMenu() {
      JMenuBar menuBar  = new JMenuBar();
      JMenuItem email = new JMenuItem("Email Results");
      JMenuItem save = new JMenuItem("Save Results");
      qmh = new QueryMenuHandler();
      email.addActionListener(qmh);
      save.addActionListener(qmh);
      menuBar.add(email);
      menuBar.add(save);
      setJMenuBar(menuBar);
   } // close CreateMenu
   
   // method to add searched items to the query results
   public void setItemInventory() throws ParseException {
      // add time query here
      for (Map.Entry<String, Product> entry : inventory.entrySet()) {
         Product value = entry.getValue();
         if (value.itemSearched.equals(item) && inQuery(value)) {
            itemInventory.put(value.id, value);
            addToPrintOut(value);
         } // close if
      } // close for loop
      if (itemInventory.size() != 0) {
         displayInventory();
         qmh.setResults(printout);
         setVisible(true);
      } // close if
      else if (itemInventory.size() == 0 && !searchType.equals("online")) {
         sg.displayEmptySearch(item);
      } // close else if
   } // close setItemInventory
   
   // method to check if an item belongs in the results
   public boolean inQuery(Product product) throws ParseException {
      boolean qualifies = true;
      if (searchType.equals("time")) {
         qualifies = false;
         qualifies = timeCheck(product);
      } // close if
      return qualifies;
   } // close inQuery
   
   // method to see if the time matches with the search time frame
   public boolean timeCheck(Product product) throws ParseException {
      Date productTimestamp = new SimpleDateFormat("YYYYMMDD_HHMMSS").parse(product.timestamp);
      Date inputTimestamp = new SimpleDateFormat("YYYY.MM.DD-HH.MM.SS").parse(inputTime);
      if (productTimestamp.compareTo(inputTimestamp) >= 0) {
         return true;
      } // close if
      return false;
   } // close timeCheck
   
   // method to display the results
   public void displayInventory() {
      panel.removeAll();
      JTable myTable = createTable();
      JScrollPane scroll = new JScrollPane(myTable);
      scroll.setVisible(true);
      myTable.setFillsViewportHeight(true);
      panel.setLayout(new BorderLayout());
      panel.add(myTable.getTableHeader(), BorderLayout.PAGE_START);
      panel.add(scroll);
      setVisible(true);
   } // close displayInventory
   
   // method to add results to a string
   public void addToPrintOut(Product value) {
      if (value != null) {
         printout += value.name + "|" + value.id + "|" + value.itemSearched + "|" + value.condition + "|" + value.biddingPrice + "|" + value.bids + "|" + value.buyNowPrice + "|" + value.timeLeft + "|" + value.imageURL + "|" + value.timestamp + "|" + value.username +  "\n";
      } // close if
   } // close addToPrintOut
      
   // method to create the table of results
   public JTable createTable() {
      String[] columnNames = {"Name", "ID", "Item Searched", "Condition", "Bidding Price", "Bids", "Buy Now Price", "Time Left", "Timestamp", "Username"};
      Object[][] data = new Object[itemInventory.size()][10];
      int i = 0;
      for (Map.Entry<String, Product> entry : itemInventory.entrySet()) {
         Product value = entry.getValue();
         data[i][0]= value.name;
         data[i][1] = value.id;
         data[i][2] = value.itemSearched;
         data[i][3] = value.condition;  
         data[i][4] = value.biddingPrice;
         data[i][5] = value.bids;
         data[i][6] = value.buyNowPrice;
         data[i][7] = value.timeLeft;
         data[i][8] = value.timestamp;
         data[i][9] = value.username;
         i++;
     } // close for loop
      JTable table = new JTable(data, columnNames);
      return table;
   } // close createTable
      
} // close ItemGUI
