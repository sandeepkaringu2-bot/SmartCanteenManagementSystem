package com.smartcanteen.ui;

import com.smartcanteen.model.*;
import com.smartcanteen.service.CanteenService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.*;

public class StudentDashboard extends JFrame {
    private final User user;
    private final CanteenService service;
    
    private final DefaultTableModel menuModel = new DefaultTableModel(new Object[]{"ID", "Food", "Description", "Price"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    
    private final DefaultTableModel cartModel = new DefaultTableModel(new Object[]{"Food ID", "Food", "Qty", "Unit Price", "Line Total"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    
    private final DefaultTableModel historyModel = new DefaultTableModel(new Object[]{"Order ID", "Date", "Total", "Status"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    
    private final JTable menuTable = new JTable(menuModel);
    private final JTable cartTable = new JTable(cartModel);
    private final JTable historyTable = new JTable(historyModel);
    private final Map<Integer, CartItem> cart = new LinkedHashMap<>();
    private final JLabel totalLabel = new JLabel("Total: ₹0.00");
    
    public StudentDashboard(User user, CanteenService service) {
        super("Smart Canteen - Student: " + user.getName());
        this.user = user;
        this.service = service;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(850, 570);
        setLocationRelativeTo(null);
        
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Food Menu", menuPanel());
        tabs.addTab("My Cart", cartPanel());
        tabs.addTab("Order History", historyPanel());
        add(tabs, BorderLayout.CENTER);
        
        JButton logout = UIUtil.button("Logout");
        logout.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        add(logout, BorderLayout.SOUTH);
        
        refreshMenu();
        refreshHistory();
    }
    
    private JPanel menuPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JScrollPane(menuTable), BorderLayout.CENTER);
        
        JPanel actions = new JPanel();
        JButton add = UIUtil.button("Add selected item");
        JButton refresh = UIUtil.button("Refresh menu");
        actions.add(add);
        actions.add(refresh);
        p.add(actions, BorderLayout.SOUTH);
        
        add.addActionListener(e -> {
            int row = menuTable.getSelectedRow();
            if (row < 0) {
                UIUtil.info(this, "Select a food item first.");
                return;
            }
            try {
                int id = Integer.parseInt(menuModel.getValueAt(row, 0).toString());
                FoodItem f = service.availableFoods().stream()
                        .filter(x -> x.getFoodId() == id)
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException("Food item no longer available."));
                
                CartItem ci = cart.get(id);
                if (ci == null) {
                    cart.put(id, new CartItem(f, 1));
                } else {
                    ci.setQuantity(ci.getQuantity() + 1);
                }
                refreshCart();
                UIUtil.info(this, f.getFoodName() + " added to cart.");
            } catch (Exception ex) {
                UIUtil.error(this, ex);
            }
        });
        
        refresh.addActionListener(e -> refreshMenu());
        return p;
    }
    
    private JPanel cartPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JScrollPane(cartTable), BorderLayout.CENTER);
        
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton minus = UIUtil.button("- Qty");
        JButton plus = UIUtil.button("+ Qty");
        JButton remove = UIUtil.button("Remove");
        JButton place = UIUtil.button("Place order");
        
        bottom.add(minus);
        bottom.add(plus);
        bottom.add(remove);
        bottom.add(totalLabel);
        bottom.add(place);
        p.add(bottom, BorderLayout.SOUTH);
        
        minus.addActionListener(e -> changeQty(-1));
        plus.addActionListener(e -> changeQty(1));
        
        remove.addActionListener(e -> {
            int r = cartTable.getSelectedRow();
            if (r >= 0) {
                try {
                    int id = Integer.parseInt(cartModel.getValueAt(r, 0).toString());
                    cart.remove(id);
                    refreshCart();
                } catch (Exception ex) {
                    UIUtil.error(this, ex);
                }
            } else {
                UIUtil.info(this, "Select an item from the cart to remove.");
            }
        });
        
        place.addActionListener(e -> {
            if (cart.isEmpty()) {
                UIUtil.info(this, "Your cart is empty!");
                return;
            }
            try {
                int id = service.placeOrder(user, new ArrayList<>(cart.values()));
                UIUtil.info(this, "Order #" + id + " placed successfully. Total: ₹" + total());
                cart.clear();
                refreshCart();
                refreshHistory();
            } catch (Exception ex) {
                UIUtil.error(this, ex);
            }
        });
        
        return p;
    }
    
    private void changeQty(int delta) {
        int r = cartTable.getSelectedRow();
        if (r < 0) {
            UIUtil.info(this, "Select an item in the cart to alter quantity.");
            return;
        }
        try {
            int id = Integer.parseInt(cartModel.getValueAt(r, 0).toString());
            CartItem item = cart.get(id);
            if (item != null) {
                int q = item.getQuantity() + delta;
                if (q < 1) {
                    cart.remove(id);
                } else {
                    item.setQuantity(q);
                }
                refreshCart();
            }
        } catch (Exception ex) {
            UIUtil.error(this, ex);
        }
    }
    
    private JPanel historyPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JScrollPane(historyTable), BorderLayout.CENTER);
        
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton refresh = UIUtil.button("Refresh history");
        bottom.add(refresh);
        p.add(bottom, BorderLayout.SOUTH);
        
        refresh.addActionListener(e -> refreshHistory());
        return p;
    }
    
    private BigDecimal total() {
        return cart.values().stream()
                .map(CartItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    private void refreshMenu() {
        try {
            menuModel.setRowCount(0);
            for (FoodItem f : service.availableFoods()) {
                menuModel.addRow(new Object[]{f.getFoodId(), f.getFoodName(), f.getDescription(), "₹" + f.getPrice()});
            }
        } catch (Exception e) {
            UIUtil.error(this, e);
        }
    }
    
    private void refreshCart() {
        cartModel.setRowCount(0);
        for (CartItem i : cart.values()) {
            cartModel.addRow(new Object[]{
                i.getFood().getFoodId(), 
                i.getFood().getFoodName(), 
                i.getQuantity(), 
                "₹" + i.getFood().getPrice(), 
                "₹" + i.getLineTotal()
            });
        }
        totalLabel.setText("Total: ₹" + total());
    }
    
    private void refreshHistory() {
        try {
            historyModel.setRowCount(0);
            for (OrderSummary o : service.studentOrders(user.getUserId())) {
                historyModel.addRow(new Object[]{o.getOrderId(), o.getDate(), "₹" + o.getTotal(), o.getStatus()});
            }
        } catch (Exception e) {
            UIUtil.error(this, e);
        }
    }
}
