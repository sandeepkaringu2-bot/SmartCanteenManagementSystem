package com.smartcanteen.ui;
import com.smartcanteen.model.User;import com.smartcanteen.service.CanteenService;
import javax.swing.*;import java.awt.*;
public class LoginFrame extends JFrame {
 private final CanteenService service=new CanteenService();private final JTextField username=new JTextField(18);private final JPasswordField password=new JPasswordField(18);
 public LoginFrame(){super("Smart Canteen Management System");setDefaultCloseOperation(EXIT_ON_CLOSE);setSize(430,300);setLocationRelativeTo(null);
  JPanel p=new JPanel(new GridBagLayout());p.setBorder(BorderFactory.createEmptyBorder(20,25,20,25));GridBagConstraints g=new GridBagConstraints();g.insets=new Insets(7,7,7,7);g.fill=GridBagConstraints.HORIZONTAL;
  JLabel title=new JLabel("SMART CANTEEN",SwingConstants.CENTER);title.setFont(new Font("SansSerif",Font.BOLD,22));g.gridx=0;g.gridy=0;g.gridwidth=2;p.add(title,g);
  g.gridwidth=1;g.gridy++;g.gridx=0;p.add(new JLabel("Username"),g);g.gridx=1;p.add(username,g);g.gridy++;g.gridx=0;p.add(new JLabel("Password"),g);g.gridx=1;p.add(password,g);
  JButton login=UIUtil.button("Login"),register=UIUtil.button("Register as Student");g.gridy++;g.gridx=0;p.add(login,g);g.gridx=1;p.add(register,g);add(p);
  login.addActionListener(e->doLogin());register.addActionListener(e->new RegisterFrame(this,service).setVisible(true));password.addActionListener(e->doLogin());
 }
 private void doLogin(){try{User user=service.login(username.getText(),new String(password.getPassword()));if(user==null){UIUtil.info(this,"Incorrect username or password.");return;}dispose();if(user.isAdmin())new AdminDashboard(user,service).setVisible(true);else new StudentDashboard(user,service).setVisible(true);}catch(Exception ex){UIUtil.error(this,ex);}}
}
