package app.persistence;


import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class MaterialMapper {
    public static void addMaterial (String name, String description, double price, int unitId, int width, int length, int height, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "insert into materials (name, description, price, unit_id, width, length, height) values (?,?,?,?,?,?,?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, description);
            ps.setDouble(3, price);
            ps.setInt(4, unitId);
            ps.setInt(5, width);
            ps.setInt(6, length);
            ps.setInt(7, height);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Fejl ved oprettelse af materiale ");
            }

        } catch (SQLException e) {
            String msg = "Der er sket en fejl. Prøv igen";

            throw new DatabaseException(msg, e.getMessage());
        }
    }
}
