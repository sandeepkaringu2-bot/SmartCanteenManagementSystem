package com.smartcanteen;

import com.smartcanteen.ui.LoginFrame;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Fallback to default cross-platform Look & Feel if system-specific fails
            }
            new LoginFrame().setVisible(true);
        });
    }
}
