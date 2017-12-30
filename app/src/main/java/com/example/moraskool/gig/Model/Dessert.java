package com.example.moraskool.gig.Model;

/**
 * Created by Suleiman on 02/03/17.
 */

public class Dessert {

    private String name;
    private String description;
    private String firstLetter;
    private String amount;

    public Dessert(String name, String description, String amount) {
        this.name = name;
        this.firstLetter = String.valueOf(name.charAt(0));
        this.description = description;
        this.amount = amount;
    }
   public Dessert() {

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
        //smart ass move! *_* but not quite u_u
        return  amount ;
    }

    public void setAmount(String amount) {
         this.amount =  " $ " + amount;
    }

    public String getFirstLetter() {
        return firstLetter.toUpperCase();
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

}
