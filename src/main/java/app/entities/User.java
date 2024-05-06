package app.entities;

public class User {

private int userId;
private String firstName;
private String lastName;
private String email;
private String password;
private String address;
private String phonenumber;

private String zipcode;

private String city;
private int zipId;
private int role;

    public User(int userID, String firstName, String lastName, String email, String password, String address, String phonenumber, String zipcode, int role) {
        this.userId = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.address = address;
        this.phonenumber = phonenumber;
        this.zipcode = zipcode;
        this.role = role;
    }

    public User(String firstName, String lastName, String email, String address, String phonenumber, String zipcode, String city) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.zipcode = zipcode;
        this.city = city;
        this.phonenumber = phonenumber;
    }

    public int getRole() {
        return role;
    }


    public int getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getCity() {
        return city;
    }
}


