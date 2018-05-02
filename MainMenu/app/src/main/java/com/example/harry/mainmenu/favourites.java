package com.example.harry.mainmenu;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class favourites extends AppCompatActivity {

    private ArrayList<userRecipes> userMadeRecipes= new ArrayList<userRecipes>();
    private int recipeIdentify = 0;
    private LinearLayout recipeSection;
    private LinearLayout newSection;
    private int recipeToLoadByInt;
    private userRecipes recipeToLoad;
    private static final int LOAD_RECIPE = 2;
    private String inputValues = "";
    private String userName = "";
    private TextView title;
    private int iAmount = 0;
    ProgressBar spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        spinner=(ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);

        Bundle extras = getIntent().getExtras();

        userName= extras.getString("userName");

        getFav();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                spinner.setVisibility(View.GONE);
                setContentView(R.layout.activity_favourites);

                recipeSection = (LinearLayout) findViewById(R.id.mainLinear);
                title = (TextView) findViewById(R.id.favTitle);
                if(inputValues != ""){
                    organise();
                }
            }
        }, 1000);

    }

    public void viewSettings(View view){
        Intent intent = new Intent(this, mainSettings.class);
        startActivity(intent);
    }

    public void backMain(View view){
        finish();
    }

    private void getFav(){
        String type = "search_fav";
        connection connections = new connection(this);
        connections.execute(type, userName);
    }

    public void setList(String input){
        inputValues = input;
    }

    private void organise(){

        String[] recipes = inputValues.split("//recipe//");

        for(int i = 0; i < recipes.length; i++ ){

            ArrayList<ingredient> ingredients = new ArrayList<>();

            String[] name = recipes[i].split("//name//");

            String[] ingredient = name[1].split("//ingredient//");

            for(int j = 0; j < ingredient.length - 1; j++){
                ingredient iItem = new ingredient();
                String[] ingredient_name = ingredient[j].split("//ingredientName//");
                String[] ingredient_amount = ingredient_name[1].split("//amount//");
                String unit = ingredient_amount[1].replace("//unit//", "");

                iItem.setIngredientName(ingredient_name[0]);
                iItem.setIngredientAmount(ingredient_amount[0]);
                iItem.setIngredientUnit(unit);

                ingredients.add(iItem);
            }

            ArrayList<task> tasks = new ArrayList<>();

            String[] task = ingredient[ingredient.length - 1].split("//task//");

            for(int j = 0; j < task.length; j++){
                task tItem = new task();
                String[] task_name = task[j].split("//taskName//");
                String[] task_inst = task_name[1].split("//instruction//");
                String[] task_sec = task_inst[1].split("//seconds//");
                String[] task_min = task_sec[1].split("//minutes//");
                String hour = task_min[1].replace("//hours//", "");

                ArrayList<Integer> time = new ArrayList<>();

                time.add(Integer.parseInt(hour));
                time.add(Integer.parseInt(task_min[0]));
                time.add(Integer.parseInt(task_sec[0]));

                tItem.setTaskName(task_name[0]);
                tItem.setInstructions(task_inst[0]);

                tItem.setTimeValues(time);

                tasks.add(tItem);
            }

            userRecipes tempRecipe = new userRecipes();

            tempRecipe.setName(name[0]);
            tempRecipe.setIngredients(ingredients);
            tempRecipe.setRecipeTasks(tasks);
            tempRecipe.setRecipeImage(null);

            userMadeRecipes.add(tempRecipe);

            createRecipeSection(tempRecipe);

            iAmount++;
        }
        if(iAmount == 1){
            title.setText(Integer.toString(iAmount) + " Item in Favourites");
        }
        else {
            title.setText(Integer.toString(iAmount) + " Items in Favourites");
        }
    }

    private void createRecipeSection(userRecipes newRecipeView){
        LinearLayout.LayoutParams lParamsLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 350);

        LinearLayout newLayout = new LinearLayout(this);

        newLayout.setOrientation(LinearLayout.HORIZONTAL);
        newLayout.setLayoutParams(lParamsLayout);
        newLayout.setBackgroundResource(R.drawable.edit_text_background);
        newLayout.setId(recipeIdentify);

        newLayout.setOnClickListener(loadRecipe);

        recipeSection.addView(newLayout);

        newSection = (LinearLayout) findViewById(recipeIdentify);

        int taskHour = 0;
        int taskMin = 0;
        int taskSec = 0;

        for(task t : newRecipeView.getRecipeTasks()){
            taskHour = taskHour + t.getTimeValues().get(0);
            taskMin = taskMin + t.getTimeValues().get(1);
            taskSec = taskSec + t.getTimeValues().get(2);
        }

        if(taskSec / 60 >= 1){
            taskMin = taskMin + (int)taskSec / 60;
            taskSec = taskSec % 60;
        }

        if(taskMin / 60 >= 1){
            taskHour = taskHour + (int) taskMin / 60;
            taskMin = taskMin % 60;
        }

        String time = "\nDuration: ";

        if(taskHour != 0) {
            time += Integer.toString(taskHour) + " Hours, ";
        }
        if(taskMin != 0){
            time += Integer.toString(taskMin) + " Minutes, ";
        }
        if(taskSec != 0){
            time += Integer.toString(taskSec) + " Seconds";
        }

        recipeIdentify = recipeIdentify + 1;

        LinearLayout.LayoutParams lParamsText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lParamsText.setMargins(10,280,0,10);
        TextView newTxtView = new TextView(this);
        newTxtView.setLayoutParams(lParamsText);
        newTxtView.setEms(10);
        newTxtView.setTextColor(getResources().getColor(R.color.black));
        newTxtView.setEllipsize(TextUtils.TruncateAt.START);
        newTxtView.setPadding(0,0,0,0);
        newTxtView.setTextSize(12);
        newTxtView.setText(newRecipeView.getName() + time);

        newSection.addView(newTxtView);

    }

    View.OnClickListener loadRecipe = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            recipeToLoadByInt = view.getId();


            recipeToLoad = userMadeRecipes.get(recipeToLoadByInt);

            Intent i = new Intent(view.getContext(), recipe.class);

            i.putExtra("userName", userName);
            i.putExtra("ingredients", recipeToLoad.getIngredients());
            i.putExtra("tasks", recipeToLoad.getRecipeTasks());
            i.putExtra("recipeName", recipeToLoad.getName());
            i.putExtra("mode", "0");

            startActivityForResult(i, LOAD_RECIPE);
        }
    };
}
