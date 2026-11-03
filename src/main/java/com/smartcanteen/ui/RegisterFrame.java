package com.smartcanteen.ui;
import com.smartcanteen.service.CanteenService;import javax.swing.*;import java.awt.*;
public class RegisterFrame extends JDialog {
 private final JTextField name=new JTextField(18),username=new JTextField(18);private final JPasswordField password=new JPasswordField(18);
 public RegisterFrame(JFrame parent,CanteenService service){super(parent,"Student Registration",true);setSize(380,250);setLocationRelativeTo(parent);JPanel p=new JPanel(new GridLayout(4,2,8,8));p.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));p.add(new JLabel("Full name"));p.add(name);p.add(new JLabel("Username"));p.add(username);p.add(new JLabel("Password (6+ chars)"));p.add(password);JButton create=UIUtil.button("Create account"),cancel=UIUtil.button("Cancel");p.add(create);p.add(cancel);add(p);
 create.addActionListener(e->{try{service.register(name.getText(),username.getText(),new String(password.getPassword()));UIUtil.info(this,"Registration successful. You can now log in.");dispose();}catch(Exception ex){UIUtil.error(this,ex);}});cancel.addActionListener(e->dispose());}
}
