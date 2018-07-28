package com.example.ali_sarkhosh.cafebazzar.activitys;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ali_sarkhosh.cafebazzar.G;
import com.example.ali_sarkhosh.cafebazzar.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivitySecond extends Activity {

    AnimationDrawable animationDrawable;
    Button btn_back;
    TextView name , city , country , distance , crossStreet ,address , t1 , t2 , t3 , t4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_second);

        init();
   //     initAnim();
        setTypeFace();
        setData();

  //      ViewCompat.setLayoutDirection(findViewById(R.id.layout_dialog), ViewCompat.LAYOUT_DIRECTION_LTR);


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void setTypeFace() {
        name.setTypeface(G.tf);
        city.setTypeface(G.tf);
        country.setTypeface(G.tf);
        distance.setTypeface(G.tf);
        crossStreet.setTypeface(G.tf);
        address.setTypeface(G.tf);
        btn_back.setTypeface(G.tf);
        t1.setTypeface(G.tf);
        t2.setTypeface(G.tf);
        t3.setTypeface(G.tf);
        t4.setTypeface(G.tf);
    }

    @SuppressLint("SetTextI18n")
    private void setData() {
        Intent intent = getIntent();
        name.setText(intent.getStringExtra("name"));
        city.setText(intent.getStringExtra("city"));
        country.setText(intent.getStringExtra("country"));
        distance.setText(intent.getStringExtra("distance") + getString(R.string.meter));
        crossStreet.setText(intent.getStringExtra("crossStreet"));
        address.setText(intent.getStringExtra("address"));
    }

    private void initAnim() {
        animationDrawable = (AnimationDrawable) btn_back.getBackground();
        animationDrawable.setEnterFadeDuration(3000);
        animationDrawable.setExitFadeDuration(1000);
    }

    private void init() {
        btn_back =findViewById(R.id.det_btn_back);
        name  = findViewById(R.id.det_txt_name);
        city = findViewById(R.id.det_txt_city);
        country = findViewById(R.id.det_txt_country);
        distance = findViewById(R.id.det_txt_dis);
        crossStreet = findViewById(R.id.det_txt_cross);
        address = findViewById(R.id.det_txt_address);
        t1 = findViewById(R.id.det_txt_t1);
        t2 = findViewById(R.id.det_txt_t2);
        t3 = findViewById(R.id.det_txt_t3);
        t4 = findViewById(R.id.det_txt_t4);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (animationDrawable != null && !animationDrawable.isRunning())
            animationDrawable.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (animationDrawable != null && animationDrawable.isRunning())
            animationDrawable.stop();
    }
}
