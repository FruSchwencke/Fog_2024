package app.services;

import app.entities.Material;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.MaterialMapper;

public class Calculate {


    public static void calculatePosts(int length, int width, ConnectionPool connectionPool){

        //Get material
        int materialId = 1601;
        try {
            Material material = MaterialMapper.getMaterialById(1601, connectionPool);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }


        // Calculate posts based on width
        // offset is the space on the sides of the carport
        int offsetW1 = 350;
        int offsetW2 = 350;
        //offsets are subtracted from maxWidth, so we accurately can calculate if the need for at post in the middle to hold the roof, really is required.
        int maxWidth = 6000 - (offsetW1+offsetW2);
        //the ceil method rounds a number UP to the nearest Integer, and not in either direction like the round() method.
        //adding 1 post at the end, because there are two ends of the width supporting the roof.
        int quantityByWidth = (int) Math.ceil( (double) width - (offsetW1 +offsetW2) / (double) maxWidth +1);



        // Calculate posts based on length

        // offset is the space in the front and back of the carport
        int offsetL1 = 1000;
        int offsetL2 = 300;
        //offsets are not subtracted from the length, it is not as vital for the design, like it is for width.
        int maxlength = 6000;
        //the ceil method rounds a number UP to the nearest Integer, and not in either direction like the round() method.
        //adding 1 post at the end, because there are two sides supporting the roof.
        int quantityByLength = (int) Math.ceil( (double) length - (offsetL1 +offsetL2) / (double) maxlength +1);


        //calculating the quantity of posts needed by multiplying the quantities of posts (length & width) needed with each other.
        int quantityOfPosts = quantityByWidth * quantityByLength;


    }


}
