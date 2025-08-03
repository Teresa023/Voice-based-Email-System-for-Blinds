package com.nextgen.emailbot;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddEmailActivity extends AppCompatActivity {

    private static final String TAG = "AddEmailActivity";

    TextToSpeech myTTS;

    EditText addEmailET;
    Button addEmailBT;

    private GlobalPreference globalPreference;
    private String ip;
    private ImageView backIV;
    private TextView titleTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_email);

        globalPreference = new GlobalPreference(this);
        ip = globalPreference.getIP();


        backIV = findViewById(R.id.BackImageButton);
        titleTV = findViewById(R.id.appBarTitle);
        titleTV.setText("Add Email");

        myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    myTTS.setLanguage(Locale.US);

                }
            }
        });

        addEmailET = findViewById(R.id.addEmailEditText);
        addEmailBT = findViewById(R.id.addEmailButton);

        addEmailBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEmail();
            }
        });

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent bIntent = new Intent(AddEmailActivity.this,EmailActivity.class);
                startActivity(bIntent);
                finish();
            }
        });
    }

    private void addEmail() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://" + ip + "/voice_email/api/addEmail.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("success")){

                    myTTS.speak("Email added Successfully",TextToSpeech.QUEUE_FLUSH,null);

                    Toast.makeText(AddEmailActivity.this, "Email added Successfully", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AddEmailActivity.this,EmailActivity.class);
                    startActivity(intent);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"onErrorResponse: "+error);
            }
        }){
            @Override
            @Nullable
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("email",addEmailET.getText().toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(AddEmailActivity.this);
        requestQueue.add(stringRequest);
    }
}