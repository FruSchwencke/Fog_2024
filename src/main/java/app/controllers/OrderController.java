package app.controllers;

import app.entities.Order;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

    public class OrderController {
        public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
            app.get("/salesperson", ctx -> getAllOrders(ctx, connectionPool));
            app.post("/salesperson", ctx -> getAllOrders(ctx, connectionPool));
            app.get("/customize", ctx -> ctx.render("customize_page.html"));
            app.post("/customize", ctx -> customizeCarportRoute(ctx, connectionPool));
            app.get("/order_details/{orderId}", ctx -> getOrderDetails(ctx, connectionPool));
            app.post("/updatetotalprice", ctx -> updateTotalPrice(ctx, connectionPool));
            app.post("/setstatus2", ctx -> setStatus2(ctx, connectionPool));
            app.post("/setStatusAccepted", ctx -> setStatusAccepted(ctx, connectionPool));


        }

        private static void getAllOrders(Context ctx, ConnectionPool connectionPool)  {

            List<Order>allOrdersList = null;
            try {
                allOrdersList = OrderMapper.getAllOrders(connectionPool);
                ctx.attribute("allOrdersList", allOrdersList);
                ctx.render("salesperson_page.html");
            } catch (DatabaseException e) {
                throw new RuntimeException(e); //på anden side.
            }

        }

        private static void getOrderPrUser(Context ctx, ConnectionPool connectionPool)
        {


        }

            private static void getOrderDetails(Context ctx, ConnectionPool connectionPool) {
            try {
                int orderId = Integer.parseInt(ctx.pathParam("orderId"));
                Order orderDetails = OrderMapper.getOrderDetails(orderId, connectionPool);
                User userInformation = OrderMapper.getUserInformation(orderId, connectionPool);

                if (orderDetails != null) {
                    ctx.attribute("orderDetails", orderDetails);
                    ctx.attribute("userInformation", userInformation);
                }

                ctx.render("order_details.html");
            } catch (NumberFormatException | DatabaseException e) {
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
            Order order = new Order(Integer.parseInt(width), Integer.parseInt(length), input);

            //catch orderId from orderMapper...
        }


        public static double calculateMargin(double originalPrice, double newPrice) {

            double originalMargin = 30.0;
            double originalMarginAmount = originalPrice * (originalMargin / 100);
            double newMargin = ((newPrice - originalPrice + originalMarginAmount) / newPrice) * 100;
            return newMargin;
        }

        public static void updateTotalPrice(Context ctx, ConnectionPool connectionPool) {
            try {
                int orderId = Integer.parseInt(ctx.formParam("orderId"));
                double newTotalPrice = Double.parseDouble(ctx.formParam("newTotalPrice"));

                OrderMapper.updateTotalPrice(orderId, newTotalPrice, connectionPool);
                ctx.attribute("messageupdateprice", newTotalPrice + " er nu prisen for ordre nr " + orderId + ".");
                ctx.render("order_details");

            } catch (NumberFormatException e) {


            } catch (DatabaseException e) {
                String s = "Fejl ved opdatering af totalpris: " + e.getMessage();
            }
        }

        private static void setStatus2(Context ctx, ConnectionPool connectionPool) {
            try {
                int orderId = Integer.parseInt(ctx.formParam("orderId"));
                int newStatusId = 2;

                OrderMapper.updateStatus(orderId, newStatusId, connectionPool);
                List<Order>allOrdersList = OrderMapper.getAllOrders(connectionPool);
                ctx.attribute("allOrdersList", allOrdersList);
                ctx.render("salesperson_page.html");

            } catch (NumberFormatException | DatabaseException e) {

                ctx.attribute("message", "Fejl ved opdatering af ordrestatus: " + e.getMessage());

            }
        }

        private static void setStatusAccepted(Context ctx, ConnectionPool connectionPool) {

            Order orderUser = ctx.sessionAttribute("orderUser");

            int newStatusId = 3;
            try {
                int orderId = orderUser.getOrderId();

                OrderMapper.updateStatus(orderId, newStatusId, connectionPool);

                orderUser.setStatusId(newStatusId);


                ctx.sessionAttribute("orderUser", orderUser);


                ctx.render("customer_page.html");
            } catch (DatabaseException e) {

                throw new RuntimeException(e);
            }
        }


        private static void setStatus4(Context ctx, ConnectionPool connectionPool) {
            try {
                int orderId = Integer.parseInt(ctx.formParam("orderId"));
                int newStatusId = 4;

                OrderMapper.updateStatus(orderId, newStatusId, connectionPool);

                ctx.redirect("/salesperson");
            } catch (NumberFormatException | DatabaseException e) {

                ctx.attribute("message", "Fejl ved opdatering af ordrestatus: " + e.getMessage());

            }
        }


    }


