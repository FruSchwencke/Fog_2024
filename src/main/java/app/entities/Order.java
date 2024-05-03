package app.entities;

public class Order {

    private int orderId;
    private int lenght;
    private int width;
    private String status;
    private double totalprice;

    public Order(int orderId, int lenght, int width, double totalprice) {
        this.orderId = orderId;
        this.lenght = lenght;
        this.width = width;
        this.totalprice = totalprice;
    }

    public Order(int orderId, String status) {
        this.orderId = orderId;
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getStatus() {
        return status;
    }
}
