package app.controllers;

import app.entities.Material;
import app.entities.Order;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.MaterialMapper;
import app.persistence.OrderMapper;
import app.services.Calculate;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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

                int width = Integer.parseInt(ctx.formParam("choose_width"));
                int length = Integer.parseInt(ctx.formParam("choose_length"));
                //String roof = ctx.formParam("choose_roof");
                String input = ctx.formParam("text_input");

            // retrieving the userId
                User currentUser = ctx.sessionAttribute("currentUser");

            try {
                // fetching a specific orderId on currentUser using the OrderMapper method createOrder
                int orderId = OrderMapper.createOrder(currentUser.getUserId(), width, length, input, connectionPool);
                // calling the method that collects all the results from the calculations based on the customer choice
                List<Material> materialList = calculateMaterials(length, width, connectionPool);

                // foreach element on the materialList (which contains results from calculations), we want to create a material line
                materialList.forEach(material -> {
                    try {

                        MaterialMapper.createMaterialLine(material.getQuantity(), orderId, material.getMaterialId(), connectionPool);

                    } catch (DatabaseException e) {
                        throw new RuntimeException(e);
                    }
                });

            } catch (DatabaseException e) {
                throw new RuntimeException(e);
            }
        }

        private static List<Material> calculateMaterials(int carportLength, int carportWidth, ConnectionPool connectionPool) throws DatabaseException {

            // each method for calculation is called and assigned a list
            List<Material> postList = Calculate.calculatePosts(carportLength,carportWidth,connectionPool);
            List<Material> rafterList = Calculate.calculateRafter(carportLength,carportWidth,connectionPool);
            List <Material> roofList = Calculate.calculateRoof(carportLength,carportWidth,connectionPool);
            List <Material> sternList = Calculate.calculateStern(carportLength,carportWidth,connectionPool);
            List <Material> beamList = Calculate.calculateBeam(carportLength,carportWidth,connectionPool);

            // All lists are then combined into one list using Stream.concat
            List<Material> materialList = Stream.concat(Stream.concat(Stream.concat(Stream.concat(postList.stream(), rafterList.stream()), roofList.stream()), sternList.stream()), beamList.stream()).collect(Collectors.toList());

            return materialList;
        }


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
                ctx.attribute("message", "Du har nu accepteret, sælger vil kontakte dig snarest");
                ctx.render("customer_page.html");
            } catch (DatabaseException e) {

                throw new RuntimeException(e);
            }
        }


        private static void setStatusPaid(Context ctx, ConnectionPool connectionPool) {
            try {
                int orderId = Integer.parseInt(ctx.formParam("orderId"));
                int newStatusId = 4;

                OrderMapper.updateStatus(orderId, newStatusId, connectionPool);
                List<Order>allOrdersList = OrderMapper.getAllOrders(connectionPool);
                ctx.attribute("allOrdersList", allOrdersList);
                ctx.render("salesperson_page.html");

            } catch (NumberFormatException | DatabaseException e) {

                ctx.attribute("message", "Fejl ved opdatering af ordrestatus: " + e.getMessage());

            }
        }



    }


