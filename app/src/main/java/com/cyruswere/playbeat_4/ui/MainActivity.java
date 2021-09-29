package com.cyruswere.playbeat_4.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.cyruswere.playbeat_4.R;

public class MainActivity extends AppCompatActivity {

    //To control how long the splash screen is displayed.
    private static int SPLASH_TIME_OUT = 5000;

    //Hooks variables.
    View first,second,third,fourth,fifth,sixth;
    TextView tagLine,logoText;

    //Animation variables
    Animation topAnimation,bottomAnimation,middleAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        middleAnimation = AnimationUtils.loadAnimation(this,R.anim.middle_animation);

        //Hooks
        first = findViewById(R.id.first_line);
        second = findViewById(R.id.second_line);
        third = findViewById(R.id.third_line);
        fourth = findViewById(R.id.fourth_line);
        fifth = findViewById(R.id.fifth_line);
        sixth = findViewById(R.id.sixth_line);

        tagLine = findViewById(R.id.tagLine);
        logoText = findViewById(R.id.textLogo);

        first.setAnimation(topAnimation);
        second.setAnimation(topAnimation);
        third.setAnimation(topAnimation);
        fourth.setAnimation(topAnimation);
        fifth.setAnimation(topAnimation);
        sixth.setAnimation(topAnimation);

        logoText.setAnimation(middleAnimation);
        tagLine.setAnimation(bottomAnimation);

        //Handle moving from splash screen to welcome screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this,WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}