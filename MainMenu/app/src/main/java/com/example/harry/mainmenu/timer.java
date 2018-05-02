package com.example.harry.mainmenu;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

public class timer extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<task> taskList;
    private String name;
    private task currentTask;
    private TextView timeDisplay;
    private ArrayList<Integer> times;
    private long startTimeInMillis;
    private long timeLeftInMillis;
    private CountDownTimer countDownTimer;
    private boolean timerRunning;
    private Button startButton;
    private MediaPlayer alert;
    private TextView taskName;
    private int currentTaskPosition = 0;
    private int numberOfTasks;
    private Button next;
    private Button previous;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        alert = MediaPlayer.create(this, R.raw.alert);

        timeDisplay = (TextView) findViewById(R.id.digitalclock);
        startButton = (Button) findViewById(R.id.start);
        taskName = (TextView) findViewById(R.id.currentTask);
        next = (Button) findViewById(R.id.next);
        previous = (Button) findViewById(R.id.previous);

        startButton.setOnClickListener(this);
        next.setOnClickListener(this);
        previous.setOnClickListener(this);


        taskList = (ArrayList<task>) getIntent().getSerializableExtra("recipeTasks");
        name = getIntent().getStringExtra("recipeName");

        currentTask = taskList.get(currentTaskPosition);
        numberOfTasks = taskList.size() - 1;

        setTimer();

    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.start:
                if (timerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
                break;
            case R.id.next:
                if(currentTaskPosition < numberOfTasks){
                    currentTaskPosition = currentTaskPosition + 1;
                    currentTask = taskList.get(currentTaskPosition);
                    setTimer();
                }
                break;
            case R.id.previous:
                if(currentTaskPosition > 0){
                    currentTaskPosition = currentTaskPosition - 1;
                    currentTask = taskList.get(currentTaskPosition);
                    setTimer();
                }
        }
    }

    private void setTimer(){
        times = currentTask.getTimeValues();

        int timeInSeconds = times.get(1)* 60;
        timeInSeconds = timeInSeconds + times.get(2);
        startTimeInMillis = timeInSeconds * 1000;
        timeLeftInMillis = startTimeInMillis;

        taskName.setText(currentTask.getTaskName());

        updateCountDownText();

    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                startButton.setText("Start");
                alert.start();
                timeLeftInMillis = startTimeInMillis;
                updateCountDownText();
            }
        }.start();

        timerRunning = true;
        startButton.setText("pause");
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        timerRunning = false;
        startButton.setText("Start");
    }

    public void backMain(View view){
        finish();
    }

    public void viewSettings(View view){
        Intent intent = new Intent(this, mainSettings.class);
        startActivity(intent);
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        timeDisplay.setText(timeLeftFormatted);
    }
}