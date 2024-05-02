package app.entities;

public class Order {

    private int orderId;
    private int lenght;
    private int width;
    private String status;
    private double totalprice;

    public Order(int orderId, String status) {
        this.orderId = orderId;
        this.status = status;
    }
}
