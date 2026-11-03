package com.smartcanteen.dao;

import com.smartcanteen.database.DBConnection;
import com.smartcanteen.model.User;
import com.smartcanteen.service.PasswordUtil;
import java.sql.*;

public class UserDAO {
    
    public User login(String username, String password) throws SQLException {
        String sql = "SELECT user_id, name, username, password_hash, role FROM users WHERE username = ?";
        try (Connection c = DBConnection.getConnection(); 
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && PasswordUtil.matches(password, rs.getString("password_hash"))) {
                    return new User(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("username"),
                        rs.getString("role")
                    );
                }
            }
        }
        return null;
    }

    public boolean register(String name, String username, String password) throws SQLException {
        String sql = "INSERT INTO users (name, username, password_hash, role) VALUES (?, ?, ?, 'STUDENT')";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setString(1, name.trim());
            ps.setString(2, username.trim());
            ps.setString(3, PasswordUtil.hash(password));
            
            return ps.executeUpdate() == 1;
        }
    }
}
