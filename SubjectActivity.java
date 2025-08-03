package com.nextgen.emailbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

public class SubjectActivity extends AppCompatActivity {

    private static final String TAG = "SubjectActivity";

    TextToSpeech myTTS;

    ImageView micIV;

    String url;

    String email,remail,subject,content;

    private GlobalPreference globalPreference;
    private String ip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        globalPreference = new GlobalPreference(this);
        ip = globalPreference.getIP();
        email = globalPreference.getEmail();

        remail = getIntent().getStringExtra("remail");

        micIV = findViewById(R.id.micImageView);
        micIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                promptSpeechInput();
            }
        });

        myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    myTTS.setLanguage(Locale.US);
                    myTTS.speak("Say  the  subject   ", TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });

       // Toast.makeText(SubjectActivity.this, "sender::"+email+"remail"+remail, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                //  promptSpeechInputProfile();
                promptSpeechInput();
            }
        }, 3000);



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

    private void promptSpeechInputContent() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Something");
        try {
            startActivityForResult(intent, 102);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Not Supported",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 101: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    //////////////////////// email //////////////////////////////
                    if(result.get(0).equals(" ")){

                    }
                    else{
                        subject = result.get(0);
                        myTTS.speak("say      the     Content    to     send", TextToSpeech.QUEUE_FLUSH, null);
                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                //  promptSpeechInputProfile();
                                promptSpeechInputContent();
                            }
                        }, 3000);
                    }
                }
                break;
            }
            case 102:{
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Toast.makeText(getApplicationContext(),""+result.get(0), Toast.LENGTH_LONG).show();
                    content = result.get(0);
                  //  insert();
                    confirmContent();

                }
                break;

            }
        }
    }

    private void confirmContent() {

       Intent intent = new Intent(SubjectActivity.this,DisplayContentActivity.class);
       intent.putExtra("email",email);
       intent.putExtra("remail",remail);
       intent.putExtra("subject",subject);
       intent.putExtra("content",content);
       startActivity(intent);

    }

    private void insert() {

        url = "http://"+ ip +"/voice_email/api/sendEmail.php";

        Log.d(TAG, "insert: "+url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response.contains("failed")){

                    myTTS.speak("Failed to send Email", TextToSpeech.QUEUE_FLUSH, null);

                }
                else{
                    myTTS.speak("Email has been sent", TextToSpeech.QUEUE_FLUSH, null);

                    Intent intent = new Intent(SubjectActivity.this,HomeActivity.class);
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
            protected Map<String,String> getParams() throws AuthFailureError{
                Map<String,String> params = new HashMap<>();
                params.put("email",email);
                params.put("remail",remail);
                params.put("subject",subject);
                params.put("content",content);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(SubjectActivity.this);
        requestQueue.add(stringRequest);
    }
}