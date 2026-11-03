package com.smartcanteen.ui;

import javax.swing.*;
import java.awt.*;

public final class UIUtil {
    
    private UIUtil() {
        // Prevent instantiation of utility class
    }
    
    public static void error(Component parent, Exception e) {
        String msg = (e.getMessage() == null || e.getMessage().trim().isEmpty()) 
                     ? "An unexpected error occurred: " + e.getClass().getSimpleName() 
                     : e.getMessage();
        JOptionPane.showMessageDialog(parent, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public static void info(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Smart Canteen", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static JButton button(String text) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        return b;
    }
    
    public static JPanel panel() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        return p;
    }
}
