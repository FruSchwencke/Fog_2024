import app.persistence.ConnectionPool;
import app.services.Calculate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculateTest {

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "databasename";
    private static ConnectionPool connectionPool;


    @BeforeAll
    public static void setUpClass(){
        try{
            connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void calculatePosts(){
        assertEquals(4, Calculate.calculatePosts(4000,3000, connectionPool));
    }

}
