package app.persistence;

import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper {

    public static boolean checkZipCode (int zipcode, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "select * from zip_code where zip_code = ?";
    try (Connection connection = connectionPool.getConnection();
    PreparedStatement ps = connection.prepareStatement(sql))
    {
        ps.setInt(1,zipcode);

       ResultSet rs = ps.executeQuery();

           return rs.next();

    } catch (SQLException e) {
        String msg= "der er sket en fejl";
        throw new DatabaseException(msg, e.getMessage());
    }

    }
    public static void createuser(String email, String password, String firstName, String lastName, String phonenumber, String address, int zipcode, ConnectionPool connectionPool) throws DatabaseException
    {
        String sql = "insert into users (email, password, first_name, last_name, phonenumber, address, zip_code) values (?,?,?,?,?,?,?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        )
        {
            ps.setString(1, email);
            ps.setString(2, password);
            ps.setString(3, firstName);
            ps.setString(4, lastName);
            ps.setString(5, phonenumber);
            ps.setString(6, address);
            ps.setInt(7, zipcode);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1)
            {
                throw new DatabaseException("Fejl ved oprettelse af ny bruger");
            }
        }
        catch (SQLException e)
        {
            String msg = "Der er sket en fejl. Prøv igen";
            if (e.getMessage().startsWith("ERROR: duplicate key value "))
            {
                msg = "Brugernavnet findes allerede. Vælg et andet";
            }
            throw new DatabaseException(msg, e.getMessage());
        }
    }
}
