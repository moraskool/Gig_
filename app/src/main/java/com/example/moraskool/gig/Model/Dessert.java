package com.example.moraskool.gig.Model;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suleiman on 02/03/17.
 */

public class Dessert {

    private String name;
    private String description;
    private String firstLetter;
    private String amount;
    private DatabaseReference mDatabaseReference;

    public Dessert(){

    }
    public Dessert(String name, String description, String amount) {
        this.name = name;
        this.firstLetter = String.valueOf(name.charAt(0));
        this.description = description;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount() {
        this.amount = amount;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    // TODO: 31/07/17 Append the Firebase data from database here
    public static List<Dessert> prepareDesserts(String[] names, String[] descriptions, String[] amounts) {
        List<Dessert> desserts = new ArrayList<>(names.length);


        for (int i = 0; i < names.length; i++) {
            Dessert dessert = new Dessert(names[i], descriptions[i], amounts[i]);
            desserts.add(dessert);
        }


        return desserts;
    }

}
