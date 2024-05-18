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

        drawPosts(450,250, postList);
        drawBeams(450,250,beamList);

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
                svg.addRectangle(x + (length * i) + offsetL1 - (15 * 0.5),y + (width * j) + offsetW1 - (15 * 0.5),15,15,"stroke-width:1px; stroke:#000000; fill:#ffffff");
                svg.addN();
            }
        }


    }


    //REMMER
    public static void drawBeams(double length, double width, List<Material> beams){

        int x = 120;
        int y = 60;
        double offsetW1 = 35;

        for (int i = 0; i < beams.get(0).getQuantity(); i++) {

            svg.addRectangle(x, y + (width * i) + offsetW1, 19.5 , 3600, "stroke-width:1px; stroke:#000000; fill:#ffffff");
            svg.addN();
        }

    }


    //SPÆR
    public static void drawRafters(int x, int y, double length, double width){

        svg.addRectangle(x,y,length,width,"stroke-width:1px; stroke:#000000; fill:#ffffff");
    }


    //STERN
    public static void drawSterns(int x, int y, double length, double width){

        svg.addRectangle(x,y,length,width,"stroke-width:1px; stroke:#000000; fill:#ffffff");
    }



    public static void drawCarport(double length, double width, List<Material> postList, List<Material> rafterList, List <Material> sternList, List <Material> beamList){


            drawPosts(length, width,postList);


    }


}
