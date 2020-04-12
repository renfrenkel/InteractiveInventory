import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

// class to handle Queries
public class QueryMenuHandler extends JFrame implements ActionListener{
   
   String results; // String of the search results
   
   // constructor
   public QueryMenuHandler() {
   } // close constructor
   
   // method to perform query actions
   @Override
   public void actionPerformed(ActionEvent event) {
      String menuName = event.getActionCommand();
      if (menuName.equals("Email Results")) {
         email();
      } // close if
      if (menuName.equals("Save Results")) {
         try {
            save();
         } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
         }
      } // close if
   } // close actionPerformed
   
   // method to put the search results into a string
   public void setResults(String printout) {
      results = printout;
      results = results.substring(4);
   } // close setReults
   
   // method to create an email with the search results
   public void email() {
      String emailAddress = JOptionPane.showInputDialog("Enter your email address");
      final String username = "CS370finalproject@gmail.com";
      final String password = "CS370project";
      String subject = "Search Results";
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
   
   // method to save the search results to a file
   public void save() throws FileNotFoundException, UnsupportedEncodingException {
      String fileName = "QueryResults.txt";
      PrintWriter writer = new PrintWriter(fileName, "UTF-8");
      writer.print(results);
      writer.close();
   } // close save

} // close QueryMenuHandler
