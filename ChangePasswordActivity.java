package com.nextgen.emailbot;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText currentPasswordET;
    EditText newPasswordET;
    Button resetBT;

    private GlobalPreference globalPreference;
    private String ip;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        globalPreference = new GlobalPreference(this);
        ip = globalPreference.getIP();
        uid = globalPreference.getID();

        currentPasswordET = findViewById(R.id.currentPasswordEditText);
        newPasswordET = findViewById(R.id.newPasswordEditText);
        resetBT = findViewById(R.id.resetPasswordButton);

        resetBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentPasswordET.getText().toString().equals("") || newPasswordET.getText().toString().equals("")){

                    Toast.makeText(ChangePasswordActivity.this, "Please Fill the Fields", Toast.LENGTH_SHORT).show();
                }else{
                    updatePassword();
                }
            }
        });
    }

    private void updatePassword() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://"+ ip +"/voice_email/api/changePassword.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("success")){
                    Toast.makeText(ChangePasswordActivity.this, "Password Updated", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(ChangePasswordActivity.this,HomeActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(ChangePasswordActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ChangePasswordActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("uid", uid);
                params.put("currentPass", currentPasswordET.getText().toString());
                params.put("newPass", newPasswordET.getText().toString());
                return params;

            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}