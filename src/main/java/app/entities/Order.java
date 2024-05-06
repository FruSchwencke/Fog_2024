package app.entities;

public class Order {

    private int orderId;
    private int length;
    private int width;

    private String textInput;
    private String status;
    private double totalprice;

    public Order(int orderId, int length, int width, double totalprice) {
        this.orderId = orderId;
        this.length = length;
        this.width = width;
        this.totalprice = totalprice;
    }

    public Order(int orderId, double totalprice) {
        this.orderId = orderId;
        this.totalprice = totalprice;
    }

    public Order(int length, int width, String textInput) {
        this.length = length;
        this.width = width;
        this.textInput = textInput;
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

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public double getTotalprice() {
        return totalprice;
    }
}
