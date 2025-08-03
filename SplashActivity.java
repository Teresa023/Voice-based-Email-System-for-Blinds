package com.nextgen.emailbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;

import java.util.HashMap;
import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    TextToSpeech myTTS;
    GlobalPreference globalPreference;
    boolean loginStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        globalPreference = new GlobalPreference(this);
        loginStatus = globalPreference.getLoginStatus();

        myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    myTTS.setLanguage(Locale.US);
                    myTTS.setOnUtteranceCompletedListener(new TextToSpeech.OnUtteranceCompletedListener() {
                        @Override
                        public void onUtteranceCompleted(String utteranceId) {
                            // The text has been spoken or read, now start the new activity

                            Intent intent;
                            if (loginStatus){
                                intent = new Intent(SplashActivity.this, EmailActivity.class);
                            }else{
                                intent = new Intent(SplashActivity.this, LoginActivity.class);
                            }
                            startActivity(intent);

                        }
                    });

                    HashMap<String, String> params = new HashMap<>();
                    params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");

                    myTTS.speak("Welcome to Voice Based Email", TextToSpeech.QUEUE_FLUSH, params);
                }
            }
        });
    }
}