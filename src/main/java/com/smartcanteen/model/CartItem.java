package com.smartcanteen.model;

import java.math.BigDecimal;

public class CartItem {
    private final FoodItem food;
    private int quantity;
    public CartItem(FoodItem food, int quantity){this.food=food;this.quantity=quantity;}
    public FoodItem getFood(){return food;} public int getQuantity(){return quantity;}
    public void setQuantity(int quantity){this.quantity=quantity;}
    public BigDecimal getLineTotal(){return food.getPrice().multiply(BigDecimal.valueOf(quantity));}
}
