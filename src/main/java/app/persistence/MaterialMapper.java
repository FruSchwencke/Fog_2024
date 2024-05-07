package app.persistence;


import app.entities.Material;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


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
    public static Material findMaterialForUpdateMaterial(int materialId, ConnectionPool connectionPool) throws DatabaseException {
        Material material= null;
        String sql="Select * from materials where m_id = ?";

        try(Connection connection = connectionPool.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql))

        {
         ps.setInt(1,materialId);
            ResultSet rs =ps.executeQuery();
            if (rs.next())
            {
            String name = rs.getString("name");
            String description = rs.getString("description");
            double price = rs.getDouble("price");
            material =new Material(name,description,price);
            }

        } catch (SQLException e)
        {
            throw new DatabaseException("Fejl ved indhentning af oplysninger på matriale id =" + materialId, e.getMessage());
        }
        return material;
    }
    public static void updateMaterial(int materialId,String name, String description, double price, ConnectionPool connectionPool) throws DatabaseException
    {
        String sql = "update materials set name=?, description=?, price = ? where m_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        )
        {
            ps.setString(1, name);
            ps.setString(2, description);
            ps.setDouble(3, price);
            ps.setInt(4, materialId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1)
            {
                throw new DatabaseException("Fejl i opdatering af en materialer");
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException("Fejl i opdatering af en materialer", e.getMessage());
        }
    }
    public static List<Material> companyMaterialList (ConnectionPool connectionPool) throws DatabaseException {
        List <Material> materials= new ArrayList<>();
        String sql="Select * from materials";

        try(Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql))

        {

            ResultSet rs =ps.executeQuery();
            while (rs.next())
            {
               int materialId = rs.getInt("m_id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                int unitId = rs.getInt("unit_id");
                int width = rs.getInt("width");
                int length = rs.getInt("length");
                int height = rs.getInt("height");
                Material material = new Material(materialId,name,description,price,unitId,width,length,height);
                materials.add(material);

            }

        } catch (SQLException e)
        {
            throw new DatabaseException("Fejl ved indhentning af virksomhedens materiale liste =" , e.getMessage());
        }
        return materials;
    }
}
