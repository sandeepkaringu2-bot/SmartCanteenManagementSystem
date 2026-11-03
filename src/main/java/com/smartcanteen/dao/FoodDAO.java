package com.smartcanteen.dao;

import com.smartcanteen.database.DBConnection;
import com.smartcanteen.model.FoodItem;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodDAO {
    public List<FoodItem> getAvailableFoods() throws SQLException { return queryFoods(true); }
    public List<FoodItem> getAllFoods() throws SQLException { return queryFoods(false); }
    private List<FoodItem> queryFoods(boolean onlyAvailable) throws SQLException {
        List<FoodItem> list=new ArrayList<>();
        String sql="SELECT * FROM food_items"+(onlyAvailable?" WHERE available=TRUE":"")+" ORDER BY food_name";
        try(Connection c=DBConnection.getConnection();Statement st=c.createStatement();ResultSet rs=st.executeQuery(sql)){
            while(rs.next()) list.add(new FoodItem(rs.getInt("food_id"),rs.getString("food_name"),rs.getString("description"),rs.getBigDecimal("price"),rs.getBoolean("available")));
        }
        return list;
    }
    public void save(Integer id,String name,String description,BigDecimal price,boolean available) throws SQLException {
        if(id==null){String sql="INSERT INTO food_items(food_name,description,price,available) VALUES(?,?,?,?)";
            try(Connection c=DBConnection.getConnection();PreparedStatement ps=c.prepareStatement(sql)){ps.setString(1,name);ps.setString(2,description);ps.setBigDecimal(3,price);ps.setBoolean(4,available);ps.executeUpdate();}
        } else {String sql="UPDATE food_items SET food_name=?,description=?,price=?,available=? WHERE food_id=?";
            try(Connection c=DBConnection.getConnection();PreparedStatement ps=c.prepareStatement(sql)){ps.setString(1,name);ps.setString(2,description);ps.setBigDecimal(3,price);ps.setBoolean(4,available);ps.setInt(5,id);ps.executeUpdate();}}
    }
    public void setAvailability(int id,boolean available) throws SQLException {
        try(Connection c=DBConnection.getConnection();PreparedStatement ps=c.prepareStatement("UPDATE food_items SET available=? WHERE food_id=?")){ps.setBoolean(1,available);ps.setInt(2,id);ps.executeUpdate();}
    }
}
