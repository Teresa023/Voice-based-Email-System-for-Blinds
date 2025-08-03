package com.nextgen.emailbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    EditText nameET;
    EditText numberET;
    EditText emailET;
    EditText usernameET;
    TextView saveTV;
    TextView changePasswordTV;
    TextView profileIconTV;
    private ImageView backIV;
    private GlobalPreference globalPreference;
    private String ip,uid;
    private Intent intent;
    String userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        globalPreference = new GlobalPreference(this);
        ip = globalPreference.getIP();
        uid = globalPreference.getID();

        intent = getIntent();
        userDetails = intent.getStringExtra("userdata");


        iniit();

        setData(userDetails);

        changePasswordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this,ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        saveTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(EditProfileActivity.this, "Save Clicked", Toast.LENGTH_SHORT).show();
                check();
            }
        });

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(EditProfileActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void check() {
        if (nameET.getText().toString().equals("")) {
            nameET.setError("Please Enter name");
        }
        else if (numberET.getText().equals("") || numberET.getText().length() > 10 || numberET.getText().length() < 10) {
            numberET.setError("Invalid Phone number ");
        }
        else if (emailET.getText().toString().equals("")) {
            emailET.setError("Please Enter Email");
        }
        else if (usernameET.getText().toString().equals("")) {
            usernameET.setError("Please Enter Username");
        }else{
            updateProfile();
        }

    }

    private void setData(String response) {

        try{
            JSONObject obj = new JSONObject(response);
            JSONArray array = obj.getJSONArray("data");
            JSONObject data = array.getJSONObject(0);

            String uName = data.getString("name");
            String uNumber = data.getString("number");
            String uEmail = data.getString("email");
            String uUsername = data.getString("username");


            // to get the first letter of name

            String iconText = uName;
            char first = iconText.charAt(0);
            String firstLetter = String.valueOf(first);

            //setting the first letter of user name as icon
            profileIconTV.setText(firstLetter);


            nameET.setText(uName);
            numberET.setText(uNumber);
            emailET.setText(uEmail);
            usernameET.setText(uUsername);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void updateProfile() {

        String URL = "http://"+ ip +"/voice_email/api/editProfile.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(EditProfileActivity.this,""+response,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditProfileActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditProfileActivity.this,""+error,Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();

                params.put("name",nameET.getText().toString());
                params.put("number",numberET.getText().toString());
                params.put("email",emailET.getText().toString());
                params.put("username",usernameET.getText().toString());
                params.put("uid",uid);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void iniit() {
        nameET = findViewById(R.id.eNameEditText);
        numberET = findViewById(R.id.eNumberEditText);
        emailET = findViewById(R.id.eEmailEditText);
        usernameET = findViewById(R.id.eUsernameEditText);
        saveTV = findViewById(R.id.saveTextView);
        changePasswordTV = findViewById(R.id.changePasswordTextView);
        profileIconTV = findViewById(R.id.eProfileIconTextView);
        backIV = findViewById(R.id.editBackButtonIV);

    }
}