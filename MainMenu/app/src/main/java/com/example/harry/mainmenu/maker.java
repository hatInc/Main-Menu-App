package com.example.harry.mainmenu;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class maker extends AppCompatActivity implements View.OnClickListener {

    private static final int RESULT_LOAD_IMAGE = 1; //return value for loading image from phone
    private static final int STEP_RESULT_POP_UP = 2; //return value for opening up the pop up window to create a step
    private static final int STEP_RESULT_POP_UP_EDIT = 3; //return value for opening up the create step window in edit mode
    private static final int INGREDIENT_RESULT_POP_UP = 4; //return value for opening up the new ingredient window
    private static final int INGREDIENT_RESULT_POP_UP_EDIT = 5; //return value for opening up the new ingredient window in edit mode
    private ImageView imageView;

    private LinearLayout newIngredientLayout;
    private LinearLayout newTaskLayout;
    private Button newStep;
    private Button newIndredient;
    private ArrayList<task> steps = new ArrayList<task>(); //contains all the steps for this recipe
    private ArrayList<ingredient> ingredients = new ArrayList<ingredient>(); //contains all the ingredients for the new recipe
    private int taskIdentifier = 0;
    private int ingredientIdentifier = 0;
    private int editViewNumber;
    private task taskToEdit; //this will be given a specific task object if a task is selected to be edited
    private ingredient ingredientToEdit; // this will be given a specific ingredient object if an ingredient is selected to be edited
    private Button create;
    private ImageView delete;
    private EditText recipeName;
    private Bitmap image = null;
    private ByteArrayOutputStream bytes = new ByteArrayOutputStream(); //this is used in the process the save the image to send between activitys
    private int imageChecker = 0;
    private String mode = "";
    private String id = "";
    ArrayList<ingredient> prevIngredientsList;
    ArrayList<task> prevTaskList;
    String userName = "";
    Uri imgUri;
    String prevName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maker);

        imageView = (ImageView) findViewById(R.id.imgView); //get the image view for the recipe image

        newStep = (Button) findViewById(R.id.addStep); //find the new step button
        newIndredient = (Button) findViewById(R.id.addIngredient); //find the new ingredient button
        create = (Button) findViewById(R.id.createRecipe); //find the create button
        recipeName = (EditText) findViewById(R.id.nameRecipe); //find the recipe name txt view
        delete = (ImageView) findViewById(R.id.delete);

        newIngredientLayout = (LinearLayout) findViewById(R.id.newIngredients); //find the new ingredients layout where new ingredients will be added to
        newTaskLayout = (LinearLayout) findViewById(R.id.newTasks); //find the new tasks layout where new tasks will be displayed

        newIndredient.setOnClickListener(this);
        imageView.setOnClickListener(this);
        newStep.setOnClickListener(this);
        create.setOnClickListener(this);

        userName = getIntent().getStringExtra("userName");
        mode = getIntent().getStringExtra("mode");
        delete.setClickable(false);

        if(mode.contains("1")){
            id = getIntent().getStringExtra("id");
            delete.setClickable(true);
            delete.setImageResource(R.drawable.clear_all);
            editLoad();
            create.setText("Update Data");
        }
    }

    public void deleteRecipe(View v){
        String type = "delete_recipe";

        connection connections = new connection(this);
        connections.execute(type, id);
    }

    private void editLoad(){
        prevIngredientsList = (ArrayList<ingredient>) getIntent().getSerializableExtra("ingredients");
        prevTaskList = (ArrayList<task>) getIntent().getSerializableExtra("tasks");
        prevName = getIntent().getStringExtra("recipeName");

        recipeName.setText(prevName);

        for(ingredient i : prevIngredientsList){
            String info = i.getIngredientName() + " \nAmount: " + i.getIngredientAmount() + " " + i.getIngredientUnit();
            newIngredientLayout.addView(createViewBox(info, 0)); //adds a new ingredient to the ingredient view
            ingredients.add(i); //adds this new ingredient to the list
        }

        for(task t : prevTaskList){

            ArrayList<Integer> values = t.getTimeValues();
            String task = "";
            if(values.get(0) > 0){
                if(values.get(0) == 1){
                    task += " " + Integer.toString(values.get(2)) + " Hour";
                }
                else {
                    task += " " + Integer.toString(values.get(2)) + " Hours";
                }
            }
            if(values.get(1) > 0){
                if(values.get(1) == 1){
                    task += " " + Integer.toString(values.get(1)) + " Min";
                }
                else {
                    task += " " + Integer.toString(values.get(1)) + " Mins";
                }
            }
            if(values.get(2) > 0){
                if(values.get(2) == 1){
                    task += " " + Integer.toString(values.get(2)) + " Sec";
                }
                else {
                    task += " " + Integer.toString(values.get(2)) + " Secs";
                }
            }
            String info = t.getTaskName() + "\n" + t.getInstructions() + "\nTime: " + task;

            newTaskLayout.addView(createViewBox(info, 1));

            steps.add(t); //add the task to the steps array
        }

    }

    public void backMain(View view) {
        finish();
    } //returns the app to the previous screen by just closing this activity

    public void viewSettings(View view) {
        Intent intent = new Intent(this, mainSettings.class);
        startActivity(intent);
    } // loads up the settings activity leaving this activity still running so then we can return to it afterwards

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.imgView: //if the id of what has been clicked is the imgView then run this.
                Intent intentGalley = new Intent(Intent.ACTION_PICK); //new intent to open up the gallery
                intentGalley.setType("image/*"); //set the type to be an image...i think
                startActivityForResult(intentGalley, RESULT_LOAD_IMAGE); //start the activity waiting for a result afterwards
                break;
            case R.id.addIngredient: //if id is addIngredient then run this
                startActivityForResult(new Intent(this, ingredientWindow.class), INGREDIENT_RESULT_POP_UP); //run the popup box for a new ingredient waiting for a result
                break;
            case R.id.addStep: //is id is addStep then run this
                Intent createStepIntent = new Intent(this, createStep.class); //create a new intent that will load the createStep class

                createStepIntent.putExtra("otherSteps", steps); //send the steps to the intent that will be used for dependencies

                startActivityForResult(createStepIntent, STEP_RESULT_POP_UP); //start the activity waiting for a result
                break;
            case R.id.createRecipe: //if id is createRecipe then run this
                if(imageChecker == 0){
                    Context context = getApplicationContext();
                    CharSequence text = "You have not selected an image!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else if(recipeName.getText().toString().length() == 0){
                    Context context = getApplicationContext();
                    CharSequence text = "You have not entered a title";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else if(ingredients.isEmpty()){
                    Context context = getApplicationContext();
                    CharSequence text = "You have not entered any ingredients";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else if(steps.isEmpty()){
                    Context context = getApplicationContext();
                    CharSequence text = "You have not entered any tasks";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else if(mode.contains("1")){

                    //ArrayList<task> temp = organiseTasks();
                    String tasks = "";
                    String items = "";

                    for(task t : steps){
                        String hour = Integer.toString(t.getTimeValues().get(0));
                        String min = Integer.toString(t.getTimeValues().get(1));
                        String sec = Integer.toString(t.getTimeValues().get(2));
                        tasks += t.getTaskName() + "//Name//" + t.getInstructions() + "//Des//"
                                + hour + "//hour//" + min + "//min//" + sec + "//sec//";


                        for(task ti : t.getDependencies()){
                            tasks += ti.getTaskName() + "//";
                        }


                        tasks += "//task//";
                    }

                    for(ingredient i : ingredients){
                        String name = i.getIngredientName();
                        String amount = i.getIngredientAmount();
                        String unit = i.getIngredientUnit();
                        items += name + "//Name//" + amount + "//Amount//"
                                + unit + "//Unit////Ingredient//";
                    }

                    String type = "delete_recipe";

                    connection deleteConnection = new connection(this);
                    deleteConnection.execute(type, id);

                    type = "add";

                    connection addConnection = new connection(this);
                    addConnection.execute(type, userName, recipeName.getText().toString(), tasks, items);

                }
                else {

                    ArrayList<task> temp = organiseTasks();
                    String tasks = "";
                    String items = "";

                    for(task t : temp){
                        String hour = Integer.toString(t.getTimeValues().get(0));
                        String min = Integer.toString(t.getTimeValues().get(1));
                        String sec = Integer.toString(t.getTimeValues().get(2));
                        tasks += t.getTaskName() + "//Name//" + t.getInstructions() + "//Des//"
                                + hour + "//hour//" + min + "//min//" + sec + "//sec//";


                        for(task ti : t.getDependencies()){
                            tasks += ti.getTaskName() + "//";
                        }


                        tasks += "//task//";
                    }

                    for(ingredient i : ingredients){
                        String name = i.getIngredientName();
                        String amount = i.getIngredientAmount();
                        String unit = i.getIngredientUnit();
                        items += name + "//Name//" + amount + "//Amount//"
                                + unit + "//Unit////Ingredient//";
                    }

                    String type = "add";

                    connection connections = new connection(this);
                    connections.execute(type, userName, recipeName.getText().toString(), tasks, items);
                }
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case RESULT_LOAD_IMAGE: // if the base request code was this then run this code
                if(resultCode == RESULT_OK){ //make sure that the return result is ok
                    try {
                        imgUri = data.getData(); //gets the Uri of the image

                        image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imgUri); //adds the image to the bitmap image
                        image.compress(Bitmap.CompressFormat.JPEG, 50, bytes); //compress the image to 50% quality

                        image = Bitmap.createScaledBitmap(image, imageView.getWidth(), imageView.getHeight(), false); //scale the image to th size of the image view
                        imageView.setImageBitmap(image); //set the image view to be the bitmap
                        imageChecker = 1;
                    }
                    catch(IOException e){
                        //add code for the exception
                    }
                }

                break;
            case STEP_RESULT_POP_UP:
                if(resultCode == RESULT_OK){

                    ArrayList<Integer> t = data.getIntegerArrayListExtra("timeList");
                    String task = "";
                    if(t.get(0) > 0){
                        if(t.get(0) == 1){
                            task += " " + Integer.toString(t.get(2)) + " Hour";
                        }
                        else {
                            task += " " + Integer.toString(t.get(2)) + " Hours";
                        }
                    }
                    if(t.get(1) > 0){
                        if(t.get(1) == 1){
                            task += " " + Integer.toString(t.get(1)) + " Min";
                        }
                        else {
                            task += " " + Integer.toString(t.get(1)) + " Mins";
                        }
                    }
                    if(t.get(2) > 0){
                        if(t.get(2) == 1){
                            task += " " + Integer.toString(t.get(2)) + " Sec";
                        }
                        else {
                            task += " " + Integer.toString(t.get(2)) + " Secs";
                        }
                    }
                    String info = data.getStringExtra("name") + "\n" + data.getStringExtra("info") + "\nTime: " + task;
                    newTaskLayout.addView(createViewBox(info, 1)); //add a new view to the new task layout. the TOrI int is used to set the correct listener depending on if we are adding an ingredient or a step
                    task tempTask = new task(); //create a new task

                    tempTask.setTaskName(data.getStringExtra("name")); //set the name of the new task to be the passed back name
                    tempTask.setInstructions(data.getStringExtra("info")); //set the instructions of the new task to be the passed back info
                    tempTask.setDependencies((ArrayList<task>) data.getSerializableExtra("dependenciesList"));
                    tempTask.setTimeValues(data.getIntegerArrayListExtra("timeList"));

                    steps.add(tempTask); //add the task to the steps array

                }
                break;
            case STEP_RESULT_POP_UP_EDIT:
                if(resultCode == RESULT_OK) {
                    taskToEdit.setInstructions(data.getStringExtra("info")); //changes the info of the task that is being edited to the new info
                    taskToEdit.setTaskName(data.getStringExtra("name")); //changes the name of the task that is being edited to the new name
                    taskToEdit.setDependencies((ArrayList<task>) data.getSerializableExtra("dependenciesList"));
                    taskToEdit.setTimeValues(data.getIntegerArrayListExtra("timeList"));

                    TextView txtDisplay = findViewById(editViewNumber); //gets the text view display of this task

                    ArrayList<Integer> t = data.getIntegerArrayListExtra("timeList");
                    String task = "";
                    if(t.get(0) > 0){
                        if(t.get(0) == 1){
                            task += " " + Integer.toString(t.get(2)) + " Hour";
                        }
                        else {
                            task += " " + Integer.toString(t.get(2)) + " Hours";
                        }
                    }
                    if(t.get(1) > 0){
                        if(t.get(1) == 1){
                            task += " " + Integer.toString(t.get(1)) + " Min";
                        }
                        else {
                            task += " " + Integer.toString(t.get(1)) + " Mins";
                        }
                    }
                    if(t.get(2) > 0){
                        if(t.get(2) == 1){
                            task += " " + Integer.toString(t.get(2)) + " Sec";
                        }
                        else {
                            task += " " + Integer.toString(t.get(2)) + " Secs";
                        }
                    }
                    String info = data.getStringExtra("name") + "\n" + data.getStringExtra("info") + "\nTime: " + task;


                    txtDisplay.setText(info); //changes the text of this text view to the new name

                }
                break;
            case INGREDIENT_RESULT_POP_UP:
                if(resultCode == RESULT_OK){
                    String info = data.getStringExtra("name") + " \nAmount: " + data.getStringExtra("amount") + " " + data.getStringExtra("unit");
                    newIngredientLayout.addView(createViewBox(info, 0)); //adds a new ingredient to the ingredient view

                    ingredient tempIngredient = new ingredient(); //creates a new ingredient

                    tempIngredient.setIngredientName(data.getStringExtra("name")); //adds a name to the new ingredient
                    tempIngredient.setIngredientAmount(data.getStringExtra("amount"));//adds the amount to the new ingredient
                    tempIngredient.setIngredientUnit(data.getStringExtra("unit"));//adds the unit to the new ingredient

                    ingredients.add(tempIngredient); //adds this new ingredient to the list
                }
                break;
            case INGREDIENT_RESULT_POP_UP_EDIT:
                if(resultCode == RESULT_OK){
                    ingredientToEdit.setIngredientName(data.getStringExtra("name")); //changes the name of the ingredient to the new name
                    ingredientToEdit.setIngredientAmount(data.getStringExtra("amount")); //changes the amount of the ingredient to the new amount
                    ingredientToEdit.setIngredientUnit(data.getStringExtra("unit")); //changes the unit of the ingredient to the new unit

                    TextView txtDisplay = findViewById(editViewNumber); //gets the textview of this ingredient
                    String info = data.getStringExtra("name") + " \nAmount: " + data.getStringExtra("amount") + " " + data.getStringExtra("unit");
                    txtDisplay.setText(info); //changes the text of the text view to be the new name
                }
                break;
        }


    }

    private TextView createViewBox(String name, int TOrI){
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
        newTxtView.setTextSize(18); //set the text size
        newTxtView.setText(name); //set the text of the view to be the passed in name

        if(TOrI == 1){ //if this int is 1 then a new task is being made
            newTxtView.setOnClickListener(taskListener); //give the view an onclick command
            newTxtView.setId(taskIdentifier); //set the id of the view
            taskIdentifier = taskIdentifier + 1; //increase the taskID int being used to give the views a unique id
        }
        else{ //else it is a ingredient that is being made
            newTxtView.setOnClickListener(ingredientListner);
            newTxtView.setId(ingredientIdentifier); //set the id of the view
            ingredientIdentifier = ingredientIdentifier + 1; //increase the taskID int being used to give the views a unique id
        }

        return newTxtView; //return the new view
    }

    View.OnClickListener taskListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            editViewNumber = view.getId();


            taskToEdit = steps.get(editViewNumber);

            Intent i = new Intent(view.getContext(), createStep.class);

            i.putExtra("otherSteps", steps);
            i.putExtra("taskDependencies", taskToEdit.getDependencies());
            i.putExtra("taskName", taskToEdit.getTaskName());
            i.putExtra("task", taskToEdit.getInstructions());
            i.putExtra("taskTime", taskToEdit.getTimeValues());


            startActivityForResult(i, STEP_RESULT_POP_UP_EDIT);
        }
    };

    View.OnClickListener ingredientListner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            editViewNumber = view.getId();


            ingredientToEdit = ingredients.get(editViewNumber);

            Intent y = new Intent(view.getContext(), ingredientWindow.class);

            y.putExtra("ingredientName", ingredientToEdit.getIngredientName());
            y.putExtra("amount", ingredientToEdit.getIngredientAmount());
            y.putExtra("unit", ingredientToEdit.getIngredientUnit());

            startActivityForResult(y, INGREDIENT_RESULT_POP_UP_EDIT);
        }
    };

    private ArrayList<task> organiseTasks(){

        ArrayList<task> organisedTasks = new ArrayList<task>();
        ArrayList<task> test = new ArrayList<task>();


        for(task t : steps){
            if(t.getDependencies().size() == 0){
                organisedTasks.add(t);
                t.getTaskName();
            }
        }

        for(task t : organisedTasks){
            steps.remove(t);
        }

        while (steps.size() != 0){

            for(task t : steps){
                if(organisedTasks.containsAll(t.getDependencies())){
                    organisedTasks.add(t);
                    test.add(t);
                }
            }

            for(task t : test){
                steps.remove(t);
            }

            test.clear();
        }

        return organisedTasks;
    }
}