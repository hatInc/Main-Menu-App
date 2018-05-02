package com.example.harry.mainmenu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.harry.mainmenu.R;

import java.util.ArrayList;

/**
 * Created by Harry on 28/02/2018.
 */

public class createStep extends Activity implements View.OnClickListener{

    private EditText name;
    private EditText info;
    private Button sub;
    private ArrayList<task> dependantTasks; //this will contain a list of the tasks that the new task could be dependant on
    private LinearLayout dependantLayout;
    private ArrayList<task> dependantNumbers = new ArrayList<task>();
    private int dependantTaskID = 0;
    private int thisTaskID = 0;
    private NumberPicker hourPicker;
    private NumberPicker minPicker;
    private NumberPicker secPicker;
    private ArrayList<Integer> time;
    private ArrayList<Integer> passedInTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.stepwindow);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width* .8),(int) (height * .8));

        ScrollView layout = findViewById(R.id.mainLayout);
            // Gets the layout params that will allow you to resize the layout
        ViewGroup.LayoutParams params = layout.getLayoutParams();
            // Changes the height and width to the specified *pixels*
        params.height = (int)(height * .8);
        params.width = (int)(width * .8);
        layout.setLayoutParams(params);

        name = (EditText) findViewById(R.id.stepName);
        info = (EditText) findViewById(R.id.stepInfo);
        sub = (Button) findViewById(R.id.submit);
        dependantLayout = (LinearLayout) findViewById(R.id.dependenciesLayout);
        hourPicker = (NumberPicker) findViewById(R.id.hour);
        minPicker = (NumberPicker) findViewById(R.id.min);
        secPicker = (NumberPicker) findViewById(R.id.sec);

        hourPicker.setMaxValue(24);
        minPicker.setMaxValue(59);
        secPicker.setMaxValue(59);



        dependantTasks = (ArrayList<task>) getIntent().getSerializableExtra("otherSteps");

        if(dependantTasks.size() == 0){
            dependantLayout.addView(createViewBox());
        }
        else{
            int viewModeChecker = 0;
            if(getIntent().hasExtra("viewMode")){
                viewModeChecker = 1;
            }
            for (task t : dependantTasks){
                if(getIntent().hasExtra("taskName")){

                    ArrayList<task> tempNumbers = (ArrayList<task>) getIntent().getSerializableExtra("taskDependencies");

                    if(!t.getTaskName().equals(getIntent().getStringExtra("taskName"))){

                        if(tempNumbers.contains(t)){
                            dependantLayout.addView(createCheckBox(t.getTaskName(), viewModeChecker,1));
                        }
                        else{
                            dependantLayout.addView(createCheckBox(t.getTaskName(), viewModeChecker,0));
                        }

                    }
                    else{
                        thisTaskID = dependantTaskID + 1;
                        dependantTaskID = dependantTaskID + 1;
                    }

                }
                else {
                    dependantLayout.addView(createCheckBox(t.getTaskName(), viewModeChecker,0));
                }
            }
        }



        sub.setOnClickListener(this);

        if(getIntent().hasExtra("taskName")){
            name.setText(getIntent().getStringExtra("taskName"));
            info.setText(getIntent().getStringExtra("task"));

            passedInTime = getIntent().getIntegerArrayListExtra("taskTime");

            hourPicker.setValue(passedInTime.get(0));
            minPicker.setValue(passedInTime.get(1));
            secPicker.setValue(passedInTime.get(2));

        }

        if(getIntent().hasExtra("viewMode")){
            name.setEnabled(false);
            info.setEnabled(false);
            sub.setVisibility(View.GONE);
            hourPicker.setEnabled(false);
            minPicker.setEnabled(false);
            secPicker.setEnabled(false);

        }
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.submit:
                Intent i = new Intent();
                i.putExtra("name", name.getText().toString());
                i.putExtra("info", info.getText().toString());

                int tempID = 0;
                CheckBox tempCheck;

                ArrayList<Integer> dependantPosistion = new ArrayList<Integer>();

                for(task t : dependantTasks){
                    if(thisTaskID == 0){
                        tempCheck = (CheckBox) findViewById(tempID);
                        if(tempCheck.isChecked()){
                            dependantPosistion.add(tempID);
                        }
                        tempID = tempID + 1;
                    }
                    else{
                        if(tempID != thisTaskID - 1){
                            tempCheck = (CheckBox) findViewById(tempID);
                            if(tempCheck.isChecked()){
                                dependantPosistion.add(tempID);
                            }
                        }
                        tempID = tempID + 1;
                    }
                }

                for(int x : dependantPosistion){
                    dependantNumbers.add(dependantTasks.get(x));
                }

                time = new ArrayList<Integer>();

                time.add(hourPicker.getValue());
                time.add(minPicker.getValue());
                time.add(secPicker.getValue());

                i.putExtra("timeList", time);

                i.putExtra("dependenciesList", dependantNumbers);
                setResult(RESULT_OK,i);
                finish();
                break;
        }
    }

    private CheckBox createCheckBox(String name, int viewMode, int checker){
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lParams.setMargins(25,10,25,10);
        CheckBox newCheckBox = new CheckBox(this);
        newCheckBox.setLayoutParams(lParams);
        newCheckBox.setEms(10);
        newCheckBox.setTextColor(getResources().getColor(R.color.black));
        newCheckBox.setEllipsize(TextUtils.TruncateAt.START);
        newCheckBox.setGravity(Gravity.CENTER);
        newCheckBox.setBackgroundResource(R.drawable.edit_text_background);
        newCheckBox.setPadding(0,0,0,0);
        newCheckBox.setTextSize(18);
        newCheckBox.setText(name);
        newCheckBox.setId(dependantTaskID);
        dependantTaskID = dependantTaskID + 1;

        if(checker == 1){
            newCheckBox.setChecked(true);
        }

        if(viewMode == 1){
            newCheckBox.setEnabled(false);
        }

        return newCheckBox;
    }

    private TextView createViewBox(){
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT); //creates new layout parameters to be used with the new view
        lParams.setMargins(25,10,25,10);
        TextView newTxtView = new TextView(this); //creates a new text view
        newTxtView.setLayoutParams(lParams); //give the new text view the parameters just made
        newTxtView.setEms(10); //honestly not sure what this does
        newTxtView.setTextColor(getResources().getColor(R.color.black)); //sets the text to black
        newTxtView.setEllipsize(TextUtils.TruncateAt.START); //not sure about this either
        newTxtView.setGravity(Gravity.CENTER); //moves the text to the center of the box
        newTxtView.setBackgroundResource(R.drawable.edit_text_background); //set the background to be the base border backgroud
        newTxtView.setPadding(0,0,0,0); //set the padding to be 0 on all sides
        newTxtView.setTextSize(18); //set the text size
        newTxtView.setText("There are no other tasks"); //set the text of the view to be the passed in name


        return newTxtView; //return the new view
    }

}
