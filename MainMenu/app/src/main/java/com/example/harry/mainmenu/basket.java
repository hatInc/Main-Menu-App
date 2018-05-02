package com.example.harry.mainmenu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class basket extends AppCompatActivity {

    private String userName = "";
    String valueInput = "";
    ArrayList<ingredient> ingredients = new ArrayList<>();
    int ingredientIdentify = 0;
    int ingredientToLoadByInt;
    ingredient ingredientToLoad;
    LinearLayout ingredientSection;
    LinearLayout newSection;
    TextView title;
    int iAmount = 0;
    ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_loading_screen);

        spinner=(ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);

        Bundle extras = getIntent().getExtras();

        userName= extras.getString("userName");

        getRandom();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                spinner.setVisibility(View.GONE);
                setContentView(R.layout.activity_basket);

                ingredientSection = (LinearLayout) findViewById(R.id.mainLinear);
                title = (TextView) findViewById(R.id.basketTitle);

                if(valueInput != ""){
                    organise();
                }
            }
        }, 1000);

    }

    public void backMain(View view) {
        finish();
    }

    public void viewSettings(View view){
        Intent intent = new Intent(this, mainSettings.class);
        startActivity(intent);
    }

    private void getRandom(){
        String type = "search_basket";
        connection connections = new connection(this);
        connections.execute(type, userName);
    }

    public void setValue(String input){
        valueInput = input;
    }

    private void organise() {

        String[] recipes = valueInput.split("//Ingredient//");

        for (int i = 0; i < recipes.length; i++) {

            String[] name = recipes[i].split("//Name//");
            String[] amount = name[1].split("//Amount//");
            String unit = amount[1].replace("//Unit//", "");

            ingredient iItem = new ingredient();
            iItem.setIngredientName(name[0]);
            iItem.setIngredientAmount(amount[0]);
            iItem.setIngredientUnit(unit);

            ingredients.add(iItem);

            createIngredientSection(iItem);
            iAmount++;
        }
        if(iAmount == 1){
            title.setText(Integer.toString(iAmount) + " Item in Basket");
        }
        else {
            title.setText(Integer.toString(iAmount) + " Items in Basket");
        }
    }

    private void createIngredientSection(ingredient newIngredientView){
        LinearLayout.LayoutParams lParamsLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);

        LinearLayout newLayout = new LinearLayout(this);

        newLayout.setOrientation(LinearLayout.HORIZONTAL);
        newLayout.setLayoutParams(lParamsLayout);
        newLayout.setBackgroundResource(R.drawable.edit_text_background);
        newLayout.setId(ingredientIdentify);

        newLayout.setOnClickListener(loadRecipe);

        ingredientSection.addView(newLayout);

        newSection = (LinearLayout) findViewById(ingredientIdentify);

        ingredientIdentify = ingredientIdentify + 1;

        LinearLayout.LayoutParams lParamsText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lParamsText.setMargins(10,5,0,10);
        TextView newTxtView = new TextView(this);
        newTxtView.setLayoutParams(lParamsText);
        newTxtView.setEms(10);
        newTxtView.setTextColor(getResources().getColor(R.color.black));
        newTxtView.setEllipsize(TextUtils.TruncateAt.START);
        newTxtView.setGravity(Gravity.CENTER);
        newTxtView.setTextSize(15);
        newTxtView.setPadding(0,0,0,0);
        newTxtView.setText(newIngredientView.getIngredientName() + "\nAmount: " + newIngredientView.getIngredientAmount() + " " + newIngredientView.getIngredientUnit());

        newSection.addView(newTxtView);

    }

    View.OnClickListener loadRecipe = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            ingredientToLoadByInt = view.getId();

            ingredientToLoad = ingredients.get(ingredientToLoadByInt);

            AlertDialog alertDialog = new AlertDialog.Builder(basket.this).create();
            alertDialog.setTitle("Would you like to remove this ingredient?");
            alertDialog.setMessage(ingredientToLoad.getIngredientName() + " " +
                    ingredientToLoad.getIngredientAmount() + " " +
                    ingredientToLoad.getIngredientUnit());

            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Delete",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            deleteIngredient(ingredientToLoad.getIngredientName() + "//Name//" +
                                    ingredientToLoad.getIngredientAmount() + "//Amount//" +
                                    ingredientToLoad.getIngredientUnit() + "//Unit////Ingredient//");

                            setContentView(R.layout.activity_loading_screen);

                            spinner=(ProgressBar)findViewById(R.id.progressBar);
                            spinner.setVisibility(View.VISIBLE);

                            getRandom();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    spinner.setVisibility(View.GONE);
                                    setContentView(R.layout.activity_basket);

                                    ingredientSection = (LinearLayout) findViewById(R.id.mainLinear);
                                    title = (TextView) findViewById(R.id.basketTitle);

                                    if(valueInput != ""){
                                        organise();
                                    }
                                }
                            }, 1000);
                        }
                    });
            alertDialog.show();
        }
    };

    public void deleteAll(View v){

        AlertDialog alertDialog = new AlertDialog.Builder(basket.this).create();
        alertDialog.setTitle("Would you like to remove all ingredients from basket?");

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Delete All",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String output = "";
                        for(ingredient i :ingredients){
                            output += i.getIngredientName() + "//Name//" +
                                    i.getIngredientAmount() + "//Amount//" +
                                    i.getIngredientUnit() + "//Unit////Ingredient//";
                        }

                        deleteIngredient(output);

                        setContentView(R.layout.activity_loading_screen);

                        spinner=(ProgressBar)findViewById(R.id.progressBar);
                        spinner.setVisibility(View.VISIBLE);

                        getRandom();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                spinner.setVisibility(View.GONE);
                                setContentView(R.layout.activity_basket);

                                ingredientSection = (LinearLayout) findViewById(R.id.mainLinear);
                                title = (TextView) findViewById(R.id.basketTitle);

                                if(valueInput != ""){
                                    organise();
                                }
                            }
                        }, 1000);
                    }
                });
        alertDialog.show();
    }

    private void deleteIngredient(String input){
        String type = "delete_basket";
        connection connections = new connection(this);
        connections.execute(type, userName, input);
    }
}