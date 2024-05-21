package app.controllers;

import app.entities.Material;
import app.entities.Order;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.MaterialMapper;
import app.persistence.OrderMapper;
import app.services.Calculate;
import app.services.CarportSvg;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
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
        app.post("/offermade", ctx -> setStatusOfferMade(ctx, connectionPool));
        app.post("/setStatusAccepted", ctx -> setStatusAccepted(ctx, connectionPool));
        app.post("/setStatusDeclined", ctx -> setStatusDeclined(ctx, connectionPool));
        app.post("/setStatusPaid", ctx -> setStatusPaid(ctx, connectionPool));
        app.post("/setStatusDone", ctx -> setStatusDone(ctx, connectionPool));

    }

    private static void getAllOrders(Context ctx, ConnectionPool connectionPool) {

        List<Order> allOrdersList = null;
        try {
            allOrdersList = OrderMapper.getAllOrders(connectionPool);
            ctx.attribute("allOrdersList", allOrdersList);
            ctx.render("salesperson_page.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", "der var problemer med at oprette listen med alle ordre");
            ctx.render("salesperson_page.html");
        }

    }

    private static void getOrderDetails(Context ctx, ConnectionPool connectionPool) {
        try {
            Locale.setDefault(new Locale("US"));
            int orderId = Integer.parseInt(ctx.pathParam("orderId"));
            Order orderDetails = OrderMapper.getOrderDetails(orderId, connectionPool);
            User userInformation = OrderMapper.getUserInformation(orderId, connectionPool);
            double costPrice = OrderMapper.getCostPrice(orderId, connectionPool);
            double suggestedPrice = calculateSuggestedPrice(costPrice);
            List<Material> orderMaterialList = MaterialMapper.getOrderMaterialList(orderId, connectionPool);
            int carportWidth = orderDetails.getWidth();
            int carportLength = orderDetails.getLength();

            // each method for calculation is called and assigned a list
            List<Material> postList = Calculate.calculatePosts(carportLength, carportWidth, connectionPool);
            List<Material> rafterList = Calculate.calculateRafter(carportLength, carportWidth, connectionPool);
            List<Material> roofList = Calculate.calculateRoof(carportLength, carportWidth, connectionPool);
            List<Material> sternList = Calculate.calculateStern(carportLength, carportWidth, connectionPool);
            List<Material> beamList = Calculate.calculateBeam(carportLength, carportWidth, connectionPool);

            String svg = CarportSvg.drawCarport((double) carportLength/10, (double) carportWidth/10,postList,rafterList,sternList,beamList);

            if (orderDetails != null) {
                ctx.sessionAttribute("orderDetails", orderDetails);
                ctx.sessionAttribute("userInformation", userInformation);
                ctx.sessionAttribute("costPrice", costPrice);
                ctx.sessionAttribute("suggestedPrice", suggestedPrice);
                ctx.sessionAttribute("orderMaterialList", orderMaterialList);
                ctx.sessionAttribute("svgDrawing",svg);
            }

            ctx.render("order_details.html");
        } catch (NumberFormatException | DatabaseException e) {
            ctx.attribute("message", "der var problemer med at fremskaffe ordredetaljerne");
            ctx.render("salesperson_page.html");
        }
    }


        public static void customizeCarportRoute (Context ctx, ConnectionPool connectionPool){
            //These parameters represent the choices that the user has to make and the ability to send forward a text with remark or wishes, when customizing a carport
            int width = Integer.parseInt(ctx.formParam("choose_width"));
            int length = Integer.parseInt(ctx.formParam("choose_length"));
            //String roof = ctx.formParam("choose_roof");
            String input = ctx.formParam("text_input");

            // retrieving the userId
            User currentUser = ctx.sessionAttribute("currentUser");
            try {
                //returning a user if they allready have an order
                 if (OrderMapper.userHasOrder(currentUser.getUserId(), connectionPool)) {
                   Order orderUser = OrderMapper.getOrderPrUser(currentUser.getUserId(), connectionPool);
                     if(orderUser != null){
                         int newStatusId = 1;
                         orderUser.setStatusId(newStatusId);
                     }
                     ctx.sessionAttribute("orderUser", orderUser);
                     ctx.attribute("message", "Du har allerede en ordre, kontakt os gerne hvis du ønsker flere");
                     ctx.render("customer_page.html");
                     return;
                 }
                // fetching a specific orderId on currentUser using the OrderMapper method createOrder
                int orderId = OrderMapper.createOrder(currentUser.getUserId(), width, length, input, connectionPool);
                // calling the method that collects all the results from the calculations based on the customer choice
                List<Material> materialList = calculateMaterials(length, width, connectionPool);

            // foreach element on the materialList (which contains results from calculations), we want to create a material line
            materialList.forEach(material -> {
                try {

                    MaterialMapper.createMaterialLine(material.getQuantity(), orderId, material.getMaterialId(), connectionPool);

                    } catch (DatabaseException e) {
                        ctx.attribute("message", "kunne ikke oprette materiale linje");
                        ctx.render("customize_page");
                    }
                });


        } catch (DatabaseException e) {
            ctx.attribute("message", "kunne ikke oprette en ny ordre, prøv igen");
            ctx.render("customize_page");
        }
        ctx.render("order_confirm_page.html");
    }

        private static List<Material> calculateMaterials ( int carportLength, int carportWidth, ConnectionPool
        connectionPool) throws DatabaseException {

            // each method for calculation is called and assigned a list
            List<Material> postList = Calculate.calculatePosts(carportLength, carportWidth, connectionPool);
            List<Material> rafterList = Calculate.calculateRafter(carportLength, carportWidth, connectionPool);
            List<Material> roofList = Calculate.calculateRoof(carportLength, carportWidth, connectionPool);
            List<Material> sternList = Calculate.calculateStern(carportLength, carportWidth, connectionPool);
            List<Material> beamList = Calculate.calculateBeam(carportLength, carportWidth, connectionPool);


        // All lists are then combined into one list using Stream.concat
        List<Material> materialList = Stream.concat(Stream.concat(Stream.concat(Stream.concat(postList.stream(), rafterList.stream()), roofList.stream()), sternList.stream()), beamList.stream()).collect(Collectors.toList());

        return materialList;
    }

        public static void updateTotalPrice (Context ctx, ConnectionPool connectionPool){
            try {
                int orderId = Integer.parseInt(ctx.formParam("orderId"));
                double newTotalPrice = Double.parseDouble(ctx.formParam("newTotalPrice"));
                double costPrice = OrderMapper.getCostPrice(orderId, connectionPool);


                if (newTotalPrice >= costPrice) {
                    OrderMapper.updateTotalPrice(orderId, newTotalPrice, connectionPool);
                    ctx.attribute("messageUpdatePrice", "Salgsprisen for ordrenummer " + orderId + " er nu " + newTotalPrice + " kr.");
                } else {
                    ctx.attribute("messageUpdatePrice", "Du kan ikke afgive tilbud, som er mindre end indkøbsprisen");
                }
                Order orderDetails = OrderMapper.getOrderDetails(orderId, connectionPool);
                ctx.sessionAttribute("orderDetails", orderDetails);

                ctx.render("order_details.html");

            } catch (DatabaseException | NumberFormatException e) {
                ctx.attribute("message", "Der var udfordringer med at opdatere prisen: " + e.getMessage());
                ctx.render("order_details.html");
            }
    }
        private static void setStatusOfferMade (Context ctx, ConnectionPool connectionPool){
            try {
                int orderId = Integer.parseInt(ctx.formParam("orderId"));
                int newStatusId = 5;

                OrderMapper.updateStatus(orderId, newStatusId, connectionPool);
                List<Order> allOrdersList = OrderMapper.getAllOrders(connectionPool);
                ctx.attribute("allOrdersList", allOrdersList);
                ctx.attribute("messageorder", "Der er nu afsedt tilbud til kunde med ordrenummer" + orderId + ".");
                ctx.render("salesperson_page.html");


        } catch (NumberFormatException | DatabaseException e) {

            ctx.attribute("message", "Fejl ved opdatering af ordrestatus: " + e.getMessage());
            ctx.render("salesperson_page.html");
        }
    }

        private static void setStatusAccepted (Context ctx, ConnectionPool connectionPool){

            Order orderUser = ctx.sessionAttribute("orderUser");
            int newStatusId = 2;

            try {
                int orderId = orderUser.getOrderId();
                OrderMapper.updateStatus(orderId, newStatusId, connectionPool);
                orderUser.setStatus("Tilbud accepteret");


            ctx.sessionAttribute("orderUser", orderUser);
            ctx.attribute("message", "Du har nu accepteret, sælger vil kontakte dig snarest");
            ctx.render("customer_page.html");
        } catch (DatabaseException e) {

                ctx.attribute("message", "der skete en fejl under udførslen, prøv igen");
                ctx.render("customer_page.html");
            }
        }

        private static void setStatusDeclined (Context ctx, ConnectionPool connectionPool){

            Order orderUser = ctx.sessionAttribute("orderUser");
            int newStatusId = 4;

            try {
                int orderId = orderUser.getOrderId();
                OrderMapper.updateStatus(orderId, newStatusId, connectionPool);
                orderUser.setStatus("Tilbud afslået");

                ctx.sessionAttribute("orderUser", orderUser);
                ctx.attribute("message", "Du har nu valgt at afslå tilbuddet på denne ordre. Du er altid velkommen til at kontakte os igen");
                ctx.render("customer_page.html");
            } catch (DatabaseException e) {

                ctx.attribute("message, der skete en fejl under udførslen, prøv igen");
                ctx.render("customer_page.html");
            }

        }
        private static void setStatusPaid (Context ctx, ConnectionPool connectionPool) {
            try {
                int orderId = Integer.parseInt(ctx.formParam("orderId"));
                int newStatusId = 3;

                OrderMapper.updateStatus(orderId, newStatusId, connectionPool);
                List<Order> allOrdersList = OrderMapper.getAllOrders(connectionPool);
                ctx.attribute("allOrdersList", allOrdersList);
                ctx.attribute("messageorder", "Kunde med ordrenummer " + orderId + " har nu betalt.");
                ctx.render("salesperson_page.html");


            } catch (NumberFormatException | DatabaseException e) {

                ctx.attribute("message", "Fejl ved opdatering af ordrestatus: " + e.getMessage());

            }
        }

    public static void setStatusDone(Context ctx, ConnectionPool connectionPool) {
        try {
            List<String> orderIds = ctx.formParams("orderIds");
            if (orderIds != null) {
                for (String orderIdStr : orderIds) {
                    int orderId = Integer.parseInt(orderIdStr);
                    OrderMapper.updateStatus(orderId, 6, connectionPool);
                }
                ctx.attribute("messageorder", "Valgte ordrer er nu flytte til listen for færdigbehandlede ordrer.");
            } else {
                ctx.attribute("messageorder", "Ingen ordrer valgt til opdatering.");
            }
            List<Order> allOrdersList = OrderMapper.getAllOrders(connectionPool);
            ctx.attribute("allOrdersList", allOrdersList);
            ctx.render("salesperson_page.html");
        } catch (DatabaseException | NumberFormatException e) {
            ctx.attribute("message", "Der var problemer med at flytte valgte ordrer" + e.getMessage());
            ctx.render("salesperson_page.html");
        }
    }

    private static double calculateSuggestedPrice(double costPrice) {
        double suggestedPrice = costPrice * 1.30;
        return Math.ceil(suggestedPrice);
    }

}




