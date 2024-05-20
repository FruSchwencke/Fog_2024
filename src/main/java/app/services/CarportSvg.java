package app.services;

import app.entities.Material;
import app.persistence.ConnectionPool;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CarportSvg {


    private static final SvgTemplates svg = new SvgTemplates();

    public static void main(String[] args) {
        List<Material> postList = new ArrayList<>();
        postList.add(new Material(1,"post","lala",1,1,97,1,97,4));

        List<Material> beamList = new ArrayList<>();
        beamList.add(new Material(1,"beam","lala",1,1,195,4500,45,2));

        List<Material> rafterList = new ArrayList<>();
        rafterList.add(new Material(1,"rafter","lala",1,1,195,1,45,5));

        List<Material> sternList = new ArrayList<>();
        sternList.add(new Material(1,"rafter","lala",1,1,200,1,25,2));

        drawPosts(450,250, postList);
        drawBeams(450,250,beamList);
        drawRafters(450,250,rafterList);
        drawSterns(450,250,sternList);
        drawArrows(450,250);


        String finalSvg = svg.toString();
        try {

            // attach a file to FileWriter
            FileWriter fw
                    = new FileWriter("test.svg");

            // read each character from string and write
            // into FileWriter
            for (int i = 0; i < finalSvg.length(); i++)
                fw.write(finalSvg.charAt(i));

            System.out.println("Successfully written");

            // close the file
            fw.close();
        }
        catch (Exception e) {
            e.getStackTrace();
        }

    }


    //STOLPER
    public static void drawPosts(double carportLength, double carportWidth, List<Material> posts){
        double x = 120;
        int y = 60;
        double offsetW1 = 35;
        double offsetL1 = 110;
        double postWidth = (double) posts.get(0).getWidth()/10;
        double postHeight = (double) posts.get(0).getHeight()/10;

        for (int i = 0; i < (posts.get(0).getQuantity() / 2); i++) {
            for (int j = 0; j < 2; j++) {
                svg.addRectangle((x + ((carportLength-offsetL1) * i) + offsetL1) - postHeight,y + ((carportWidth - postWidth) * j) + offsetW1 ,postHeight,postWidth,"stroke-width:0.05px; stroke:#000000; fill:grey");
                svg.addN();
            }
        }
    }


    //REMMER
    public static void drawBeams(double carportLength, double carportWidth, List<Material> beams){

        int x = 120;
        double y = 60;
        double offsetW1 = 35;
        double offsetL1 = 110;
        double beamHeigth = (double) beams.get(0).getHeight()/10;

        for (int i = 0; i < beams.get(0).getQuantity(); i++) {
            svg.addRectangle(x + offsetL1 -9.7, y + (((carportWidth - 9.7)* i))  + (offsetW1 + (9.7 * 0.25)), beamHeigth , carportLength, "stroke-width:0.05px; stroke:#000000; fill:blue;");
            svg.addN();
        }
    }

    //SPÆR
    public static void drawRafters(double carportLength, double carportWidth, List<Material> rafters){

        double x = 120;
        double y = 60;
        double rafterHeight = (double) rafters.get(0).getHeight()/10;
        double offsetL1 = 110;
        double offsetW1 = 35;
        int quantity = rafters.get(0).getQuantity();
        double lengthBetween = carportLength / (quantity-1);

        for (int i = 0; i < quantity; i++) {
            if (i == quantity - 1) {
                svg.addRectangle(((x + (lengthBetween * i)) + offsetL1) -(rafterHeight*3), y + offsetW1 , carportWidth, rafterHeight, "stroke-width:0.05px; stroke:#000000; fill:purple;");
                svg.addN();
            }else{
                svg.addRectangle((x + (lengthBetween * i)) + offsetL1  - (9.7 * 0.50), y + offsetW1 , carportWidth, rafterHeight, "stroke-width:0.05px; stroke:#000000; fill:purple;");
                svg.addN();
            }
        }
    }


    //STERN
    public static void drawSterns(double carportLength, double carportWidth, List<Material> sterns){
        int x = 120;
        int y = 60;
        double offsetW1 = 35;
        double offsetL1 = 100.3;
        double sternHeight = (double) sterns.get(0).getHeight()/10;

        for (int i = 0; i < (sterns.get(0).getQuantity() / 2); i++) {
            for (int j = 0; j < 2; j++) {
                svg.addRectangle(x + offsetL1 , y + offsetW1 +((carportWidth -(9.7*0.25)) * j), sternHeight, carportLength, "stroke-width:0.05px; stroke:#000000; fill:green");
                svg.addN();
            }
        }

        for (int i = 0; i < (sterns.get(0).getQuantity() / 2); i++) {
            for (int j = 0; j < 2; j++) {
                svg.addRectangle(x + offsetL1 + (carportLength*j), y + offsetW1, carportWidth, sternHeight, "stroke-width:0.05px; stroke:#000000; fill:red");
                svg.addN();
            }
        }

    }

    public static void drawArrows (double carportLength, double carportWidth){
        svg.addArrow();
        svg.addHorizontalText(carportLength);
        svg.addVerticalText(carportWidth);
    }


    public static void drawCarport(double carportLength, double carportWidth, List<Material> postList, List<Material> rafterList, List <Material> sternList, List <Material> beamList){

            drawPosts(carportLength,carportWidth,postList);
            drawBeams(carportLength,carportWidth,beamList);
            drawRafters(carportLength,carportWidth,rafterList);
            drawSterns(carportLength,carportWidth,sternList);
            drawArrows(carportLength,carportWidth);
    }


}
