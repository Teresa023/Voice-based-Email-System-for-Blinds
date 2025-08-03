package com.nextgen.emailbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

public class ContentActivity extends AppCompatActivity {

    TextToSpeech myTTS;
    String content;

    Button contentBT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);


        content = getIntent().getStringExtra("content");

        contentBT = findViewById(R.id.contentButton);

        myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    myTTS.setLanguage(Locale.US);
                    myTTS.speak("Tap on the  screen   to   read  content", TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });

        contentBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*int speechSpeed = 120;
                int contentLength = content.length();
                int estimatedDuration = (contentLength / speechSpeed) * 60 * 1000; */

                Intent intent = new Intent(ContentActivity.this, ContentActivity2.class);
                intent.putExtra("content",content);
                startActivity(intent);

              /*  myTTS.speak(content, TextToSpeech.QUEUE_FLUSH, null);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(ContentActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                }, 4000); */
            }
        });


    }
}