package com.example.harry.mainmenu;

import java.io.Serializable;

/**
 * Created by Harry on 04/03/2018.
 */

public class ingredient implements Serializable{ //this stores an ingredient and its details
    private String ingredientName = null;
    private String ingredientAmount = null;
    private String ingredientUnit = null;

    //need to add amount
    //maybe add if its vegan/vegetarian etc..

    public String getIngredientName(){return ingredientName;}

    public void setIngredientName(String newName){
        ingredientName = newName;
    }

    public String getIngredientAmount(){return ingredientAmount;}

    public void setIngredientAmount(String newAmount){
        ingredientAmount = newAmount;
    }

    public String getIngredientUnit(){return ingredientUnit;}

    public void setIngredientUnit(String newUnit){
        ingredientUnit = newUnit;
    }
}
