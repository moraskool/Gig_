package com.example.moraskool.gig.Model;

/**
 * Created by moraskool on 01/09/2017.
 */

public class Users {
    private String Name;
    private String email;
    private String Uid;
    private Dessert dessert;

    public Users() {
    }

    public Users(String user) {
        this.Name = user;
    }

    public Users(String uid, String Name, String email, Dessert dessert) {
        this.Name = Name;
        this.email = email;
        this.Uid = uid;
        this.dessert = dessert;
    }
}

