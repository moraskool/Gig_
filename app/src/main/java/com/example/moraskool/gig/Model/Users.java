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

    public Users(String uid, String Name, String email, Dessert dessert ) {
        this.Name = Name;
        this.email = email;
        this.Uid = uid;
        this.dessert = dessert;
    }

    public String getName() {
        return Name;
    }

    public void setName(String user) {
        this.Name = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public Dessert getDessert() {
        return dessert;
    }

    public void setDessert(Dessert dessert) {
        this.dessert = dessert;
    }
}
