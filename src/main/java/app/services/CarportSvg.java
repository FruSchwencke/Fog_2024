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
        postList.add(new Material(1,"post","lala",1,1,1,1,1,4));

        List<Material> beamList = new ArrayList<>();
        beamList.add(new Material(1,"beam","lala",1,1,1,1,1,2));

        List<Material> rafterList = new ArrayList<>();
        rafterList.add(new Material(1,"rafter","lala",1,1,1,1,1,5));

        List<Material> sternList = new ArrayList<>();
        sternList.add(new Material(1,"rafter","lala",1,1,1,1,1,4));

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
        int x = 120;
        int y = 60;
        double offsetW1 = 35;
        double offsetL1 = 110;

        for (int i = 0; i < (posts.get(0).getQuantity() / 2); i++) {
            for (int j = 0; j < 2; j++) {
                svg.addRectangle(x + (length * i) + offsetL1 - (15 * 0.5),y + (width * j) + offsetW1 - (15 * 0.5),15,15,"stroke-width:1px; stroke:#000000; fill:grey");
                svg.addN();
            }
        }
    }


    //REMMER
    public static void drawBeams(double length, double width, List<Material> beams){

        int x = 120;
        int y = 60;
        double offsetW1 = 35;
        double offsetL1 = 110;

        for (int i = 0; i < beams.get(0).getQuantity(); i++) {
            svg.addRectangle(x + offsetL1, y + (width * i) + offsetW1, 1.95 , 450, "stroke-width:1px; stroke:#000000; fill:lightgray:");
            svg.addN();
        }
    }

    //SPÆR
    public static void drawRafters(double length, double width, List<Material> rafters){

        int x = 120;
        double y = 87.5;
        double offsetW1 = 45;
        double offsetL1 = 120;
        double lengthBetween = 35;

        for (int i = 0; i < rafters.get(0).getQuantity(); i++) {
                svg.addRectangle((x+(lengthBetween*i))+offsetL1, y+9, 250, 1.95, "stroke-width:1px; stroke:#000000; fill:purple;");
                svg.addN();
        }
    }


    //STERN
    public static void drawSterns(double length, double width, List<Material> sterns){
        int x = 120;
        int y = 60;
        double offsetW1 = 35;
        double offsetL1 = 110;

        for (int i = 0; i < sterns.get(0).getQuantity(); i++) {

                svg.addRectangle(x + offsetL1, y + offsetW1, 450, 1.95, "stroke-width:1px; stroke:#000000; fill:green");
                svg.addRectangle(x + offsetW1, y + offsetL1, 1.95, 250, "stroke-width:1px; stroke:#000000; fill:red");
                svg.addN();

        }

    }

    public static void drawCarport(double length, double width, List<Material> postList, List<Material> rafterList, List <Material> sternList, List <Material> beamList){


            drawPosts(length, width,postList);


    }


}
