import app.entities.Material;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.services.Calculate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
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

    @Test
    void calculateBeam(){
        Material material = new Material(1514,"45x195 mm. spærtræ ubh.","Remme i sider, sadles ned i stolper",315.0,1,195,6000,45,2);
        List<Material> expected = new ArrayList<>();
        expected.add(material);
        List<Material> result = new ArrayList<>();
        result = Calculate.calculateBeam(6000,6000, connectionPool);

        //assertEquals only works here, because of the equals-override in Material Class. Where it is defined what equals is.
        assertEquals(expected, result);

    }

    @Test
    void calculateBeam2(){
        Material material = new Material(1506,"45x195 mm. spærtræ ubh.","Remme i sider, sadles ned i stolper",189.0,1,195,3600,45,2);
        List<Material> expected = new ArrayList<>();
        expected.add(material);
        List<Material> result = new ArrayList<>();
        result = Calculate.calculateBeam(2400,2400, connectionPool);

        //assertEquals only works here, because of the equals-override in Material Class. Where it is defined what equals is.
        assertEquals(expected, result);

    }



    @Test
    void calculateRafter() throws DatabaseException {
        Material material = new Material(1534,"45x195 mm. spærtræ ubh.","Spær, monteres på rem",315.0,1,195,6000,45,15);
        List<Material> expected = new ArrayList<>();
        expected.add(material);
        List<Material> result = new ArrayList<>();
        result = Calculate.calculateRafter(7800,6000, connectionPool);

        //assertEquals only works here, because of the equals-override in Material Class. Where it is defined what equals is.
        assertEquals(expected, result);


    }


    @Test
    void calcRoofing() throws DatabaseException {
        Material material = new Material(2003, "Plastmo Ecolite blåtonet", "Tagplader monteres på spær", 97.5,1,1090,3600,0,6);
        List<Material> expected = new ArrayList<>();
        expected.add(material);
        List<Material> result = new ArrayList<>();
        result = Calculate.calculateRoof(6000,3100, connectionPool);

        //assertEquals only works here, because of the equals-override in Material Class. Where it is defined what equals is.
        assertEquals(expected, result);

    }


}
