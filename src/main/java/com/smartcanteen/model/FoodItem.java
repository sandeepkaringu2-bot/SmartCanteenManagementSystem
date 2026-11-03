package com.smartcanteen.model;

import java.math.BigDecimal;

public class FoodItem {
    private final int foodId;
    private final String foodName;
    private final String description;
    private final BigDecimal price;
    private final boolean available;
    public FoodItem(int foodId, String foodName, String description, BigDecimal price, boolean available) {
        this.foodId=foodId; this.foodName=foodName; this.description=description; this.price=price; this.available=available;
    }
    public int getFoodId(){return foodId;} public String getFoodName(){return foodName;}
    public String getDescription(){return description;} public BigDecimal getPrice(){return price;}
    public boolean isAvailable(){return available;}
    @Override public String toString(){return foodName+" (₹"+price+")";}
}
