package app.entities;

public class Order {

    private int orderId;
    private int length;
    private int width;

    private String textInput;
    private String status;
    private double totalprice;
    private int statusId;


    public Order(int orderId, int length, int width, String status, double totalprice) {
        this.orderId = orderId;
        this.length = length;
        this.width = width;
        this.status = status;
        this.totalprice = totalprice;
    }

    public Order(int orderId, int length, int width, double totalprice, String textInput, String status) {
        this.orderId = orderId;
        this.length = length;
        this.width = width;
        this.totalprice = totalprice;
        this.textInput = textInput;
        this.status = status;
    }


    public Order(int length, int width, String status, double totalprice) {
        this.length = length;
        this.width = width;
        this.status = status;
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

    public String getTextInput() {
        return textInput;
    }

    public double getTotalprice() {
        return totalprice;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
