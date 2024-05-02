package app.controllers;

import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.http.Context;

public class UserController {





    private static boolean EmailValidate(String email) {
        return email.contains("@");
    }
    private static void createUser(Context ctx, ConnectionPool connectionPool) {
        String email = ctx.formParam("email");
        String password1 = ctx.formParam("password1");
        String password2 = ctx.formParam("password2");
        String address = ctx.formParam("address");
        String firstName = ctx.formParam("first_name");
        String lastName  =ctx.formParam("last_name");
        int zipcode = Integer.parseInt(ctx.formParam("zip_code"));
        String phoneNumber = ctx.formParam("phonenumber");

        //firsName must be atleast 2 characters
        if (firstName.length()< 2) {
            ctx.attribute("message", "intet dansk navn er på 1 bogstav");
            ctx.render("createuser.html");
            return;
        }
        //Lastname must be atleast 2 characters
        if (lastName.length()< 2) {
            ctx.attribute("message", "intet dansk efternavn er på 1 bogstav");
            ctx.render("createuser.html");
            return;
        }

        //simple email validation
        if (!EmailValidate(email)){
            ctx.attribute("message", "Email skal indholde '@' symbol");
            ctx.render("createuser.html");
            return;
        }

        //zipcode cant be below 0 and cannot be 5 digits or more.
        if (zipcode <= 0 || zipcode >= 4){
            ctx.attribute("message", "Postnummer skal være bornholmsk");
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
        if (!password1.matches(".*[A-Z].*") || !password1.matches(".*\\d.*")) {
            ctx.attribute("message", "Password skal indeholde mindst én stor bogstav og mindst ét tal");
            ctx.render("createuser.html");
            return;
        }

        if(password1.equals(password2)){
            try {
                UserMapper.createuser(email, password1, firstName,lastName,phoneNumber,address,zipcode, connectionPool);
                ctx.sessionAttribute("message", "du er hermed oprettet med " + email + ". Nu skal du logge på.");
                ctx.render("login.html");

            } catch (DatabaseException e) {
                ctx.attribute("message", "dit brugernavn findes allerede, prøv igen, eller log ind");
                ctx.render("createuser.html");
            }


        }else{
            ctx.attribute("message", "dine to passwords matcher ikke! prøv igen");
            ctx.render("createuser.html");
        }

    }
}
