package com.nextgen.emailbot;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

/**
 HomeFragment
 */
public class HomeFragment extends Fragment {

    // TODO: Rename and change types of parameters

    private static final String TAG = "QRFragment";

    View view;
    TextView nameTV;
    ImageView logoutIV;

    CardView inboxCV;
    CardView composeCV;
    TextToSpeech myTTS;

    private GlobalPreference globalPreference;
    private String name,email;
    public HomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        globalPreference = new GlobalPreference(getContext());
        name = globalPreference.getName();
        email = globalPreference.getEmail();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        nameTV = view.findViewById(R.id.nameTextView);
        logoutIV = view.findViewById(R.id.logoutImageView);
        inboxCV = view.findViewById(R.id.card_inbox);
        composeCV = view.findViewById(R.id.card_compose);

        myTTS = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    myTTS.setLanguage(Locale.US);

                }
            }
        });

        nameTV.setText(name);

      /*  inboxCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),InboxActivity.class);
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
        });*/

        logoutIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }

    private void check() {

        Log.d(TAG, "Check: "+email);
        if(email.equals("")){

           myTTS.speak("Email not  Recognised    please login again  ", TextToSpeech.QUEUE_FLUSH, null);
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent inn = new Intent(getContext(),LoginActivity.class);
                    startActivity(inn);
                }
            }, 3000);

        }
        else{
            Intent inn = new Intent(getContext(),AddressActivity.class);
            inn.putExtra("email",email);
            startActivity(inn);
        }

    }
}