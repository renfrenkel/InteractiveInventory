import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;

// class for when the GUIs shouldn't be started 
public class NoGUI {
   
   FlagParser fp; // command line flag parser
   HashMap<String, Product> inventory; // main inventory hashmap
   Login login; // login GUI
   ArrayList initialSearches; // list of items to search on startup
   final String WEBSTORE_URL = "https://www.webstore.com//search.php?option=basic_search&basic_search="; // webstore URL
   String results; // string of the item's results

   // constructor
   public NoGUI(FlagParser flagParser, HashMap<String, Product> inv, Login log, ArrayList searches) throws IOException, ParseException {
      fp = flagParser;
      inventory = inv;
      login = log;
      initialSearches = searches;
      conductInitialSearches(); 
      addToOutputAndCache();
      email();
   } // close constructor
   
   // method to conduct searches on items from the input file
   public void conductInitialSearches() throws IOException, ParseException {
      for (int i=0; i < initialSearches.size(); i++) {
         search(initialSearches.get(i).toString());
         removeTempFile(initialSearches.get(i).toString());
      } // close for loop
   } // close conductInitialSearches
   
   // method to perform searches
   public void search(String searchedItem) throws IOException {
      URL url = new URL(WEBSTORE_URL + searchedItem);
      createConnection(url, searchedItem);
      try {
         parseFile(searchedItem);
      } catch (Exception e) {
         e.printStackTrace();
      } // close try catch
   } // close search
   
   // method to open url connections, and extract url information
   public void createConnection(URL url, String searchedItem) throws IOException {
      URLConnection uc = url.openConnection();
      //uc.addRequestProperty("User-Agent", USER_AGENT);
      saveLocally(uc, searchedItem);
   } // close createConnection
   
   // method to save a file locally
   public void saveLocally(URLConnection uc, String searchedItem) throws IOException {
      InputStream in = uc.getInputStream();
      Path outputFile = Paths.get(searchedItem + ".html");
      if (!new File(outputFile.toString()).exists()) {
         Files.copy(in, outputFile);
      } // close if
   } // close saveLocally
   
   public void removeTempFile(String item) throws IOException {
      Path file = Paths.get(item + ".html");
      Files.delete(file);
   }

   // method to parse HTML files from webstorre
   public void parseFile(String item) throws IOException {
      String file = readFile(item);
      if (!file.contains("search.php?search_empty")) {
         getItem(file, item);
      } // close if
   } // close parseFile
   
   // method to read the HTML files
   public String readFile(String item) throws IOException {
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
   public void getItem(String file, String item) throws IOException {
      String itemDescription = "";
      String regex = "contentfont(\\s+.*?)</tr>";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(file);
      while (matcher.find()) {
         itemDescription = matcher.group();
         getProductDetails(itemDescription, item);
      } // close while 
   } // close getItem
   
   // method to get details about an item
   public void getProductDetails(String itemDescription, String item) throws IOException {
      String name = getProductName(itemDescription);
      String condition = getCondition(itemDescription);
      String biddingPrice = getBiddingPrice(itemDescription);
      int bids = getBids(itemDescription);
      String buyNowPrice = getBuyNowPrice(itemDescription);
      String timeLeft = getTimeLeft(itemDescription);
      URL image = getImage(itemDescription);
      String id = getId(itemDescription);
      String username = login.username;
      String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()).toString();
      Product product = new Product(name, item, condition, biddingPrice, bids, buyNowPrice, timeLeft, image, id, timestamp, username);
      inventory.put(product.id, product);
      results += product.name + "|" + product.id + "|" + product.itemSearched + "|" + product.condition + "|" + product.biddingPrice + "|" + product.bids + "|" + product.buyNowPrice + "|" + product.timeLeft + "|" + product.imageURL + "|" + product.timestamp + "|" + product.username +  "\n";
   } // close getProductDetails
   
   // method to get the name of an item
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
   
   // method to get the time left on an auction for an item
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
   
   // method to get the id of an item
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
   
   // method to add items to the output and cache
   public void addToOutputAndCache() throws IOException {
      PrintWriter writer = new PrintWriter(fp.outputFile, "UTF-8");
      for (Map.Entry<String, Product> entry : inventory.entrySet()) {
         Product value = entry.getValue();
         writer.println(value.name + "|" + entry.getKey() + "|" + value.itemSearched + "|" + value.condition + "|" + value.biddingPrice + "|" + value.bids + "|" + value.buyNowPrice + "|" + value.timeLeft + "|" + value.imageURL + "|" + value.timestamp + "|" + value.username);
      }  // close for loop
      writer.close();
      copyToCache();
   } // close addToOutputAndCache
   
   // method to copy the output file to the cache
   public void copyToCache() throws FileNotFoundException, IOException {
      Files.copy(Paths.get(fp.outputFile), new FileOutputStream(fp.cache));
   } // close copyToCache
   
   // method to send an email
   public void email() {
      if (fp.email == false) {
         return;
      } // close if
      createEmail();
   } // close email
   
   // method to create an email with the search results
   public void createEmail() {
      String emailAddress = JOptionPane.showInputDialog("Enter your email address");
      final String username = "CS370finalproject@gmail.com";
      final String password = "CS370project";
      String subject = "Search Results";
      results = results.substring(4);
      String message = results;
      sendEmail(username, password, emailAddress, subject, message);
   } // close email 

   // method to send the email
   private static void sendEmail(String sender, String password, String recipient, String subject, String message) {
      Properties props = System.getProperties();
      String host = "smtp.gmail.com";
      props.put("mail.smtp.starttls.enable", "true");
      props.put("mail.smtp.host", host);
      props.put("mail.smtp.user", sender);
      props.put("mail.smtp.password", password);
      props.put("mail.smtp.port", "587");
      props.put("mail.smtp.auth", "true");
      Session session = Session.getDefaultInstance(props);
      MimeMessage mimeMessage = new MimeMessage(session);
      try {
          mimeMessage.setFrom(new InternetAddress(sender));
          mimeMessage.addRecipients(Message.RecipientType.TO, recipient);
          mimeMessage.setSubject(subject);
          mimeMessage.setText(message);
          Transport transport = session.getTransport("smtp");
          transport.connect(host, sender, password);
          transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
          transport.close();
      } // close try
      catch (AddressException ae) {
          ae.printStackTrace();
      } // close catch
      catch (MessagingException me) {
          me.printStackTrace();
      } // close catch
   } // close sendEmail
   
} // close NoGUI
