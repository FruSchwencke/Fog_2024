package app.controllers;

import app.entities.Material;
import app.entities.Order;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.MaterialMapper;
import app.persistence.OrderMapper;
import app.persistence.UserMapper;
import app.services.Calculate;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.stream.Nodes.collect;

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



    }


