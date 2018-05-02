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
import com.example.harry.mainmenu.connection;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class register extends Activity {

    private
    EditText textUser;
    EditText textPassword;
    EditText textPasswordCon;
    EditText textEmail;
    EditText textEmailCon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startLayout1();
    }

    public void backLogin(View v){
        finish();
    }

    public void register(View v){
        textUser = (EditText) findViewById(R.id.username);
        CharSequence textName_value = textUser.getText();

        textPassword = (EditText) findViewById(R.id.password);
        CharSequence textPassword_value = textPassword.getText();

        textPasswordCon = (EditText) findViewById(R.id.confirmPass);
        CharSequence textPasswordCon_value = textPasswordCon.getText();

        textEmail = (EditText) findViewById(R.id.email);
        CharSequence textEmail_value = textEmail.getText();

        textEmailCon = (EditText) findViewById(R.id.confirmEmail);
        CharSequence textEmailCon_value = textEmailCon.getText();

        if(textName_value.length() < 1 || textPassword_value.length() < 1){
            Context context = getApplicationContext();
            CharSequence text = "Invalid: Fields not filled in";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else if(!textPassword_value.toString().equals(textPasswordCon_value.toString())){
            errorMessage("Invalid: The password does not match");
        }
        else if(!textEmail_value.toString().equals(textEmailCon_value.toString())){
            errorMessage("Invalid: The email does not match");
        }
        else if(!checkEmail(textEmail_value.toString())){
            Context context = getApplicationContext();
            CharSequence text = "Invalid: Email is in incorrect format";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else {

            String username = textUser.getText().toString();
            String email = textEmail.getText().toString();
            String password = String.valueOf(textPassword.getText().toString().hashCode());
            String type = "register";
            connection connections = new connection(this);
            connections.execute(type, username, email, password);
        }
    }

    private void startLayout1(){
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        ImageView backImage = (ImageView) findViewById(R.id.backLogin);
        backImage.setImageResource(R.drawable.backbutton);
    };

    private boolean checkEmail(String email){
        String emailFormat = "(?:[a-zA-Z0-9-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern emailPattern = Pattern.compile(emailFormat);

        Matcher matcher = emailPattern.matcher(email);
        if(matcher.matches() == true) {
            return true;
        }
        else{
            return false;
        }
    }

    private void errorMessage(String s){
        Context context = getApplicationContext();
        CharSequence text = s;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
