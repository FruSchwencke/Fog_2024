package app.controllers;

import app.entities.Order;
import app.entities.User;
import java.sql.SQLException;
import java.util.List;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;


public class UserController {


    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("createuser", ctx -> ctx.render("createuser"));
        app.post("createuser", ctx -> createUser(ctx, connectionPool));
      app.post("login", ctx -> login(ctx, connectionPool));
        app.get("login", ctx -> ctx.render("login"));
        app.get("logout", ctx -> logout(ctx));
        app.get("salesperson", ctx -> ctx.render("salesperson_page"));



    }

    //made the email validate strict with use of regex.
    private static boolean emailValidate(String email) {
        String emailPattern ="^(?=.{1,64}@)[A-Za-z0-9_æøåÆØÅ-]+(\\.[A-Za-z0-9_æøåÆØÅ-]+)*@[A-Za-z0-9_æøåÆØÅ-]+(\\.[A-Za-z0-9_æøåÆØÅ-]+)*(\\.[A-Za-z]{2,})$";
        return email.matches(emailPattern);
    }

    //name validate, that only allow letters, and the lenght has to be more than 2
    private static boolean nameValidate (String name){
        String danishLetters = "^[a-zA-ZæøåÆØÅ]+$";
        return name.matches(danishLetters) && name.length() >=2;
    }

    private static void createUser(Context ctx, ConnectionPool connectionPool) {
        String email = ctx.formParam("email");
        String password1 = ctx.formParam("password1");
        String password2 = ctx.formParam("password2");
        String address = ctx.formParam("address");
        String firstName = ctx.formParam("first_name");
        String lastName = ctx.formParam("last_name");
        String zipcode = ctx.formParam("zip_code");
        String phoneNumber = ctx.formParam("phonenumber");

        //error handling if any param is empty
        if (email == null || email.isEmpty() || password1 == null || password1.isEmpty() ||
                password2 == null || password2.isEmpty() || address == null || address.isEmpty() ||
                firstName == null || firstName.isEmpty() || lastName == null || lastName.isEmpty() ||
                phoneNumber == null || phoneNumber.isEmpty() || zipcode == null || zipcode.isEmpty()) {
            ctx.attribute("message", "alle felter skal være udfyldt");
            ctx.render("createuser.html");
            return;
        }

        //firsName must be atleast 2 characters and only letters
        if (!nameValidate(firstName)) {
            ctx.attribute("message", "kun bogstaver og på minimum 2 langt, prøv igen");
            ctx.render("createuser.html");
            return;
        }
        //Lastname must be atleast 2 characters and only letters
        if (!nameValidate(lastName)) {
            ctx.attribute("message", "kun bogstaver og på minimum 2 langt, prøv igen");
            ctx.render("createuser.html");
            return;
        }

        //strict email validation
        if (!emailValidate(email)) {
            ctx.attribute("message", "Email du har prøvet at oprette er ugyldig");
            ctx.render("createuser.html");
            return;
        }

        //checking if zipcode exist in the table
        try {

            if (!UserMapper.checkZipCode(zipcode, connectionPool)) {
                ctx.attribute("message", "dit postnummer findes ikke i Danmark");
                ctx.render("createuser.html");
                return;
            }
        } catch (DatabaseException e) {
            ctx.attribute("message", "der opstod en fejl under kontrol af postnummer");
            ctx.render("createuser.html");
            return;
        }

        //check password for atleast 8 characters
        if (password1.length() < 8) {
            ctx.attribute("message", "Password skal være mindst 8 tegn langt");
            ctx.render("createuser.html");
            return;
        }
        // Check password for at least one capital letter and one number
        // using Regex (a pattern (or filter) that describes a set of strings that matches the pattern)  In other words, a regex accepts a certain set of strings and rejects the rest.
        if (!password1.matches(".*[A-ZÆØÅ].*") || !password1.matches(".*\\d.*")) {
            ctx.attribute("message", "Password skal indeholde mindst ét stor bogstav og mindst ét tal");
            ctx.render("createuser.html");
            return;
        }

        if (password1.equals(password2)) {
            try {
                UserMapper.createuser(email, password1, firstName, lastName, phoneNumber, address, zipcode, connectionPool);
                ctx.sessionAttribute("message", "du er hermed oprettet med " + email + ". Nu skal du logge på.");
                ctx.render("login.html");

            } catch (DatabaseException e) {
                ctx.attribute("message", "En bruger med den mail eller telefonnummer findes allerede, prøv igen, eller log ind");
                ctx.render("createuser.html");
            }


        } else {
            ctx.attribute("message", "dine to passwords matcher ikke! prøv igen");
            ctx.render("createuser.html");
        }

    }
    private static void login(Context ctx, ConnectionPool connectionPool) {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        try {
            User user = UserMapper.login(email, password, connectionPool);
            if (user.getRole() == 1) {
                ctx.sessionAttribute("currentUser", user);
                ctx.render("customer_page.html");
            } else {
                List<Order> allOrdersList = OrderMapper.getAllOrders(connectionPool);
                ctx.attribute("allOrdersList", allOrdersList);
               ctx.sessionAttribute("currentUser", user);
                ctx.render("salesperson_page.html");
            }
        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("login.html");

        }

    }
    private static void logout(Context ctx)
    {
        ctx.req().getSession().invalidate();
        ctx.redirect("/");
    }

}
