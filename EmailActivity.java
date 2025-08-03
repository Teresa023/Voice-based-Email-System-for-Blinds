package com.nextgen.emailbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;

import java.util.Locale;

public class EmailActivity extends AppCompatActivity {
    private static final String TAG = "EmailActivity";

    CardView inboxCV;
    CardView composeCV;
    TextToSpeech myTTS;
    private String name,email;
    GlobalPreference globalPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        globalPreference = new GlobalPreference(EmailActivity.this);
        name = globalPreference.getName();
        email = globalPreference.getEmail();

        myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    myTTS.setLanguage(Locale.US);
                    myTTS.speak("Touch   on    the    top      to      composemail     and   Touch   bottom     to     Open     Inbox", TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });

        inboxCV = findViewById(R.id.card_inbox);
        composeCV = findViewById(R.id.card_compose);

        inboxCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmailActivity.this,InboxActivity.class);
                startActivity(intent);
            }
        });

        composeCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "compose: "+email);

                myTTS.speak("Compose", TextToSpeech.QUEUE_FLUSH, null);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        check();
                    }
                },2000);
            }
        });
    }

    public void check() {

        Log.d(TAG, "Check: "+email);
        if(email.equals("")){

            myTTS.speak("Email not  Recognised    please login again  ", TextToSpeech.QUEUE_FLUSH, null);
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent inn = new Intent(EmailActivity.this,LoginActivity.class);
                    startActivity(inn);
                }
            }, 3000);

        }
        else{
            Intent inn = new Intent(EmailActivity.this,AddressActivity.class);
            inn.putExtra("email",email);
            startActivity(inn);
        }

    }
}