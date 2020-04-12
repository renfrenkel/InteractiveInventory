import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;

import javax.swing.JRadioButton;
import javax.swing.JTextField;

// class to handle the advanced search button
public class AdvancedButtonHandler implements ActionListener{

   SearchGUI sg; // Main inventory GUI
   AdvancedSearchGUI asg; // GUI to input advanced search options
   JRadioButton buyNowButton; // advanced search option for Buy Now
   JRadioButton auctionStyleButton; // advanced search option for auction style
   JRadioButton reservePriceButton; // advanced search option for reserve price
   JRadioButton bestOfferButton; // advanced search option for best offer
   JRadioButton directPaymentButton; // advanced search option for direct payment
   JRadioButton freeShippingButton; // advanced search option for free shipping
   JRadioButton localPickupButton; // advanced search option for local pickup
   JTextField inputItemBox; // input box for item searched for in advanced search
   boolean buyNow; // is the buy now option selected
   boolean auctionStyle ; // is the auction style option selected
   boolean reservePrice; // is the reserve price option selected
   boolean bestOffer; // is the best offer option selected
   boolean directPayment ; // is the direct payment option selected
   boolean freeShipping; // is the free shipping option selected
   boolean localPickup; // is the local pickup option selected
   String inputItem; // the item searched for
   
   // constructor
   public AdvancedButtonHandler (JRadioButton bn, JRadioButton as, JRadioButton rp, JRadioButton bo, JRadioButton dp, JRadioButton fs, JRadioButton lp, JTextField ii, SearchGUI gui, AdvancedSearchGUI agui) {
      sg = gui;
      asg = agui;
      buyNowButton = bn;
      auctionStyleButton = as; 
      reservePriceButton = rp;
      bestOfferButton = bo;
      directPaymentButton = dp;
      freeShippingButton = fs;
      localPickupButton = lp;
      inputItemBox = ii;
   } // close constructor
   
   // method to perform an action when the search button is clicked
   @Override
   public void actionPerformed(ActionEvent event) {
      String buttonName = event.getActionCommand();
      if (buttonName.equals("Search")) {
         try {
            advancedSearch();
            asg.dispose();
         } catch (IOException | ParseException e) {
            e.printStackTrace();
         } // close try catch
      } // close if
   } // close actionPerformed
   
   // method to start the advanced search
   public void advancedSearch() throws IOException, ParseException {
      setButtons();
      AdvancedSearch as = new AdvancedSearch(inputItem, sg.inventory, sg, this);
   } // close advancedSearch
   
   // method to set which radio buttons are selected
   public void setButtons() {
      buyNow = buyNowButton.isSelected();
      auctionStyle = auctionStyleButton.isSelected();
      reservePrice = reservePriceButton.isSelected();
      bestOffer = bestOfferButton.isSelected();
      directPayment = directPaymentButton.isSelected();
      freeShipping = freeShippingButton.isSelected();
      localPickup = localPickupButton.isSelected();
      inputItem = inputItemBox.getText();
   } // close setButtons
   
} // close AdvancedButtonHandler
