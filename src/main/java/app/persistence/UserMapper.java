package app.persistence;

import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper {

    public static User login(String email, String password, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "select * from users where email=?";

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
                    int zipcode = rs.getInt("zip_code");
                    String phonenumber = rs.getString("phonenumber");
                    return new User(userId, firstName, lastName, email, password, address, phonenumber, zipcode, role);
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
