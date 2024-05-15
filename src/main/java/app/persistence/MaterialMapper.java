package app.persistence;


import app.entities.Material;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class MaterialMapper {
    public static void addMaterial(String name, String description, double price, int unitId, int width, int length, int height, ConnectionPool connectionPool) throws DatabaseException {
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

    public static Material findMaterialForUpdateMaterial(int materialId, ConnectionPool connectionPool) throws DatabaseException {
        Material material = null;
        String sql = "Select * from materials where m_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, materialId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                material = new Material(name, description, price);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved indhentning af oplysninger på matriale id =" + materialId, e.getMessage());
        }
        return material;
    }

    public static void updateMaterial(int materialId, String name, String description, double price, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "update materials set name=?, description=?, price = ? where m_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, name);
            ps.setString(2, description);
            ps.setDouble(3, price);
            ps.setInt(4, materialId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Fejl i opdatering af ønsket materiale");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Der skete en fejl i processen, prøv igen", e.getMessage());
        }
    }

    public static List<Material> companyMaterialList(ConnectionPool connectionPool) throws DatabaseException {
        List<Material> materials = new ArrayList<>();
        String sql = "Select * from materials";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int materialId = rs.getInt("m_id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                int unitId = rs.getInt("unit_id");
                int width = rs.getInt("width");
                int length = rs.getInt("length");
                int height = rs.getInt("height");
                Material material = new Material(materialId, name, description, price, unitId, width, length, height);
                materials.add(material);

            }

        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved indhentning af virksomhedens materiale liste =", e.getMessage());
        }
        return materials;
    }

    public static Material getMaterialById(int materialById, ConnectionPool connectionPool) throws DatabaseException {

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
                throw new DatabaseException("Fejl ved indhenting at informationer om valgte materialeID", e.getMessage());
            }
        } catch (SQLException e) {
            throw new DatabaseException("havde udfordringer med connection", e.getMessage());
        }
        return material;
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
            if (rowsAffected != 1) {
                throw new DatabaseException("Fejl ved oprettelse af orderlinje");
            }

        } catch (SQLException e) {
            String msg = "Der er sket en fejl ved bestilling. Prøv igen";

            throw new DatabaseException(msg, e.getMessage());
        }
    }

    public static List<Material> getMaterialByDescription(String description, ConnectionPool connectionPool) throws DatabaseException {

        List<Material> materialList = new ArrayList<>();
        String sql = "SELECT * FROM materials WHERE description = ?";


        try (Connection connection = connectionPool.getConnection()) {

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, description);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int materialId = rs.getInt("m_id");
                    String name = rs.getString("name");
                    double price = rs.getDouble("price");
                    int unitId = rs.getInt("unit_id");
                    int width = rs.getInt("width");
                    int length = rs.getInt("length");
                    int height = rs.getInt("height");


                    materialList.add(new Material(materialId, name, description, price, unitId, width, length, height));
                }
            } catch (SQLException e) {
                throw new DatabaseException("fejl ved indhentelse af materiale ud fra beskrivelsen", e.getMessage());
            }
        } catch (SQLException e) {
            throw new DatabaseException("fejl med connection, prøv igen", e.getMessage());
        }
        return materialList;
    }

    public static List<Material> getOrderMaterialList(int orderId, ConnectionPool connectionpool) {
        List<Material> orderMaterialList = new ArrayList<>();

        String sql = "SELECT mll.quantity, " +
                "       m.name AS material_name, " +
                "       m.description, " +
                "       m.length AS material_length, " +
                "       u.name AS unit_name " +
                "FROM material_list_lines mll " +
                "JOIN materials m ON mll.m_id = m.m_id " +
                "JOIN unit u ON m.unit_id = u.unit_id " +
                "WHERE mll.order_id = ?";


        try (
                Connection connection = connectionpool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {

            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int quantity = rs.getInt("quantity");
                String name = rs.getString("material_name");
                String description = rs.getString("description");
                int length = rs.getInt("material_length");
                String unitName = rs.getString("unit_name");

                orderMaterialList.add(new Material(name, description, length, quantity, unitName));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return orderMaterialList;
    }
}
