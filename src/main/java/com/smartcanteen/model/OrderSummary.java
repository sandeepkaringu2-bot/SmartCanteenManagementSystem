package com.smartcanteen.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderSummary {
    private final int orderId;
    private final String studentName;
    private final BigDecimal total;
    private final String status;
    private final LocalDateTime date;

    public OrderSummary(int id, String student, BigDecimal total, String status, LocalDateTime date) {
        this.orderId = id;
        this.studentName = student;
        this.total = total;
        this.status = status;
        this.date = date;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getStudentName() {
        return studentName;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
