package com.nextgen.emailbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DisplayContentActivity extends AppCompatActivity {

    private static final String TAG = "DisplayContentActivity";

    TextView contentTV;
    TextToSpeech myTTS;
    Button sendEmailBT;
    RelativeLayout contentRL;

    String url;

    String email,remail,subject,content;

    private GlobalPreference globalPreference;
    private String ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_content);

        globalPreference = new GlobalPreference(this);
        ip = globalPreference.getIP();

        email = getIntent().getStringExtra("email");
        remail = getIntent().getStringExtra("remail");
        subject = getIntent().getStringExtra("subject");
        content = getIntent().getStringExtra("content");

        contentTV = findViewById(R.id.contentTextView);
        sendEmailBT = findViewById(R.id.sendEmailButton);
        contentRL = findViewById(R.id.contentRL);

        sendEmailBT.setVisibility(View.GONE);

        myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    myTTS.setLanguage(Locale.US);
                    myTTS.speak("Your content is displayed   Click  on  the  screen  to  read  content ", TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });

        contentTV.setText(content);


        contentRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status != TextToSpeech.ERROR) {
                            myTTS.setLanguage(Locale.US);
                            myTTS.setOnUtteranceCompletedListener(new TextToSpeech.OnUtteranceCompletedListener() {
                                @Override
                                public void onUtteranceCompleted(String utteranceId) {
                                    // The text has been spoken or read, now start the new activity

                                    myTTS.speak(" Say  confirm  to  Send  your  email",TextToSpeech.QUEUE_FLUSH, null);

                                    promptSpeechInput();

                                }
                            });

                            HashMap<String, String> params = new HashMap<>();
                            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");

                            myTTS.speak(content, TextToSpeech.QUEUE_FLUSH, params);
                        }
                    }
                });


            }
        });

     /*   sendEmailBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myTTS.speak("You  Clicked  on  Send  Email  Option  Say  confirm  to  Send  your  email",TextToSpeech.QUEUE_FLUSH, null);

                promptSpeechInput();
            }
        });*/


    }

    private void promptSpeechInput() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Say Something");
        try {
            startActivityForResult(intent, 101);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), "Not Supported", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 101: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                   // Toast.makeText(DisplayContentActivity.this, "dd"+result.get(0), Toast.LENGTH_SHORT).show();

                    if(result.get(0).equalsIgnoreCase("confirm")){

                        insert();
                    } else {

                        myTTS.speak("Email ",TextToSpeech.QUEUE_FLUSH, null);

                        Intent intent = new Intent(DisplayContentActivity.this,EmailActivity.class);
                        startActivity(intent);
                    }

                }
                break;
            }

        }
    }

    private void insert() {

        url = "http://"+ ip +"/voice_email/api/sendEmail.php";

        Log.d(TAG, "insert: "+url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "emailResponsee:"+response);

                if(response.contains("failed")){

                    myTTS.speak("Failed to send Email", TextToSpeech.QUEUE_FLUSH, null);

                }
                else{

                    Toast.makeText(DisplayContentActivity.this, "Email has been sent", Toast.LENGTH_SHORT).show();

                    myTTS.speak("Email has been sent", TextToSpeech.QUEUE_FLUSH, null);

                    Intent intent = new Intent(DisplayContentActivity.this,EmailActivity.class);
                    startActivity(intent);


                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("res", "onErrorResponse: "+error.getMessage());
            }
        }){
            @Override
            @Nullable
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("email",email);
                params.put("remail",remail);
                params.put("subject",subject);
                params.put("content",content);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(DisplayContentActivity.this);
        requestQueue.add(stringRequest);
    }
}