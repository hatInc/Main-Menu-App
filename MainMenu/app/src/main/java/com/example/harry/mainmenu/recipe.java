package com.example.harry.mainmenu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class recipe extends AppCompatActivity {

    private ImageView displayImage;
    private TextView tvQuickInfo;
    private ArrayList<ingredient> ingredientsList;
    private ArrayList<task> taskList;
    private TextView tvIngredients;
    private TextView tvTasks;
    private LinearLayout iLayout;
    private LinearLayout tLayout;
    private int taskHour = 0;
    private int taskMin = 0;
    private int taskSec = 0;
    private String recipeName = "";
    private String userName = "";
    private String mode = "";
    private String id = "";
    private ImageView edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        displayImage = (ImageView) findViewById(R.id.tempImage);
        tvQuickInfo = (TextView) findViewById(R.id.quickInfo);
        tvIngredients = (TextView) findViewById(R.id.ingredientsTitle);
        tvTasks = (TextView) findViewById(R.id.taskTitle);
        iLayout = (LinearLayout) findViewById(R.id.ingredientLayout);
        tLayout = (LinearLayout) findViewById(R.id.taskLayout);
        edit = (ImageView) findViewById(R.id.edit);

        ingredientsList = (ArrayList<ingredient>) getIntent().getSerializableExtra("ingredients");
        taskList = (ArrayList<task>) getIntent().getSerializableExtra("tasks");
        recipeName = getIntent().getStringExtra("recipeName");
        userName = getIntent().getStringExtra("userName");
        id = getIntent().getStringExtra("id");

        mode = getIntent().getStringExtra("mode");

        if(mode.contains("0")){
            edit.setImageResource(0);
            edit.setClickable(false);
        }
        else{
            edit.setImageResource(R.drawable.addbutton);
            edit.setClickable(true);
        }

        for(ingredient i : ingredientsList){
            iLayout.addView(createViewBox(i.getIngredientName(), " \nAmount: " + i.getIngredientAmount() + " " + i.getIngredientUnit(), 0));
        }

        for(task t : taskList){

            String task = "\n" + t.getInstructions() + "\nTime:";

            if(t.getTimeValues().get(0) > 0){
                if(t.getTimeValues().get(0) == 1){
                    task += " " + Integer.toString(t.getTimeValues().get(2)) + " Hour";
                }
                else {
                    task += " " + Integer.toString(t.getTimeValues().get(2)) + " Hours";
                }
            }
            if(t.getTimeValues().get(1) > 0){
                if(t.getTimeValues().get(1) == 1){
                    task += " " + Integer.toString(t.getTimeValues().get(1)) + " Min";
                }
                else {
                    task += " " + Integer.toString(t.getTimeValues().get(1)) + " Mins";
                }
            }
            if(t.getTimeValues().get(2) > 0){
                if(t.getTimeValues().get(2) == 1){
                    task += " " + Integer.toString(t.getTimeValues().get(2)) + " Sec";
                }
                else {
                    task += " " + Integer.toString(t.getTimeValues().get(2)) + " Secs";
                }
            }

            tLayout.addView(createViewBox("Instruction : " + t.getTaskName(), task, 1));

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

        tvQuickInfo.setText(recipeName + time);

    }

    public void goToMain(View view){
        finish();
    }

    public void viewTimer(View view){
        Intent intent = new Intent(this, timer.class);
        intent.putExtra("recipeTasks", taskList);
        intent.putExtra("recipeName", recipeName);
        startActivity(intent);
    }

    private TextView createViewBox(String name, String data, int TOrI){
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT); //creates new layout parameters to be used with the new view
        lParams.setMargins(0,0,0,0); //adds margins to the parameters
        TextView newTxtView = new TextView(this); //creates a new text view
        newTxtView.setLayoutParams(lParams); //give the new text view the parameters just made
        newTxtView.setEms(10); //honestly not sure what this does
        newTxtView.setTextColor(getResources().getColor(R.color.black)); //sets the text to black
        newTxtView.setEllipsize(TextUtils.TruncateAt.START); //not sure about this either
        newTxtView.setGravity(Gravity.CENTER); //moves the text to the center of the box
        newTxtView.setBackgroundResource(R.drawable.edit_text_background); //set the background to be the base border backgroud
        newTxtView.setPadding(0,0,0,0); //set the padding to be 0 on all sides
        newTxtView.setTextSize(15); //set the text size
        newTxtView.setText(name + data); //set the text of the view to be the passed in name

        return newTxtView; //return the new view
    }

    public void addBasket(View v){

        String ingredients = "";

        for(ingredient i : ingredientsList){
            ingredients += i.getIngredientName() + "//Name//" + i.getIngredientAmount() +
                        "//Amount//" + i.getIngredientUnit() + "//Unit////Ingredients//";
        }

        String type = "add_Basket";
        connection connections = new connection(this);
        connections.execute(type, userName, ingredients);
    }

    public void addFav(View v){

        String type = "add_fav";
        connection connections = new connection(this);
        connections.execute(type, userName, recipeName);
    }

    public void goToEdit(View v){
        Intent i = new Intent(this, maker.class);
        i.putExtra("recipeName", recipeName);
        i.putExtra("ingredients", ingredientsList);
        i.putExtra("tasks", taskList);
        i.putExtra("userName", userName);
        i.putExtra("id", id);
        i.putExtra("mode", "1");
        startActivity(i);
    }
}
