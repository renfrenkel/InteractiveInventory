import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

// class to make products and object
public class Product {

   String name = ""; // item's name
   String itemSearched; // searched item
   String condition = ""; // item's condition
   String biddingPrice; // item's bidding price
   int bids; // amount of bids on an item
   String buyNowPrice; // item's buy now price
   String timeLeft; // time left on a bid for an item
   String imageURL; // item's image URL
   byte[] image; // item's image
   String id; // item's id
   String username; // username
   String timestamp; // time of item search
   
   boolean reservePrice = false; // if item has a reserve price
   boolean bestOffer = false; // if item has a best offer
   boolean directPayment = false; // if item has direct payment
   boolean freeShipping = false; // if item has free shipping
   boolean localPickup = false; // if item has local pickup
   
   // constructor
   public Product(String itemName, String search, String itemCondition, String itemBiddingPrice, int itemBids, String buyNow, String itemTimeLeft, URL itemImage, String ID, String time, String user) throws IOException {
      name = itemName;
      itemSearched = search;
      condition = itemCondition;
      biddingPrice = itemBiddingPrice;
      bids = itemBids;
      buyNowPrice = buyNow;
      timeLeft = itemTimeLeft;
      imageURL = itemImage.toString() ;
      id = ID;
      timestamp = time;
      setImage(itemImage);
      username = user;
      checkIfAuction();
   } // close constructor

   // method to gt an image from the image URL
   public void setImage(URL imageURL) throws IOException {
      ByteArrayOutputStream output = new ByteArrayOutputStream();   
      InputStream inputStream = imageURL.openStream();
      int n = 0;
      byte [] buffer = new byte[1024];
      while (-1 != (n = inputStream.read(buffer))) {
         output.write(buffer, 0, n);
      } // close while
      image = output.toByteArray();
   } // close setImage
   
   // method to check if the item is part of an auction
   public void checkIfAuction() {
      if (timeLeft.equals("")) {
         buyNowPrice = biddingPrice;
         biddingPrice = "N/A";
         timeLeft = "N/A";
      } // close if
      if (buyNowPrice.equals("")) {
         buyNowPrice = "N/A";
      } // close if
   } // close checkIfAuction
         
} // close Product
