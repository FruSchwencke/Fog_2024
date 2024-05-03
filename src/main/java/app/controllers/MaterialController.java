package app.controllers;

import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.MaterialMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class MaterialController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("addmaterial", ctx -> ctx.render("addmaterial"));
        app.post("addmaterial", ctx -> addMaterial(ctx, connectionPool));
    }
    public static void addMaterial (Context ctx, ConnectionPool connectionPool){
    String name = ctx.formParam("name");
    String description = ctx.formParam("description");
    double price = Double.parseDouble(ctx.formParam("price"));
    int unitId = Integer.parseInt(ctx.formParam("unit"));
        String widthParam = ctx.formParam("width");
        String lengthParam = ctx.formParam("length");
        String heightParam = ctx.formParam("height");

        int width = 0;
        int length = 0;
        int height = 0;

        if (widthParam != null && !widthParam.isEmpty()) {
            width = Integer.parseInt(widthParam);
        }

        if (lengthParam != null && !lengthParam.isEmpty()) {
            length = Integer.parseInt(lengthParam);
        }

        if (heightParam != null && !heightParam.isEmpty()) {
            height = Integer.parseInt(heightParam);
        }


        try {
        MaterialMapper.addMaterial(name,description,price,unitId, width,length,height,connectionPool);
        ctx.attribute("message", "dit materiale er nu oprettet i databasen");
        ctx.render("addmaterial.html");

    } catch (DatabaseException | NumberFormatException e) {

        ctx.attribute("message", "dit materiale blev desværre ikke oprettet" + e.getMessage());
        ctx.render("addmaterial.html");
    }
    }


}