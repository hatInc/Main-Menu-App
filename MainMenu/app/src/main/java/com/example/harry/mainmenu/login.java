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

public class login extends Activity {

    private
    EditText textName;
    EditText textPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startLayout1();
    }

    public void login(View v){
        textName = (EditText) findViewById(R.id.username);
        CharSequence textName_value = textName.getText();

        textPassword = (EditText) findViewById(R.id.password);
        CharSequence textPassword_value = textPassword.getText();

        if(textName_value.length() < 1 || textPassword_value.length() < 1){
            Context context = getApplicationContext();
            CharSequence text = "Invalid: Fields not filled in";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else {
            String username = textName.getText().toString();
            String password = String.valueOf(textPassword.getText().toString().hashCode());
            String type = "login";
            connection connections = new connection(this);
            connections.execute(type, username, password);
        }
    }

    public void backLogin(View v){
        finish();
    }

    private void startLayout1(){
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        ImageView backImage = (ImageView) findViewById(R.id.backLogin);
        backImage.setImageResource(R.drawable.backbutton);
    };
}
