package com.project.nilawan.biketracker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class NotificationSet extends Activity {
    private GoogleApiClient client;

    private Double sy;
    private TextView text1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Receivesys();

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        Button bt_menu = (Button)findViewById(R.id.Bmenu);
        Button bt_on = (Button)findViewById(R.id.Bon);
        Button bt_off = (Button)findViewById(R.id.Boff);


        bt_menu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent inte = new Intent(NotificationSet.this, Control.class);
                startActivity(inte);
            }
        });

    }

    private void Receivesys() {

        {


            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... voids) {
                    OkHttpClient okHttpClient = new OkHttpClient();

                    Request.Builder builder = new Request.Builder();
                    Request request = builder.url("http://tr.ddnsthailand.com/checknoti.php").build();

                    try {
                        Response response = okHttpClient.newCall(request).execute();
                        if (response.isSuccessful()) {
                            return response.body().string();
                        } else {
                            return "Not Success - code : " + response.code();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return "Error - " + e.getMessage();
                    }
                }

                @Override
                protected void onPostExecute(String string) {
                    super.onPostExecute(string);


                    try {

                        JSONArray data = new JSONArray(string);
                        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
                        HashMap<String, String> map;

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject c = data.getJSONObject(i);
                            map = new HashMap<>();
                            map = new HashMap<String, String>();
                            map.put("id", c.getString("id"));
                            map.put("sys", c.getString("sys"));
                            MyArrList.add(map);

                        }
                        String ssys = MyArrList.get(0).get("sys");

                        double sys = Double.parseDouble(ssys);

                        sy = sys;


                        condition();


                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                }

                private void condition() {


                   /**station1*/
                    if (sy == 1)  {
                        TextView text1 = (TextView) findViewById(R.id.notistatus);
                        text1.setText("OFF");
                    }

                    else if (sy == 0)  {
                        TextView text1 = (TextView) findViewById(R.id.notistatus);
                        text1.setText("ON");
                    }


                    Loop();

                }


            }.execute();

        }
    }

    private void Loop() {

        android.os.Handler h = new android.os.Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                Receivesys();
            }
        }, 4000);

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Showlocation Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.project.nilawan.biketracker/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Showlocation Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.project.nilawan.biketracker/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
