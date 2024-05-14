package app.services;
import app.entities.Material;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.MaterialMapper;
import java.util.ArrayList;
import java.util.List;
import static java.lang.Math.ceil;


public class Calculate {

    public static Material newItem(int quantity, int materialId, Material material) {
        return new Material(
                materialId,
                material.getName(),
                material.getDescription(),
                material.getPrice(),
                material.getUnitId(),
                material.getWidth(),
                material.getLength(),
                material.getHeight(),
                quantity);
    }


    //STOLPER
    public static List<Material> calculatePosts(int length, int width, ConnectionPool connectionPool){
        //Get material
        int materialId = 1601;
        Material material = new Material();

        try {

            material = MaterialMapper.getMaterialById(materialId, connectionPool);

        } catch (DatabaseException e) {
            throw new RuntimeException("fejl ved fremskaffelse af materiale fra databasen", e);
        }

        // Calculate posts based on width
        // offset is the space on the sides of the carport
        int offsetW1 = 350;
        int offsetW2 = 350;
        //offsets are subtracted from maxWidth, so we accurately can calculate if the need for at post in the middle to hold the roof, really is required.
        int maxWidth = 6000 - (offsetW1+offsetW2);
        //the ceil method converts a decimal number to the immediate largest Integer, and not in either direction like the round() method.
        //adding 1 post at the end, because there are two ends of the width supporting the roof.
        int quantityByWidth = (int) ceil( (double)(width - (offsetW1 +offsetW2)) / (double) maxWidth +1);



        // Calculate posts based on length
        // offset is the space in the front and back of the carport
        int offsetL1 = 1000;
        int offsetL2 = 300;
        //offsets are not subtracted from the length, it is not as vital for the design, like it is for width.
        int maxlength = 6000;
        //the ceil method converts a decimal number to the immediate largest Integer, and not in either direction like the round() method.
        //adding 1 post at the end, because there are two sides supporting the roof.
        int quantityByLength = (int) ceil( (double) (length - (offsetL1 +offsetL2)) / (double) maxlength +1);


        //calculating the quantity of posts needed by multiplying the quantities of posts (length & width) needed with each other.
        int quantityOfPosts = quantityByWidth * quantityByLength;

        List<Material> materialList = new ArrayList<>();
        material.setQuantity(quantityOfPosts);
        materialList.add(material);

        return materialList;
    }


    //REMME
    public static List<Material> calculateBeam (int length, int width, ConnectionPool connectionPool){
        // Get material from DB
        String description = "Remme i sider, sadles ned i stolper";

        try {
            // creating a materialList based on description
            List<Material> materialList = MaterialMapper.getMaterialByDescription(description, connectionPool);

            // Calculate
            int offsetW1 = 350;
            int offsetW2 = 350;
            int maxWidth = 6000 - (offsetW1 + offsetW2);
            int quantity = (int) ceil((double) (width - (offsetW1 + offsetW2)) / (double) maxWidth) + 1;

            //add the correct lengths
            List<Material> result = new ArrayList<>();
            //iterating over the list of various lengths in reverse order to determine if i is greater the 0, if so, it continues to iterate over the availableLengths
            for (int i = materialList.size() - 1; i > 0; i--) {

                // condition - comparing the length with the value of index i in the availableLengths list
                if ((length) >= materialList.get(i).getLength()) {
                    //if index i is greater or equal to the length, then it's added to the new item list
                    result.add(materialList.get(i));

                    // this ensures that the lengths are ready to be iterated over again, with no changes.
                    length -= materialList.get(i).getLength();

                }
            }
            // this ensures if there's any leftover length of material that wasn't accounted for in the loop processing the various lengths, it adds an item to the result list.
            if (length > 0) {
                result.add(materialList.get(0));
            }

            result.forEach(mat -> mat.setQuantity(quantity));
            return result;

        } catch (DatabaseException e) {
            throw new RuntimeException("fejl ved materialet med den beskrivelse fra databasen",e);
        }
    }


    //SPÆR
    public static List<Material> calculateRafter(int length, int width, ConnectionPool connectionPool) throws DatabaseException {

        // Get materials from database
        String description = "Spær, monteres på rem";
        List<Material> materialList = MaterialMapper.getMaterialByDescription(description, connectionPool);

        // Calculate
        // maxWidth is the largest space allowed between rafters (60 cm) subtracted with a rafters own width (50)
        int maxWidth = 550;
        // then the length of the carport is divided by maxWidth, to find the quantity needed for at specific length
        int quantity = (int) ceil((double) length / (double) maxWidth);

        // Add the correct lengths for the rafters
        List<Material> result = new ArrayList<>();
        // the length of the rafter has to be set as equal to the width of the carport.
        length = width;

        //
        for (int i = materialList.size() - 1; i > 0; i--) {
            if ((length) >= materialList.get(i).getLength()) {
                //if index i is greater or equal to the length, then it's added to the new item list
                result.add(materialList.get(i));


                // this ensures that the lengths are ready to be iterated over again, with no changes.
                length -= materialList.get(i).getLength();
            }
        }

        // this ensures if there's any leftover length of material that wasn't accounted for in the loop processing the various lengths, it adds an item to the result list.
        if (length > 0) {
            result.add(materialList.get(0));

        }

        result.get(0).setQuantity(quantity);

        return result;
    }



    // STERN
    public static List<Material> calculateStern(int carportLength, int carportWidth, ConnectionPool connectionPool) throws DatabaseException {

        // Get materials from database
        String sternSides = "Understernbrædder til siderne";
        List<Material> materialList = MaterialMapper.getMaterialByDescription(sternSides, connectionPool);

        // Get materials from database
        String sternFrontAndBack = "Understernbrædder til for- & bagende";
        List<Material> materialList2 = MaterialMapper.getMaterialByDescription(sternFrontAndBack, connectionPool);

        int quantity;
        boolean done = false;

        // Add the correct lengths
        List<Material> result = new ArrayList<>();

        for (int i = 0 ; i <= materialList.size() -1; i++) {


            if (materialList.get(i).getLength() >= carportLength && !done){

                int itemLength = materialList.get(i).getLength();
                quantity = 2;
                result.add(newItem(quantity, materialList.get(i).getMaterialId(), materialList.get(i)));
                done = true;
            }
        }

        done = false;

        for (int i = 0 ; i <= materialList2.size() -1 ; i++) {

            if (materialList2.get(i).getLength() >= carportWidth && !done){
                int itemLength = materialList2.get(i).getLength();
                quantity = 2;

                result.add(newItem(quantity, materialList2.get(i).getMaterialId(), materialList2.get(i)));
                done = true;
            }
        }
        return result;
    }


    // TAGPLADER
    public static List<Material> calculateRoof(int carportLength, int carportWidth, ConnectionPool connectionPool) throws DatabaseException {

        // Get materials from database
        String description = "Tagplader monteres på spær";
        List<Material> materialList = MaterialMapper.getMaterialByDescription(description, connectionPool);

        // Calculate
        int overlapWidth = 70;
        int quantity;
        boolean done = false;

        // A list for correct lengths
        List<Material> result = new ArrayList<>();

        for (int i = 0 ; i <= materialList.size() -1; i++) {
            //if the length of the material is greater or equal to carportWidth, then find the quantity needed.
            if (materialList.get(i).getLength() >= carportWidth && !done) {

                // how many items is needed to cover the length of the carport.
                int itemWidth = materialList.get(i).getWidth() - overlapWidth;
                quantity = (int) ceil((double) carportLength / (double) itemWidth);

                result.add(newItem(quantity, materialList.get(i).getMaterialId(), materialList.get(i)));


                done = true;

            }
        }

        return result;

    }


}