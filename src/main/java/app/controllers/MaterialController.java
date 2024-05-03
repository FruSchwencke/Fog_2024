package app.controllers;

import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class MaterialController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("addmaterial", ctx -> ctx.render("addmaterial"));
        app.post("addmaterial", ctx -> addMaterial(ctx, connectionPool));
    }
    public static void addMaterial (Context ctx, ConnectionPool connectionPool){


    }


}
