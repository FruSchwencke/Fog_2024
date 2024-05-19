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
    public static void drawPosts(double length, double width, List<Material> posts){
        double x = 120;
        int y = 60;
        double offsetW1 = 35;
        double offsetL1 = 110;
        double postWidth = (double) posts.get(0).getWidth()/10;
        double postHeight = (double) posts.get(0).getHeight()/10;

        for (int i = 0; i < (posts.get(0).getQuantity() / 2); i++) {
            for (int j = 0; j < 2; j++) {
                svg.addRectangle((x + ((length-offsetL1) * i) + offsetL1) - postHeight,y + ((width - postWidth) * j) + offsetW1 ,postHeight,postWidth,"stroke-width:0.05px; stroke:#000000; fill:grey");
                svg.addN();
            }
        }
    }


    //REMMER
    public static void drawBeams(double length, double width, List<Material> beams){

        int x = 120;
        double y = 60;
        double offsetW1 = 35;
        double offsetL1 = 110;
        double beamHeigth = (double) beams.get(0).getHeight()/10;

        for (int i = 0; i < beams.get(0).getQuantity(); i++) {
            svg.addRectangle(x + offsetL1 -9.7, y + (((width - 9.7)* i))  + (offsetW1 + (9.7 * 0.25)), beamHeigth , length, "stroke-width:0.05px; stroke:#000000; fill:blue;");
            svg.addN();
        }
    }

    //SPÆR
    public static void drawRafters(double length, double width, List<Material> rafters){

        double x = 120;
        double y = 60;
        double rafterHeight = (double) rafters.get(0).getHeight()/10;
        double offsetL1 = 110;
        double offsetW1 = 35;
        int quantity = rafters.get(0).getQuantity();
        double lengthBetween = length / (quantity-1);

        for (int i = 0; i < quantity; i++) {
            if (i == quantity - 1) {
                svg.addRectangle(((x + (lengthBetween * i)) + offsetL1) -(rafterHeight*2), y + offsetW1 , width, rafterHeight, "stroke-width:0.05px; stroke:#000000; fill:purple;");
                svg.addN();
            }else{
                svg.addRectangle((x + (lengthBetween * i)) + offsetL1  - (9.7 * 0.5), y + offsetW1 , width, rafterHeight, "stroke-width:0.05px; stroke:#000000; fill:purple;");
                svg.addN();
            }
        }
    }


    //STERN
    public static void drawSterns(double length, double width, List<Material> sterns){
        int x = 107;
        int y = 60;
        double offsetW1 = 35;
        double offsetL1 = 110.3;
        double sternHeight = (double) sterns.get(0).getHeight()/10;

        for (int i = 0; i < (sterns.get(0).getQuantity() / 2); i++) {
            for (int j = 0; j < 2; j++) {
                svg.addRectangle(x + offsetL1 , y + offsetW1 +((width +(9.7*0.5)) * j), sternHeight, length, "stroke-width:0.05px; stroke:#000000; fill:green");
                svg.addN();
            }
        }

        for (int i = 0; i < (sterns.get(0).getQuantity() / 2); i++) {
            for (int j = 0; j < 2; j++) {
                svg.addRectangle(x + offsetL1 + ((length+ (9.7 * 0.5))*j), y + offsetW1, width, sternHeight, "stroke-width:0.05px; stroke:#000000; fill:red");
                svg.addN();
            }
        }

    }


    public static void drawCarport(double length, double width, List<Material> postList, List<Material> rafterList, List <Material> sternList, List <Material> beamList){


            drawPosts(length, width,postList);


    }


}
