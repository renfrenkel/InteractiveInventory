import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.List;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

// class for the main inventory GUI
public class SearchGUI extends JFrame{

   HashMap<String, Product> inventory; // main inventory hashmap
   Container contentPane; // GUI content pane
   Panel panel; // GUI panel
   FlagParser fp; // command line flag parser
   Login login; // login GUI
   ArrayList initialSearches; // list of item's to search on startup
   
   // constructor for GUI to display inventory
   public SearchGUI(FlagParser flagParser, HashMap<String, Product> inv, Login log, ArrayList searches) throws IOException, ParseException {
      super("GUI");
      fp = flagParser;
      inventory = inv;
      login = log;
      initialSearches = searches;
      setSize(1200,400);
      setLocation(100,100);
      setTitle("Inventory");
      contentPane = getContentPane();
      panel = new Panel();
      contentPane.add(panel);
      setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      saveToCache();
      createMenu();
      conductInitialSearches(); 
      setVisible(true);
      displayInventory();
      addInventoryToOutput();
      } // close constructor
   
   // method to create a menu for the gui
   private void createMenu() throws MalformedURLException { 
      JMenuBar menuBar  = new JMenuBar();
      JMenu editMenu = new JMenu("Edit");
      EditMenuHandler emh  = new EditMenuHandler(this, fp);
      addToEditMenu("Insert", emh, editMenu);
      addToEditMenu("Modify", emh, editMenu);
      addToEditMenu("Delete", emh, editMenu);
      setJMenuBar(menuBar);
      menuBar.add(editMenu);
      JMenu searchMenu = new JMenu("Search");
      SearchMenuHandler smh = new SearchMenuHandler(this, inventory);
      addToSearchMenu("Online Search", smh, searchMenu);
      addToSearchMenu("Advanced Search", smh, searchMenu);
      menuBar.add(searchMenu); 
      OfflineSearchMenuHandler omh = new OfflineSearchMenuHandler(this, inventory);
      JMenu offlineMenu = new JMenu("Offline Search"); 
      addToOfflineSearchMenu("Item Query", omh, offlineMenu);
      addToOfflineSearchMenu("Time Frame Query", omh, offlineMenu);
      searchMenu.add(offlineMenu);
      JMenuItem reconstruct = createReconstructButton();    
      menuBar.add(reconstruct);      
   } // close createMenu
    
   // method to conduct searches on items from the input file
   public void conductInitialSearches() throws IOException, ParseException {
      for (int i=0; i < initialSearches.size(); i++) {
         new OnlineSearch(initialSearches.get(i).toString(), inventory, this);
      } // close for loop
   } // close conductInitialSearches
   
   // method to create a button for reconstruction
   public JMenuItem createReconstructButton() {
      ReconstructHandler rh = new ReconstructHandler(this, fp);
      JMenuItem reconstruct = new JMenuItem("Reconstruct Inventory");    
      reconstruct.addActionListener(rh);
      return reconstruct;
   } // close createReconstructButton
   
   // method to add buttons to the menu
   public void addToEditMenu(String button, EditMenuHandler emh, JMenu editMenu) {
      JMenuItem item;
      item = new JMenuItem(button);    
      item.addActionListener(emh);
      editMenu.add(item);
   } // close addToMenu
    
   // method to add buttons to the menu
   public void addToSearchMenu(String button, SearchMenuHandler smh, JMenu editMenu) {
      JMenuItem item;
      item = new JMenuItem(button);    
      item.addActionListener(smh);
      editMenu.add(item);
   } // close addToMenu
   
   // method to add buttons to the menu
   public void addToOfflineSearchMenu(String button, OfflineSearchMenuHandler omh, JMenu editMenu) {
      JMenuItem item;
      item = new JMenuItem(button);    
      item.addActionListener(omh);
      editMenu.add(item);
   } // close addToMenu
   
   // method to display the inventory
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
   
   // method to creat a table with the inventory
   public JTable createTable() {
      String[] columnNames = {"Name", "Image", "ID", "Item Searched", "Condition", "Bidding Price", "Bids", "Buy Now Price", "Time Left", "Timestamp", "Username"};
      Object[][] data = new Object[inventory.size()][11];
      int i = 0;
      for (Map.Entry<String, Product> entry : inventory.entrySet()) {
         Product value = entry.getValue();
         data[i][0]= value.name;
         data[i][1]= value.image;
         data[i][2]= value.id;
         data[i][3] = value.itemSearched;
         data[i][4] = value.condition;  
         data[i][5] = value.biddingPrice;
         data[i][6] = value.bids;
         data[i][7] = value.buyNowPrice;
         data[i][8] = value.timeLeft;
         data[i][9] = value.timestamp;
         data[i][10] = value.username;
         i++;
      } // close for loop
      JTable table = new JTable(data, columnNames);
      table.getColumnModel().getColumn(1).setCellRenderer(new ImageRenderer());
      table.setRowHeight(100);
      return table;
   } // close createTable
   
   // method to display a message when there are no results
   public void displayEmptySearch(String item) {
      JOptionPane.showMessageDialog(null, "Sorry, there are no matches for " + item + ".");
   } // close displayEmptySearch
   
   // method to add the inventory to the output file
   public void addInventoryToOutput() throws FileNotFoundException, UnsupportedEncodingException {
      PrintWriter writer = new PrintWriter(fp.outputFile, "UTF-8");
      for (Map.Entry<String, Product> entry : inventory.entrySet()) {
         Product value = entry.getValue();
         writer.println(value.name + "|" + entry.getKey() + "|" + value.itemSearched + "|" + value.condition + "|" + value.biddingPrice + "|" + value.bids + "|" + value.buyNowPrice + "|" + value.timeLeft + "|" + value.imageURL + "|" + value.timestamp + "|" + value.username);
      }  // close for loop
      writer.close();
   } // close addInventoryToOutput
   
   // method to save the output to the cache when the GUI is closed
   public void saveToCache() {
      addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            dispose();
            try {
               copyOutputFile();
            } catch (FileNotFoundException e1) {
               e1.printStackTrace();
            } catch (IOException e1) {
               e1.printStackTrace();
            } // close try catch 
              System.exit(0);
          } // close windowClosing
      }); // close addWindowlistener
   } // close saveToCache
   
   // method to copy the output file to the cache
   public void copyOutputFile() throws FileNotFoundException, IOException {
      File output = new File(fp.outputFile);
      if (output.length() == 0) {
         return;
      }
      Files.copy(Paths.get(fp.outputFile), new FileOutputStream(fp.cache));
   } // close copyOutputFile
        
} // close SearchGUI
