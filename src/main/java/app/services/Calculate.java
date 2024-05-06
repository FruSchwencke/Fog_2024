package app.services;

import app.entities.Material;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.MaterialMapper;

public class Calculate {


    public static void calcPost(int length, ConnectionPool connectionPool){

        //Get material

        int materialId = 1601;
        try {
            Material material = MaterialMapper.getMaterialById(1601, connectionPool);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }

        // Calculate posts





    }


}
