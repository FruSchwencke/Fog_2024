package app.persistence;

import app.entities.Material;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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


    public static void createMaterialLine(int quantity, int orderId, int materialId, ConnectionPool connectionPool) throws DatabaseException {

        //OrderLines are created in DB by this method, which gets called in createOrder
        String sql = "insert into material_list_lines (quantity, order_id, m_id) values (?,?,?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {

            ps.setInt(1, quantity);
            ps.setInt(2, orderId);
            ps.setInt(3, materialId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1)
            {
                throw new DatabaseException("Fejl ved oprettelse af orderlinje");
            }

        } catch (SQLException e) {
            String msg = "Der er sket en fejl ved bestilling. Prøv igen";

            throw new DatabaseException(msg , e.getMessage());
        }
    }



    public static Material getMaterialById (int materialById, ConnectionPool connectionPool) throws DatabaseException {

        Material material = new Material();
        String sql = "SELECT * FROM materials WHERE m_id = ?";


        try (Connection connection = connectionPool.getConnection()) {

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, materialById);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    double price = rs.getDouble("price");
                    int unitId = rs.getInt("unit_id");
                    int width = rs.getInt("width");
                    int length = rs.getInt("length");
                    int height = rs.getInt("height");
                    material.setMaterialId(materialById);
                    material.setName(name);
                    material.setDescription(description);
                    material.setPrice(price);
                    material.setUnitId(unitId);
                    material.setWidth(width);
                    material.setLength(length);
                    material.setHeight(height);
                }
            } catch (SQLException e) {
                throw new DatabaseException(e.getMessage());
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
        return material;
    }


}