package app.persistence;

import app.entities.Order;
import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderMapper {

    public static List<Order> getAllOrders(ConnectionPool connectionPool) throws DatabaseException {
        List<Order> allOrdersList = new ArrayList<>();
        String sql = "SELECT o.order_id, s.status_name " +
                "FROM orders o " +
                "JOIN status s ON o.status_id = s.status_id " +
                "ORDER BY s.status_id";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                String status = rs.getString("status_name");
                Order order = new Order(orderId, status);
                allOrdersList.add(order);
            }
        } catch (SQLException e) {

            throw new DatabaseException("Ingen ordre at hente");
        }
        return allOrdersList;
    }

    public static Order getOrderPrUser(int userId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT o.order_id, o.length, o.width, o.total_price, s.status_name " +
                "FROM orders o " +
                "JOIN status s ON o.status_id = s.status_id " +
                "WHERE o.user_id = ?";

        Order orderUser = null;
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int orderId = rs.getInt("order_id");
                int length = rs.getInt("length");
                int width = rs.getInt("width");
                double totalprice = rs.getDouble("total_price");
                String status = rs.getString("status_name");
                orderUser = new Order(orderId, length, width, status, totalprice);

            }

        } catch (SQLException e) {
            throw new DatabaseException("kunne ikke indhente ordre med det brugerID", e.getMessage());
        }
        return orderUser;
    }

    public static Order getOrderDetails(int orderId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT o.order_id, o.length, o.width, o.total_price, o.text_input, s.status_name " +
                "FROM orders o " +
                "JOIN status s ON o.status_id = s.status_id " +
                "WHERE o.order_id = ?";

        Order orderDetails = null;
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, orderId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                int length = rs.getInt("length");
                int width = rs.getInt("width");
                double totalprice = rs.getDouble("total_price");
                String textInput = rs.getString("text_input");
                String status = rs.getString("status_name");

                orderDetails = new Order(orderId, length, width, totalprice, textInput, status);

            }
        } catch (SQLException e) {
            throw new DatabaseException("kunne ikke fremskaffe ordredetaljer med det ordreid", e.getMessage());
        }

        return orderDetails;
    }

    public static User getUserInformation(int orderId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT  u.first_name,  u.last_name, u.email, u.address, z.zip_code, z.city, u.phonenumber FROM orders o "
                + "JOIN " + "users u ON o.user_id = u.user_id " +
                "JOIN " + "zip_code z ON u.zip_code = z.zip_code " +
                "WHERE o.order_id = ?";
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String email = rs.getString("email");
                    String address = rs.getString("address");
                    String zipCode = rs.getString("zip_code");
                    String city = rs.getString("city");
                    String phoneNumber = rs.getString("phonenumber");

                    return new User(firstName, lastName, email, address, zipCode, city, phoneNumber);
                } else {
                    throw new DatabaseException("Ingen bruger fundet for order id: " + orderId);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Der opstod en fejl med at få bruger information på det ordreId", e.getMessage());
        }
    }


    public static int createOrder(int userId, int width, int length, String textInput, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "insert into orders (user_id, width, length, text_input) values (?,?,?,?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, userId);
            ps.setInt(2, width);
            ps.setInt(3, length);
            ps.setString(4, textInput);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                int orderId = rs.getInt("order_id");
                return orderId;



            } else {
                throw new DatabaseException("Fejl ved oprettelse af ordre. Prøv igen");
            }

        } catch (SQLException e) {
           throw new DatabaseException("Der er sket en fejl, prøv igen",e.getMessage());
        }
    }

    public static void updateStatus(int orderId, int newStatusId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "UPDATE orders SET status_id = ? WHERE order_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, newStatusId);
            ps.setInt(2, orderId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new DatabaseException("Ingen rækker blev påvirket. Ordren med id " + orderId + " blev ikke fundet.");
            }
        } catch (SQLException e) {
            String msg = "Der er sket en fejl ved opdatering af ordrestatus. Prøv igen.";
            throw new DatabaseException(msg, e.getMessage());
        }
    }

    public static double updateTotalPrice(int orderId, double newTotalPrice, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "UPDATE orders SET total_price = ? WHERE order_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setDouble(1, newTotalPrice);
            ps.setInt(2, orderId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Fejl ved opdatering af totalpris for ordre " + orderId);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Fejl i opdatering af pris", e.getMessage());
        }
        return newTotalPrice;
    }

    public static double getCostPrice(int orderId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT mll.quantity, m.price " +
                "FROM material_list_lines mll " +
                "JOIN materials m ON mll.m_id = m.m_id " +
                "WHERE order_id = ?";

        double costPrice = 0.0;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                double lineTotal = quantity * price;

                costPrice += lineTotal;
            }
        } catch (SQLException e) {
            throw new DatabaseException("der var udfordringer med at fremskaffe prisen", e.getMessage());
        }
        return costPrice;
    }

}
