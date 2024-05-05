package app.controllers;

import app.entities.Order;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.sql.SQLException;
import java.util.List;

    public class OrderController {
        public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
            app.get("/salesperson", ctx -> getAllOrders(ctx, connectionPool));
            app.get("/customize", ctx -> ctx.render("customize_page.html"));
            app.post("/customize", ctx -> customizeCarportRoute(ctx, connectionPool));

        }

        private static void getAllOrders(Context ctx, ConnectionPool connectionPool) {
            try {
                List<Order> allOrdersList = OrderMapper.getAllOrders(connectionPool);
                ctx.attribute("allOrdersList", allOrdersList);
                ctx.render("salesperson_page.html");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


        public static void customizeCarportRoute(Context ctx, ConnectionPool connectionPool) {

            //These parameters represent the choices that the user has to make and the ability to send forward a text with remark or wishes, when customizing a carport
                String width = ctx.queryParam("choose_width");
                String length = ctx.queryParam("choose_length");
                String roof = ctx.queryParam("choose_roof");
                String input = ctx.queryParam("text_input");

            // Collecting the input from the user into at list
            // TODO: check if data is collected else return a error message
            Order order = new Order(Integer.parseInt(width),Integer.parseInt(length),input);


            //


            System.out.println("Der er hul igennem");
        };


    }


