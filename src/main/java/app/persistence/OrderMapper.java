package app.persistence;

import app.entities.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper {

    public static List<Order> getAllOrders(ConnectionPool connectionPool) throws SQLException {
        List<Order> allOrdersList = new ArrayList<>();
        String sql = "SELECT order_id, status_id FROM orders";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                String status = rs.getString("status_id");

                Order order = new Order(orderId, status);
                allOrdersList.add(order);

            }
            return allOrdersList;
        }
    }

    public static Order getOrderDetails(int orderId, ConnectionPool connectionPool)
    {
        String sql = "SELECT lenght, width, total_price FROM orders WHERE order_id = ?";
        Order orderDetails = null;
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
                ps.setInt(1, orderId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                int lenght = rs.getInt("length");
                int width = rs.getInt("width");
                double totalprice = rs.getDouble("total_price");

                orderDetails = new Order(orderId, lenght, width, totalprice);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return orderDetails;
    }



}
