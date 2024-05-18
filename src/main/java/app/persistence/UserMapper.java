package app.persistence;


import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper {


    public static boolean checkZipCode (String zipcode, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "select * from zip_code where zip_code = ?";
    try (Connection connection = connectionPool.getConnection();
    PreparedStatement ps = connection.prepareStatement(sql))
    {
        ps.setString(1,zipcode);

       ResultSet rs = ps.executeQuery();

           return rs.next();

    } catch (SQLException e) {

        throw new DatabaseException("der skete en fejl ved indhentning af data", e.getMessage());
    }

    }
    public static void createuser(String email, String password, String firstName, String lastName, String phonenumber, String address, String zipcode, ConnectionPool connectionPool) throws DatabaseException
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
            ps.setString(7, zipcode);

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
                msg = "mail eller telefon nummer findes allerede. Vælg et andet";
            }
            throw new DatabaseException(msg, e.getMessage());
        }
    }

    public static User login(String email, String password, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT u.*, z.* " +
                "FROM users u " +
                "JOIN zip_code z ON u.zip_code = z.zip_code " +
                "WHERE u.email = ?";


        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)

        ) {
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if (password.equals(rs.getString("password")))  {
                    int userId = rs.getInt("user_id");
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    int role = rs.getInt("role_id");
                    String address = rs.getString("address");
                    String zipcode = rs.getString("zip_code");
                    String city = rs.getString("city");
                    String phonenumber = rs.getString("phonenumber");
                    return new User(userId, firstName, lastName, email, password, address, phonenumber, zipcode, city, role);
                } else {
                    throw new DatabaseException("Forkert kodeord. Prøv igen.");
                }
            } else {
                throw new DatabaseException("Brugeren med denne e-mail blev ikke fundet.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB fejl", e.getMessage());
        }
    }

}
