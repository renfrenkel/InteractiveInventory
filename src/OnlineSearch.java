import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// class to conduct online searches
public class OnlineSearch {

   final String WEBSTORE_URL; // webstore URL as a string
   String item; // item searched
   URL url; // webstore URL with search parameters
   URLConnection uc;  // websotre URL connection
   SearchGUI sg; // main inventory GUI
   HashMap<String, Product> inventory; // main inventory hashmap

   // constructor
   public OnlineSearch(String searchedItem, HashMap<String, Product> inv, SearchGUI searchGui) throws IOException, ParseException {
      WEBSTORE_URL = "https://www.webstore.com//search.php?option=basic_search&basic_search=";
      inventory = inv;
      sg = searchGui;
      //USER_AGENT = getUserAgent();
      item = searchedItem;
      url = new URL(WEBSTORE_URL + item);
      createConnection();
      parseFile();
      ItemGUI ig = new ItemGUI(sg.fp, inventory, item, "online", sg, "");
      removeTempFile();
   } // close constructor

   // method to open url connections, and extract url information
   public void createConnection() throws IOException {
      uc = url.openConnection();
      saveLocally();
   } // close createConnection

   // method to save a file locally
   public void saveLocally() throws IOException {
      InputStream in = uc.getInputStream();
      Path outputFile = Paths.get(item + ".html");
      if (!new File(outputFile.toString()).exists()) {
         Files.copy(in, outputFile);
      } // close if
   } // close saveLocally
   
   public void removeTempFile() throws IOException {
      Path file = Paths.get(item + ".html");
      Files.delete(file);
   }

   // method to parse the HTML file
   public void parseFile() throws IOException {
      String file = readFile();
      if (!file.contains("search.php?search_empty")) {
         getItem(file);
      } // close if
      else {
         sg.displayEmptySearch(item);
      } // close else
   } // close ParseFile

   // method to read the HTML file into a strin
   public String readFile() throws IOException {
      StringBuilder sb = new StringBuilder();
      String string;
      BufferedReader in = new BufferedReader(new FileReader(item + ".html"));
      while((string = in.readLine())!=null) {
         sb.append(string);
      } // close while
      in.close();
      String content = sb.toString();
      return content;
   } // close readFile

   // method to get an item from the HTML file
   public void getItem(String file) throws IOException {
      String itemDescription = "";
      String regex = "contentfont(\\s+.*?)</tr>";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(file);
      while (matcher.find()) {
         itemDescription = matcher.group();
         getProductDetails(itemDescription);
      } // close while  
   } // close getItem

   // method to get item details
   public void getProductDetails(String itemDescription) throws IOException {
      String name = getProductName(itemDescription);
      String condition = getCondition(itemDescription);
      String biddingPrice = getBiddingPrice(itemDescription);
      int bids = getBids(itemDescription);
      String buyNowPrice = getBuyNowPrice(itemDescription);
      String timeLeft = getTimeLeft(itemDescription);
      URL image = getImage(itemDescription);
      String id = getId(itemDescription);
      String username = sg.login.username;
      String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()).toString();
      Product product = new Product(name, item, condition, biddingPrice, bids, buyNowPrice, timeLeft, image, id, timestamp, username);
      inventory.put(product.id, product);
      sg.displayInventory();
   } // close getProductDetails

   // method to get the item's name
   public String getProductName(String itemDescription) {
      String regex = "item/(.*?)/";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(itemDescription);
      matcher.find();
      String name = matcher.group();
      name = name.substring(5, name.length()-1);
      name = name.replace('-', ' ');
      return name;
   } // close getProductName

   // method to get the condition of an item
   public String getCondition(String itemDescription) {
      String condition = "";
      String regex = "condition:(\\s+.*?)<";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(itemDescription);
      if (matcher.find()) {
         condition = matcher.group();
         condition = condition.substring(11, condition.length()-1);
      } // close if
      return condition;
   } // close getCondition

   // method to get the bidding price of an item
   public String getBiddingPrice(String itemDescription) {
      String price = "";
      String regex = "\\$(.*?)<";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(itemDescription);
      if (matcher.find()) {
         price = matcher.group();
         price = price.substring(0, price.length()-1);
      } // close if
      return price;
   } // close getBiddingPrice

   // method to get the amount of bids on an item
   public int getBids(String itemDescription) {
      String bids = "0";
      String regex = "[0-9]+\\s+bids";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(itemDescription);
      if (matcher.find()) {
         bids = matcher.group();
         bids = bids.substring(0, bids.length()-5);
      } // close if
      int bidCount = Integer.parseInt(bids);
      return bidCount;
   } // close getBids

   // method to get the buy now price of an item
   public String getBuyNowPrice(String itemDescription) {
      String price = "";
      String regex = "BUY\\sNOW(.*?)<";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(itemDescription);
      if (matcher.find()) {
         price = matcher.group();
         int startIndex = price.indexOf('$');
         price = price.substring(startIndex, price.length()-1);
      } // close if
      return price;
   } // close getBuyNowPrice

   // method to get the time left on the auction of an item
   public String getTimeLeft(String itemDescription) {
      String timeLeft = "";
      String regex = "br_ends(.*?)left\\.";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(itemDescription);
      if (matcher.find()) {
         timeLeft = matcher.group();
         timeLeft = timeLeft.substring(9, timeLeft.length()-5);
      } // close if
      return timeLeft;
   } // close getTimeLeft

   // method to get the url of an item's image
   public URL getImage(String itemDescription) throws MalformedURLException {
      String image = "";
      String regex = "br_image(.*?)border=";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(itemDescription);
      if (matcher.find()) {
         image = matcher.group();
         image = image.substring(15, image.length()-9);
      } // close if
      URL imageURL = new URL(image);
      return imageURL;
   } // close getImage

   // close getId
   public String getId(String itemDescription) {
      String id = "";
      String regex = "item/(.*?)<";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(itemDescription);
      if (matcher.find()) {
         id = matcher.group();
         int startIndex = id.lastIndexOf('/');
         id = id.substring(startIndex+1, id.length()-3);
      } // close if
      return id;
   } // close getId


} // close OnlineSearch
