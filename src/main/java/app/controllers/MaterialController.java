package app.controllers;

import app.entities.Material;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.MaterialMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class MaterialController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("addmaterial", ctx -> ctx.render("addmaterial"));
        app.post("addmaterial", ctx -> addMaterial(ctx, connectionPool));
        app.get("editMaterial", ctx -> ctx.render("editMaterial"));
        app.post("findMaterial", ctx -> findMaterial(ctx, connectionPool));
        app.post("updateMaterial", ctx -> updateMaterial(ctx, connectionPool));
        app.get("company_mat_list", ctx -> ctx.render("company_mat_list"));
        app.post("company_mat_list", ctx -> companyMatList(ctx, connectionPool));
    }

    public static void addMaterial(Context ctx, ConnectionPool connectionPool) {
        String name = ctx.formParam("name");
        String description = ctx.formParam("description");

        // Checking if name and description are empty or null
        if (name == null || name.isEmpty() || description == null || description.isEmpty()) {
            ctx.attribute("message", "de 4 øverste felter skal udfyldes.");
            ctx.render("addmaterial.html");
            return; // Exit the method if empty or null
        }

        //setting default value
        double price;
        int unitId;
        // handling excptions
        try {
            price = Double.parseDouble(ctx.formParam("price"));
            unitId = Integer.parseInt(ctx.formParam("unit"));
            if (price < 0) {
                ctx.attribute("message", "Prisen kan ikke være minus");
                ctx.render("addmaterial.html");
                return;
            }
        } catch (NumberFormatException e) {
            ctx.attribute("message", "de 4 øverste felter skal udfyldes" + e.getMessage());
            ctx.render("addmaterial.html");
            return;
        }

        String widthParam = ctx.formParam("width");
        String lengthParam = ctx.formParam("length");
        String heightParam = ctx.formParam("height");


        int width = 0;
        int length = 0;
        int height = 0;
        // set 0 in database if nothing is assigned in formparam
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
            MaterialMapper.addMaterial(name, description, price, unitId, width, length, height, connectionPool);
            ctx.attribute("message", "dit materiale er nu oprettet i databasen");
            ctx.render("addmaterial.html");

        } catch (DatabaseException | NumberFormatException e) {

            ctx.attribute("message", "dit materiale blev desværre ikke oprettet" + e.getMessage());
            ctx.render("addmaterial.html");
        }
    }

    private static void findMaterial(Context ctx, ConnectionPool connectionPool) {

        try {
            int matrialId = Integer.parseInt(ctx.formParam("material_id"));
            Material material = MaterialMapper.findMaterialForUpdateMaterial(matrialId, connectionPool);
            if (material != null) {
                ctx.attribute("material", material);
                ctx.render("editMaterial.html");
            } else {
                ctx.attribute("message", "Materiale eksistere ikke");
                ctx.render("editMaterial.html");
            }
        } catch (DatabaseException | NumberFormatException e) {
            ctx.attribute("message", "Feltet skal udfyldes med et nummer");
            ctx.render("editMaterial.html");
        }
    }

    private static void updateMaterial(Context ctx, ConnectionPool connectionPool)  {

        try {
            int materialId = Integer.parseInt(ctx.formParam("material_id"));
            String name = ctx.formParam("name");
            String description = ctx.formParam("description");
            double price = Double.parseDouble(ctx.formParam("price"));

            MaterialMapper.updateMaterial(materialId, name, description, price, connectionPool);
            ctx.attribute("message1", "din opdatering er nu gennemført");
            ctx.render("editMaterial.html");
        } catch (DatabaseException | NumberFormatException e) {
            ctx.attribute("message1", "udfyld alle felter for at undgå fejl");
            ctx.render("editMaterial.html");
        }
    }

    private static void companyMatList (Context ctx, ConnectionPool connectionPool){
        try {

            List<Material> materials = MaterialMapper.companyMaterialList( connectionPool);

                ctx.attribute("materials",materials);
                ctx.render("company_mat_list.html");
            } catch (DatabaseException e)
        {
            ctx.attribute("message", "Der er problemer med at få listen fra databasen");
            ctx.render("salesperson_page.html");
        }
    }
}
