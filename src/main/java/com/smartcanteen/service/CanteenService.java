package com.smartcanteen.service;

import com.smartcanteen.dao.FoodDAO;
import com.smartcanteen.dao.OrderDAO;
import com.smartcanteen.dao.UserDAO;
import com.smartcanteen.model.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class CanteenService {
    private final UserDAO userDAO = new UserDAO();
    private final FoodDAO foodDAO = new FoodDAO();
    private final OrderDAO orderDAO = new OrderDAO();

    public User login(String username, String password) throws SQLException {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new IllegalArgumentException("Enter username and password.");
        }
        return userDAO.login(username.trim(), password);
    }

    public void register(String name, String username, String password) throws SQLException {
        if (name == null || name.isBlank() || username == null || username.isBlank() || password == null || password.length() < 6) {
            throw new IllegalArgumentException("Enter name and username. Password must contain at least 6 characters.");
        }
        try {
            userDAO.register(name, username, password);
        } catch (SQLException ex) {
            // Check for MySQL Duplicate Entry Error Code (1062)
            if (ex.getErrorCode() == 1062) {
                throw new IllegalArgumentException("That username is already registered.");
            }
            throw ex;
        }
    }

    public List<FoodItem> availableFoods() throws SQLException {
        return foodDAO.getAvailableFoods();
    }

    public List<FoodItem> allFoods() throws SQLException {
        return foodDAO.getAllFoods();
    }

    public void saveFood(Integer id, String name, String desc, String price, boolean available) throws SQLException {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Food name is required.");
        }
        
        BigDecimal amount;
        try {
            amount = new BigDecimal(price);
        } catch (Exception e) {
            throw new IllegalArgumentException("Enter a valid price.");
        }
        
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }
        
        foodDAO.save(id, name.trim(), desc == null ? "" : desc.trim(), amount, available);
    }

    public void setAvailability(int id, boolean available) throws SQLException {
        foodDAO.setAvailability(id, available);
    }

    public int placeOrder(User user, List<CartItem> cart) throws SQLException {
        if (cart == null || cart.isEmpty()) {
            throw new IllegalArgumentException("Your cart is empty.");
        }
        for (CartItem item : cart) {
            if (item.getQuantity() < 1) {
                throw new IllegalArgumentException("Quantity must be at least one.");
            }
        }
        return orderDAO.placeOrder(user.getUserId(), cart);
    }

    public List<OrderSummary> studentOrders(int userId) throws SQLException {
        return orderDAO.getStudentOrders(userId);
    }

    public List<OrderSummary> allOrders() throws SQLException {
        return orderDAO.getAllOrders();
    }

    public void updateOrderStatus(int orderId, String status) throws SQLException {
        orderDAO.updateStatus(orderId, status);
    }

    public BigDecimal todaySales() throws SQLException {
        return orderDAO.getTodaySales();
    }
}
