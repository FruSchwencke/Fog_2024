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
    }


