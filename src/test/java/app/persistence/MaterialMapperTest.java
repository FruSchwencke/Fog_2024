package app.persistence;

import app.entities.Material;
import app.exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;
//integrations test for MaterialMapper
class MaterialMapperTest {

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance();

    @BeforeAll static void setupClass() {
        try (Connection connection = connectionPool.getConnection())
        {
            try (Statement stmt = connection.createStatement())
            {
                // The test schema is already created, so we only need to delete/create test tables
                stmt.execute("DROP TABLE IF EXISTS test.materials");
                stmt.execute("DROP SEQUENCE IF EXISTS test.materials_m_id_seq CASCADE;");

                // Create tables as copy of original public schema structure
                stmt.execute("CREATE TABLE test.materials AS (SELECT * from public.materials) WITH NO DATA");

                // Create sequences for auto generating id's for users and orders
                stmt.execute("CREATE SEQUENCE test.materials_m_id_seq");
                stmt.execute("ALTER TABLE test.materials ALTER COLUMN m_id SET DEFAULT nextval('test.materials_m_id_seq')");

            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            fail("Database connection failed");
        }
    }

    @BeforeEach
    void setUp() {
        try (Connection connection = connectionPool.getConnection())
        {
            try (Statement stmt = connection.createStatement())
            {
                // Remove all rows from all tables
                stmt.execute("DELETE FROM test.materials");


                stmt.execute("INSERT INTO test.materials (m_id, name, description, price, unit_id, width, length, height) " +
                        "VALUES  (1, 'jon', 'stor', 12, 1, 10,10,10), (2, 'benny', 'lille', 8,2,20,20,20)");


                // Set sequence to continue from the largest member_id
                stmt.execute("SELECT setval('test.materials_m_id_seq', COALESCE((SELECT MAX(m_id) + 1 FROM test.materials), 1), false)");

            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            fail("Database connection failed");
        }
    }

    @Test
    void addMaterial() {
        try
        {
            // creating an object
            String name = "bræt";
            String description = "stor";
            double price =12;
            int unitId =1;
            int width =10;
            int length= 10;
            int height = 10;

            //adding to the database
             MaterialMapper.addMaterial(name, description,price,unitId,width,length,height,connectionPool);
            //retrieve the material from the database with id
            Material getMaterial = MaterialMapper.getMaterialById(3, connectionPool);

            //creating an expected material, with the same attributes
            Material expectedMaterial = new Material(name, description, price, unitId, width, length, height);
            expectedMaterial.setMaterialId(getMaterial.getMaterialId());

            //assert added and expected
            assertEquals(expectedMaterial, getMaterial);
        }
        catch (DatabaseException e)
        {
            fail("Database fejl: " + e.getMessage());
        }

    }

    @Test
    void updateMaterial() {
        try{
            //arrange - creating an object
            int mId =1;
            String name ="bil";
            String description = "flot";
            double price = 200;
            int unitId =1;
            int width =10;
            int length= 10;
            int height = 10;

            //act - updating the Material
        MaterialMapper.updateMaterial(mId,name,description,price,connectionPool);

            //creating an expected material, with the same attributes
        Material expectedMaterial = new Material(mId,name,description,price,unitId, width, length, height);
        Material dbMaterial = MaterialMapper.getMaterialById(1,connectionPool);
        //assert
        assertEquals(expectedMaterial,dbMaterial);
        }
        catch (DatabaseException e) {
            fail("Database fejl: " + e.getMessage());
        }



    }

    @Test
    void getMaterialById() {
        try
        {
           Material expected = new Material(1, "jon", "stor", 12, 1, 10,10,10);

            Material dbMaterial = MaterialMapper.getMaterialById(1, connectionPool);
            assertEquals(expected, dbMaterial);
        }
        catch (DatabaseException e)
        {
            fail("Database fejl: " + e.getMessage());
        }
    }
}