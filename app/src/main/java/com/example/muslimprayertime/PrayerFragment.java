package com.example.muslimprayertime;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.example.muslimprayertime.feedback.feedBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class PrayerFragment extends Fragment {
    //
    //
   /* SharedPreferences sharedpreferences;
   // TextView name;
    //TextView email;
    public static final String mypreference = "mypref";
    public static final String fajor = "fajor";
    public static final String johor = "johor";*/



    //define all distric data
    private static final String[] districName = new String[]{
            "Bagerhat","Bandarban","Barguna","Barisal","Bhola","Bogra","Brahmanbaria","Chandpur","Chittagong"
            ,"Comilla","Cox's Bazar","Dhaka","Dinajpur","Faridpur","Feni","Gazipur","Gopalganj",
            "Habiganj","Jaipurhat","Jamalpur","Jessore","Jhalakati","Jhenaidah","Khagrachari","Khulna","Kishoreganj"
            ,"Kushtia","Lakshmipur","Lalmonirhat","Madaripur","Magura","Manikganj","Meherpur","Moulvibazar","Munshiganj",
            "Mymensingh","Naogaon","Narail","Narayanganj","Narsingdi","Natore","Nawabganj","Netrakona","Nilphamari","Noakhali",
            "Pabna","Panchagarh", "Parbattya Chattagram","Patuakhali","Pirojpur","Rajbari","Rajshahi","Rangpur","Satkhira",
            "Shariatpur","Sherpur","Sirajganj","Sunamganj","Sylhet","Tangail","Thakurgaon"
    };

    ////
    ImageView image;
    TextView location_info,date_info,temparature_info,fojor_namaj_info,johor_namaj_info,asor_namaj_info,magrib_namaj_info,isha_namaj_info,
            surjo_udoy_info,text_tapmatra,text_degree;

    AutoCompleteTextView actx;

    private void prayerData() {
        final AutoCompleteTextView actx = getView().findViewById(R.id.actv);

        if (actx.getText().toString().isEmpty()) {

            Toast.makeText(getActivity(),
                    "Enter A Location",
                    Toast.LENGTH_LONG)
                    .show();
        } else {

            String s = actx.getText().toString();
/*
        // URL to get contacts JSON
         String url = "https://muslimsalat.com/"+s+".json?key=80bf63d2909ced2719313f42ac3d44d3";

      //
        //  Log.d(TAG, "prayerData: " + url);*/

            // Calling download task function
            download task = new download();

            // Executing download task and change this " uk&appid=9f681051b003f104ae5e2a0cbef19a02" with your own API KEY
            task.execute("https://muslimsalat.com/"+s+".json?key=80bf63d2909ced2719313f42ac3d44d3");

            Log.d(TAG, "prayerData: " + s);

        }

    }

    private String TAG = PrayerFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View view =   inflater.inflate(R.layout.fragment_prayer, container, false);



        ///
        ///
        /*fojor_namaj_info = (TextView) getView().findViewById(R.id.fozor_id);
        johor_namaj_info = (TextView) getView().findViewById(R.id.johor_id);
        sharedpreferences = this.getActivity().getSharedPreferences(mypreference ,Context.MODE_PRIVATE);
        if (sharedpreferences.contains(fajor)) {
            fojor_namaj_info.setText(sharedpreferences.getString(fajor, ""));
        }
        if (sharedpreferences.contains(johor)) {
            johor_namaj_info.setText(sharedpreferences.getString(johor, ""));

        }

        String n = fojor_namaj_info.getText().toString();
        String e = johor_namaj_info.getText().toString();
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(fajor, n);
        editor.putString(johor  , e);
        editor.commit();*/

        //call all item with id
        final AutoCompleteTextView actx = view.findViewById(R.id.actv);
        ImageView image = (ImageView) view.findViewById(R.id.img);
        Button btn = (Button) view.findViewById(R.id.button);
        actx.setThreshold(1);
        //make a array adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_dropdown_item_1line,districName);
        actx.setAdapter(adapter);
        //show drop down image click able
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actx.showDropDown();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prayerData();
            }
        });

       /* //code for screen animation
        ImageView imageView = view.findViewById(R.id.imageViewGradiant);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.setEnterFadeDuration(10000);
        animationDrawable.setExitFadeDuration(15000);
        animationDrawable.start();
*/
        return view;
    }

    // Creating download method with Async Task ( we r going to use this to get data from internet and parse it )
    private class download extends AsyncTask<String, Void, String> {

        // This method execute after doInBackground method and Parse the Json

        @Override
        protected void onPostExecute(String result) {

            Log.i("info",result);
            // Try and catch block to catch any errors
            try {
                // linking result1 with textView with id result
                location_info = (TextView) getView().findViewById(R.id.location_id);
                date_info = (TextView) getView().findViewById(R.id.date_id);
                temparature_info = (TextView) getView().findViewById(R.id.temparature_id);
                fojor_namaj_info = (TextView) getView().findViewById(R.id.fozor_id);
                johor_namaj_info = (TextView) getView().findViewById(R.id.johor_id);
                asor_namaj_info= (TextView) getView().findViewById(R.id.asor_id);
                magrib_namaj_info = (TextView) getView().findViewById(R.id.magrib_id);
                isha_namaj_info = (TextView) getView().findViewById(R.id.isha_id);
                surjo_udoy_info = (TextView) getView().findViewById(R.id.surjoUdoy_id);

                text_tapmatra = (TextView) getView().findViewById(R.id.tapmatra_id);
                text_degree = (TextView) getView().findViewById(R.id.degree_id);

                image = (ImageView) getView().findViewById(R.id.locationImageIcon);
                final AutoCompleteTextView actx = getView().findViewById(R.id.actv);


                JSONObject jsonObj = new JSONObject(result);


                JSONObject weatherJsonObj = jsonObj.getJSONObject("today_weather");
                Log.i(TAG,"json"+jsonObj);
                String temp = weatherJsonObj.getString("temperature");
                temparature_info.setText(String.format("%s ", temp));

                text_tapmatra.setText("তাপমাত্রা :");
                text_degree.setText("°C");

              //  String city = jsonObj.getString("city");
               // temparature_info.setText(String.format("%s ", city));

                String state = jsonObj.getString("state");
                location_info.setText(actx.getText().toString()+","+String.format("%s ", state));

                image.setImageResource(R.drawable.ic_location_on_black_24dp);

                ///

                // Getting JSON Array node
                JSONArray item = jsonObj.getJSONArray("items");

                // looping through All Contacts
                for (int i = 0; i < item.length(); i++) {
                    JSONObject itemJSONObject = item.getJSONObject(i);

                    String fajorTime = itemJSONObject.getString("fajr");
                    String sat=String.format(fajorTime);

                    fojor_namaj_info.setText(sat);

                    String johorTime = itemJSONObject.getString("dhuhr");
                    johor_namaj_info.setText(String.format("%s",johorTime));

                    String asorTime = itemJSONObject.getString("asr");
                    asor_namaj_info.setText(String.format("%s",asorTime));

                    String magribTime = itemJSONObject.getString("maghrib");
                    magrib_namaj_info.setText(String.format("%s",magribTime));

                    String ishaTime = itemJSONObject.getString("isha");
                    isha_namaj_info.setText(String.format("%s",ishaTime));

                    String surzoUdoy = itemJSONObject.getString("shurooq");
                    surjo_udoy_info.setText(String.format("%s",surzoUdoy));

                    String date = itemJSONObject.getString("date_for");
                    date_info.setText(String.format("%s",date));



                    /*fojor_namaj_info = (TextView) getView().findViewById(R.id.fozor_id);
                    johor_namaj_info = (TextView) getView().findViewById(R.id.johor_id);
                    sharedpreferences = this.getActivity().getSharedPreferences(mypreference ,Context.MODE_PRIVATE);
                    if (sharedpreferences.contains(fajor)) {
                        fojor_namaj_info.setText(sharedpreferences.getString(fajor, ""));
                    }
                    if (sharedpreferences.contains(johor)) {
                        johor_namaj_info.setText(sharedpreferences.getString(johor, ""));

                    }

                    String n = fojor_namaj_info.getText().toString();
                    String e = johor_namaj_info.getText().toString();
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(fajor, n);
                    editor.putString(johor  , e);
                    editor.commit();*/

                }
                //e.printStackTrace will just print a error report like a normal program do when it crashes u can change this with anything u like
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // do doInBackground method we r using this method to download Json from site.
        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            //  calling url as url
            URL url;
            // calling HttpUrlConnection as urlConnection
            HttpURLConnection urlConnection;
            // Using try and catch block to find any errors
            try {
                // assigning url value of first object in array called urls which is declared in this start of this method
                url = new URL(urls[0]);
                // using urlConnection to open url which we assigning URL before
                urlConnection = (HttpURLConnection) url.openConnection();
                // Using InputStream to download the content
                InputStream inputStream = urlConnection.getInputStream();
                // Using InputStreamReader to read the inputstream or the data we r downloading
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                // using it to check if we reached the end of String / Data
                int Data = inputStreamReader.read();
                // using While loop to assign that data to string called result because InputStreamReader reads only one character at a time
                while (Data != -1) {
                    char current = (char) Data;
                    result += current;
                    Data = inputStreamReader.read();
                }
                // returning value of result
                return result;
                //Try and catch block to catch any errors
            } catch (Exception e) {
                //  e.printStackTrace will just print a error report like a normal program do when it crashes u can change this with anything u like
                e.printStackTrace();
            }
            return null;
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_prayer, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_prayer) {
           /* Toast.makeText(getActivity(), "Cliced on " + item.getTitle(), Toast.LENGTH_SHORT)
                    .show();*/

            Intent intent = new Intent(getActivity().getApplication(), feedBack.class);
            startActivity(intent);
        }
        return true;
    }
}
