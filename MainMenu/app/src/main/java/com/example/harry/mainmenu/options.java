package com.example.harry.mainmenu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.harry.mainmenu.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class options extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startLayout1();
    }

    public void newUser(View view){
        Intent intent = new Intent(this, register.class);
        startActivity(intent);
    }

    public void signIn(View view){
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
    }

    private void startLayout1(){
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_options);

        ImageView optionImage = (ImageView) findViewById(R.id.optionImage);
        optionImage.setImageResource(R.drawable.options);

        ImageView logoImage = (ImageView) findViewById(R.id.logoImage);
        logoImage.setImageResource(R.drawable.title_white);
    };
}
