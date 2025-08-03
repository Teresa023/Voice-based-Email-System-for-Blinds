package com.nextgen.emailbot;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 ProfileFragment
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match

    public static final String TAG = "ProfileFragment";

    View view;
    TextView nameTV;
    TextView numberTV;
    TextView emailTV;
    TextView usernameTV;

    ImageView editProfileIV;

    private GlobalPreference globalPreference;
    private  String ip,uid;
    String userdata;

    public ProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        globalPreference = new GlobalPreference(getContext());
        ip = globalPreference.getIP();
        uid = globalPreference.getID();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        nameTV = view.findViewById(R.id.pNameTextView);
        numberTV = view.findViewById(R.id.pNumberTextView);
        emailTV = view.findViewById(R.id.pEmailEditText);
        usernameTV = view.findViewById(R.id.pUsernameTextView);
        editProfileIV = view.findViewById(R.id.editProfileImageView);

        getUserDetails();

        editProfileIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(),EditProfileActivity.class);
                intent.putExtra("userdata",userdata);
                startActivity(intent);
            }
        });

        return view;
    }

    private void getUserDetails() {
        String URL = "http://"+ ip +"/voice_email/api/profile.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "onResponse: " + response);

                if (!response.equals("")){
                    try{

                        userdata = response;

                        JSONObject obj = new JSONObject(response);
                        JSONArray array = obj.getJSONArray("data");
                        JSONObject data = array.getJSONObject(0);

                        String name = data.getString("name");
                        String email = data.getString("email");
                        String number = data.getString("number");
                        String username = data.getString("username");



                        nameTV.setText(name);
                        numberTV.setText(number);
                        emailTV.setText(email);
                        usernameTV.setText(username);

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(getContext(),"hyy",Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("uid",uid);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}