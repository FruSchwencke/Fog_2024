package app.persistence;

import app.entities.Order;
import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
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

    public static int createOrder(User user, int width, int length, int textInput, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "insert into orders (user_id, width, length, text_input) values (?,?,?,?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, user.getUserId());
            ps.setInt(2, width);
            ps.setInt(3, length);
            ps.setInt(4, textInput);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 1)
            {
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                int orderId = rs.getInt("order_id");
                return orderId;



            //TODO: go through error-handling
            } else {
                throw new DatabaseException("Fejl. Prøv igen");
            }

        } catch (SQLException e) {
            String msg = "Der er sket en fejl. Prøv igen";
            if (e.getMessage().startsWith("ERROR: duplicate key value ")) {
                msg = "Brugernavnet findes allerede. Vælg et andet";
            }
            throw new DatabaseException(msg, e.getMessage());
        }
    }
}
