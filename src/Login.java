import java.awt.BorderLayout;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

// class for user login
public class Login {
   
   HashMap<String, Product> inventory; // main inventory hashmap
   FlagParser fp; // command line flag parser
   String username; // username
   String password; // password
   JFrame frame; // GUI
     
   // constructor
   public Login(HashMap<String, Product> inv, FlagParser flagParser){
      inventory = inv;
      fp = flagParser;
      frame = new JFrame();
      frame.setSize(300, 200);
      frame.setLocation(500, 200);
      JPanel panel = new JPanel();
      JLabel usernameLabel = new JLabel("Username:");   
      JLabel passwordLabel = new JLabel("Password:");
      JTextField usernameInputBox = new JTextField(20);
      JTextField passwordInputBox = new JTextField(20);
      JButton loginButton = new JButton("Login");
      panel.add(usernameLabel);
      panel.add(usernameInputBox);
      panel.add(passwordLabel);
      panel.add(passwordInputBox); 
      LoginListener ll = new LoginListener(this, usernameInputBox, passwordInputBox, inventory, fp);
      loginButton.addActionListener(ll);
      panel.add(loginButton);
      frame.getContentPane().add(BorderLayout.CENTER,panel);
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      frame.setVisible(true);
  } // close constructor

} // close Login
