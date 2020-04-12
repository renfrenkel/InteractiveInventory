import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Panel;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

// class for a GUI to display advanced search options
public class AdvancedSearchGUI extends JFrame{
   
   HashMap<String, Product> inventory; // inventory hashmap
   Container contentPane; // GUI content pane
   Panel panel; // GUI panel
   FlagParser fp; // command line flag parser
   Login login; // login GUI
   JTextField itemInput= new JTextField(20); // search item input box
   SearchGUI sg; // main inventory GUI
   
   // constructor
   public AdvancedSearchGUI (FlagParser flagParser, HashMap<String, Product> inv, Login log, SearchGUI gui) {
      super("GUI");
      sg = gui;
      fp = flagParser;
      inventory = inv;
      login = log;
      setSize(600,200);
      setLocation(350,250);
      setTitle("Advanced Search");
      contentPane = getContentPane();
      panel = new Panel();
      contentPane.add(panel);
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      addButtons(); 
      setVisible(true);
   } // close constructor
   
   // method to add buttons to the GUI
   public void addButtons() {
      JRadioButton buyNow = new JRadioButton("Items with Buy Now enabled");
      panel.add(buyNow);
      JRadioButton auctionStyle = new JRadioButton("Include Auction-Style items");
      panel.add(auctionStyle);
      JRadioButton reservePrice = new JRadioButton("Auction Items with No Reserve Price only");
      panel.add(reservePrice);
      JRadioButton bestOffer = new JRadioButton("Items with the Best Offer option");
      panel.add(bestOffer);
      JRadioButton directPayment = new JRadioButton("Items for which you can use Direct Payment (ie. Paypal)");
      panel.add(directPayment);
      JRadioButton freeShipping = new JRadioButton("Items with Free Shipping only");
      panel.add(freeShipping);
      JRadioButton localPickup = new JRadioButton("Items with Local Pickup Allowed only");
      panel.add(localPickup);
      panel.add(itemInput);
      JButton search = new JButton("Search");
      AdvancedButtonHandler abh = new AdvancedButtonHandler(buyNow, auctionStyle, reservePrice, bestOffer, directPayment, freeShipping, localPickup, itemInput, sg, this);
      search.addActionListener(abh);
      panel.add(search);
   } // close addButtons
   
} // close AdvancedSearchGUI
