package com.nextgen.emailbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.widget.TextView;

import java.util.Locale;

public class ContentActivity2 extends AppCompatActivity {

    TextToSpeech myTTS;

    TextView contentTV;
    String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content2);

        content = getIntent().getStringExtra("content");

        contentTV = findViewById(R.id.displayContentTextView);

        contentTV.setText(content);

        myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    myTTS.setLanguage(Locale.US);
                }
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                myTTS.speak(content, TextToSpeech.QUEUE_FLUSH, null);
            }
        }, 1000);


     /*   new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(ContentActivity2.this, EmailActivity.class);
                startActivity(intent);
            }
        }, 4000);*/



    }

    @Override
    public void onBackPressed() {
        myTTS.stop();

        Intent intent = new Intent(ContentActivity2.this, EmailActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        myTTS.stop();
        super.onDestroy();
    }
}