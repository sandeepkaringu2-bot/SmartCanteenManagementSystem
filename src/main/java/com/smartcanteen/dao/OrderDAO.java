package com.smartcanteen.dao;

import com.smartcanteen.database.DBConnection;
import com.smartcanteen.model.CartItem;
import com.smartcanteen.model.OrderSummary;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    public int placeOrder(int userId,List<CartItem> cart) throws SQLException {
        BigDecimal total=cart.stream().map(CartItem::getLineTotal).reduce(BigDecimal.ZERO,BigDecimal::add);
        String orderSql="INSERT INTO orders(user_id,total_amount,status) VALUES(?,?,'PENDING')";
        String itemSql="INSERT INTO order_items(order_id,food_id,food_name,quantity,unit_price) VALUES(?,?,?,?,?)";
        try(Connection c=DBConnection.getConnection()){
            c.setAutoCommit(false);
            try(PreparedStatement orderPs=c.prepareStatement(orderSql,Statement.RETURN_GENERATED_KEYS);
                PreparedStatement itemPs=c.prepareStatement(itemSql)){
                orderPs.setInt(1,userId);orderPs.setBigDecimal(2,total);orderPs.executeUpdate();
                int orderId;
                try(ResultSet keys=orderPs.getGeneratedKeys()){if(!keys.next())throw new SQLException("Could not retrieve order ID");orderId=keys.getInt(1);}
                for(CartItem item:cart){itemPs.setInt(1,orderId);itemPs.setInt(2,item.getFood().getFoodId());itemPs.setString(3,item.getFood().getFoodName());itemPs.setInt(4,item.getQuantity());itemPs.setBigDecimal(5,item.getFood().getPrice());itemPs.addBatch();}
                itemPs.executeBatch();c.commit();return orderId;
            }catch(SQLException ex){c.rollback();throw ex;}finally{c.setAutoCommit(true);}
        }
    }
    public List<OrderSummary> getStudentOrders(int userId) throws SQLException {
        List<OrderSummary> list=new ArrayList<>();
        String sql="SELECT o.order_id,u.name,o.total_amount,o.status,o.order_date FROM orders o JOIN users u ON o.user_id=u.user_id WHERE o.user_id=? ORDER BY o.order_date DESC";
        try(Connection c=DBConnection.getConnection();PreparedStatement ps=c.prepareStatement(sql)){ps.setInt(1,userId);try(ResultSet rs=ps.executeQuery()){while(rs.next())list.add(map(rs));}}
        return list;
    }
    public List<OrderSummary> getAllOrders() throws SQLException {
        List<OrderSummary> list=new ArrayList<>();String sql="SELECT o.order_id,u.name,o.total_amount,o.status,o.order_date FROM orders o JOIN users u ON o.user_id=u.user_id ORDER BY o.order_date DESC";
        try(Connection c=DBConnection.getConnection();Statement st=c.createStatement();ResultSet rs=st.executeQuery(sql)){while(rs.next())list.add(map(rs));}return list;
    }
    private OrderSummary map(ResultSet rs)throws SQLException{return new OrderSummary(rs.getInt("order_id"),rs.getString("name"),rs.getBigDecimal("total_amount"),rs.getString("status"),rs.getTimestamp("order_date").toLocalDateTime());}
    public void updateStatus(int orderId,String status)throws SQLException{
        try(Connection c=DBConnection.getConnection();PreparedStatement ps=c.prepareStatement("UPDATE orders SET status=? WHERE order_id=?")){ps.setString(1,status);ps.setInt(2,orderId);ps.executeUpdate();}
    }
    public BigDecimal getTodaySales()throws SQLException{
        String sql="SELECT COALESCE(SUM(total_amount),0) FROM orders WHERE status='COMPLETED' AND DATE(order_date)=CURDATE()";
        try(Connection c=DBConnection.getConnection();Statement st=c.createStatement();ResultSet rs=st.executeQuery(sql)){rs.next();return rs.getBigDecimal(1);}
    }
}
