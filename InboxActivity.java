package com.nextgen.emailbot;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class InboxActivity extends AppCompatActivity {

    private static final String TAG = "InboxActivity";

    ListView inboxListView;

    ArrayList<HashMap<String,String>> mailList;
    private TextToSpeech myTTS;

    private GlobalPreference globalPreference;
    private String ip,uid,email;

    String content;

    private ImageView backIV;
    private TextView titleTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        globalPreference = new GlobalPreference(this);
        ip = globalPreference.getIP();
        uid = globalPreference.getID();
        email = globalPreference.getEmail();

        Log.d(TAG,"emailId"+email);

        myTTS=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    myTTS.setLanguage(Locale.US);
                    myTTS.speak("Now your Inbox is opened click  one to read ", TextToSpeech.QUEUE_FLUSH, null);

                }
            }
        });

        inboxListView = (ListView) findViewById(R.id.inboxListView);
        mailList = new ArrayList<HashMap<String,String>>();
        getAllAddress();

        backIV = findViewById(R.id.BackImageButton);
        titleTV = findViewById(R.id.appBarTitle);
        titleTV.setText("Inbox");

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent bIntent = new Intent(InboxActivity.this,EmailActivity.class);
                startActivity(bIntent);
                finish();
            }
        });

    }

    private void getAllAddress() {

        final String  UrlData = "?email=" +email;
        Log.d(TAG, "getAllAddress: "+UrlData);

        class MessageDetails extends AsyncTask<String, Void, String> {

            ProgressDialog progressDialog;

            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(InboxActivity.this, "Please wait", null, true, true);
            }

            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.d("**11**", "onPostExecute: "+s);
                progressDialog.dismiss();
                if(s!=null && s.equalsIgnoreCase("failed")){
                    Toast.makeText(InboxActivity.this,"There is No Address", Toast.LENGTH_LONG).show();
                }
                else {
                  //  Toast.makeText(InboxActivity.this, ""+s, Toast.LENGTH_SHORT).show();

                    displayMailList(s);
                }
            }

            @Override
            protected String doInBackground(String... params) {

                String s = params[0];
                BufferedReader bf = null;
                String result = null;
                HttpURLConnection connection = null;

                try {
                    String u = "http://" + ip + "/voice_email/api/getInbox.php" + s;
                    URL url = new URL(u);
                    Log.d("URL", "doInBackground: " + url);

                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(10000); // 10 seconds timeout
                    connection.setReadTimeout(10000);
                    connection.setDoInput(true);

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        bf = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = bf.readLine()) != null) {
                            sb.append(line);
                        }
                        result = sb.toString();
                    } else {
                        Log.e("HTTP Error", "Response Code: " + responseCode);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bf != null) {
                        try {
                            bf.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
                return result;
            }

        }
        MessageDetails Msg = new MessageDetails();
        Msg.execute(UrlData);
    }

    private void displayMailList(String res) {

        Log.d("**11**", "displayMailList: "+res);

        JSONArray jarray= null;
        try {
            jarray = new JSONArray(res);

            for (int i = 0; i < jarray.length(); i++) {
                JSONObject jobj = null;

                jobj = jarray.getJSONObject(i);

               String id = jobj.getString("id");
               String cont = jobj.getString("content");
               String send = jobj.getString("sender");
               String msg = jobj.getString("subject");


                HashMap<String, String> msgs = new HashMap<String, String>();

                msgs.put("subject", msg);
                msgs.put("id", id);
                msgs.put("content", cont);
                msgs.put("sender", send);

                mailList.add(msgs);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(InboxActivity.this, mailList,R.layout.raw_inbox_list, new String[]{"sender"}, new int[]{R.id.addressTextView}) {
        };
        inboxListView.setAdapter(adapter);

        inboxListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                myTTS.speak("You clicked on "+mailList.get(i).get("subject")+"from"+mailList.get(i).get("sender"), TextToSpeech.QUEUE_FLUSH, null);

                content = mailList.get(i).get("content");
                /*addre = mailList.get(i).get("subject");
                id = mailList.get(i).get("id");
                content = mailList.get(i).get("content");
                sender = mailList.get(i).get("sender"); */
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Intent intent =new Intent(InboxActivity.this,ContentActivity.class);
                        intent.putExtra("content",content);
                        startActivity(intent);
                    }
                }, 3000);

            }
        });
    }
}