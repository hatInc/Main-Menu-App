package com.example.harry.mainmenu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class mainSettings extends AppCompatActivity {

    CheckBox vegeterian;
    CheckBox vegan;
    CheckBox lac;
    CheckBox glut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_settings);

        vegeterian = (CheckBox) findViewById(R.id.vegeterianCheck);
        vegan = (CheckBox) findViewById(R.id.veganCheck);
        lac = (CheckBox) findViewById(R.id.lacCheck);
        glut = (CheckBox) findViewById(R.id.gluCheck);
    }

    public void backMain(View view){
        finish();
    }

    public void logOut(View view){
        Intent intent = new Intent(getApplicationContext(), options.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void apply(View view){
        String vegeterianS = "0";
        String veganS = "0";
        String lacS = "0";
        String glutS = "0";
        if(vegeterian.isChecked()){
            vegeterianS = "1";
        }
        if(vegan.isChecked()){
            veganS = "1";
        }
        if(lac.isChecked()){
            lacS = "1";
        }
        if(glut.isChecked()){
            glutS = "1";
        }
        Bundle extras = getIntent().getExtras();

        String userName= extras.getString("userName");

        String type = "apply";
        String data = vegeterianS + "//" + veganS + "//" + lacS + "//" + glutS;
        connection connections = new connection(this);
        connections.execute(type, userName, data);
    }
}
