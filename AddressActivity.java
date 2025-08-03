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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddressActivity extends AppCompatActivity {

    private static final String TAG = "AddressActivity";

    TextToSpeech myTTS;
    ListView listView;
    ArrayList<HashMap<String,String>> addressList;
    LinearLayout addEmailLL;

    String url,email;
    String emailId,addresses;

    private GlobalPreference globalPreference;
    private String ip;
    private ImageView backIV;
    private TextView titleTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        globalPreference = new GlobalPreference(this);
        ip = globalPreference.getIP();

        email = getIntent().getStringExtra("email");

        myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    myTTS.setLanguage(Locale.US);

                }
            }
        });

        listView = findViewById(R.id.addressListView);
        addressList = new ArrayList<HashMap<String,String>>();
        getAddress();

        backIV = findViewById(R.id.BackImageButton);
        titleTV = findViewById(R.id.appBarTitle);
        titleTV.setText("Compose");

        addEmailLL = findViewById(R.id.addEmailLL);

        addEmailLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent bIntent = new Intent(AddressActivity.this,AddEmailActivity.class);
                startActivity(bIntent);
            }
        });

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent bIntent = new Intent(AddressActivity.this,EmailActivity.class);
                startActivity(bIntent);
                finish();
            }
        });


    }

    private void getAddress() {

        url = "http://"+ ip +"/voice_email/api/getAddress.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("failed")){

                    myTTS.speak("Your email Addressbook is null please and recipient email address ",TextToSpeech.QUEUE_FLUSH,null);
                }else {

                    showList(response);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError{
                Map<String,String> params = new HashMap<>();
                params.put("email",email);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AddressActivity.this);
        requestQueue.add(stringRequest);
    }

    private void showList(String res) {
        Log.d("res", res);

        JSONArray jsonArray = null;

        try {
            jsonArray = new JSONArray(res);

            for (int i = 0;i < jsonArray.length();i++){

                JSONObject jsonObject = null;
                jsonObject = jsonArray.getJSONObject(i);
                emailId = jsonObject.getString("email");
                HashMap<String, String> eList = new HashMap<String, String>();
                eList.put("email", emailId);

                addressList.add(eList);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(AddressActivity.this, addressList, R.layout.raw_email_list, new String[]{"email"}, new int[]{R.id.emailIdTextView});

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                addresses = addressList.get(i).get("email");
                myTTS.speak("you Clicked on "+addresses+"Say yes to Continue",TextToSpeech.QUEUE_FLUSH, null);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        // TODO Auto-generated method stub
                        //  promptSpeechInputProfile();
                        promptSpeechInput();

                    }
                },4000);
            }
        });

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

                  //  Toast.makeText(AddressActivity.this, "dd"+result.get(0), Toast.LENGTH_SHORT).show();

                    if(result.get(0).equalsIgnoreCase("yes")){
                        Intent intent = new Intent(AddressActivity.this,SubjectActivity.class);
                        intent.putExtra("remail",addresses);
                        startActivity(intent);

                    }

                }
                break;
            }

        }
    }
}