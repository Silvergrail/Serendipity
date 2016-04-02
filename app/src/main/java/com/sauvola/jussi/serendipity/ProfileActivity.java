package com.sauvola.jussi.serendipity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class ProfileActivity extends AppCompatActivity {



    //SessionManager session;

    //public static final String MyPREFERENCES = "MyPrefs" ;

    //SharedPreferences sharedpreferences = null;
    public static final String prefLoggedUserID = "idKey";



    private static final String TAG_ARRAY = "";
    private static final String TAG_FILE_ID = "file_id";
    private static final String TAG_FILE_TITLE = "file_title";
    private static final String TAG_FILE_DESCRIPTION = "description";
    private static final String TAG_FILE_GPS = "GPS";

    JSONArray music = null;

    String idtest = "197";

    String username = "";

    String JSONOutput = "";

    ArrayList<HashMap<String, String>> musicList = new ArrayList<HashMap<String, String>>();

    Button recordButton;

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String userID = "197";

        RequestQueue queue = Volley.newRequestQueue(this);

        //sharedpreferences = getSharedPreferences(MyPREFERENCES, Activity.MODE_PRIVATE);
        //String loggeduserid = sharedpreferences.getString(prefLoggedUserID, "");
        setContentView(R.layout.activity_profile);

        connectWithHttpGet(userID);

        Log.e("Lol", JSONOutput);
        //connectWithHttpGet(session.getUserID());

        final String url = "http://serendipitydemo.com/getmusic.php?id=197";


        recordButton = (Button) findViewById(R.id.record_button);

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent("com.sauvola.jussi.serendipity.RecordActivity");
                    startActivity(intent);
                }

        });

        musicList = new ArrayList<HashMap<String, String>>();

        lv = (ListView) findViewById(R.id.listView);

        try {
            connectWithHttpGet(userID);
            JSONObject reader = new JSONObject(JSONOutput);
            music = reader.getJSONArray(TAG_ARRAY);
            for (int i = 0; i < music.length(); i++) {
                JSONObject m = reader.getJSONObject(String.valueOf(i));
                String id = m.getString(TAG_FILE_ID);
                String title = m.getString(TAG_FILE_TITLE);
                String description = m.getString(TAG_FILE_DESCRIPTION);
                String gps = m.getString(TAG_FILE_GPS);
                HashMap<String, String> music = new HashMap<String, String>();
                music.put(TAG_FILE_ID, id);
                music.put(TAG_FILE_TITLE, title);
                music.put(TAG_FILE_DESCRIPTION, description);
                music.put(TAG_FILE_GPS, gps);

                musicList.add(music);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), JSONOutput, Toast.LENGTH_SHORT).show();
        }



        ListAdapter adapter = new SimpleAdapter(
                ProfileActivity.this, musicList,
                R.layout.list_item, new String[] { TAG_FILE_ID, TAG_FILE_TITLE,
                TAG_FILE_DESCRIPTION, TAG_FILE_GPS }, new int[] { R.id.file_id,
                R.id.file_title, R.id.file_description, R.id.file_gps });

        lv.setAdapter(adapter);

        connectWithHttpGet("197");
        connectWithHttpGet(userID);

        Log.e("Lol", JSONOutput);


    }

    private void connectWithHttpGet(String userID) {

        class HttpGetAsyncTask extends AsyncTask<String, Void, String>{

            @Override
            protected String doInBackground(String... params) {

                String paramUserID = params[0];
                System.out.println("username" + paramUserID);

                // Create an intermediate to connect with the Internet
                HttpClient httpClient = new DefaultHttpClient();

                HttpGet httpGet = new HttpGet("http://serendipitydemo.com/getmusic.php?id=197");

                try {
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    InputStream inputStream;

                    inputStream = httpResponse.getEntity().getContent();

                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    StringBuilder stringBuilder = new StringBuilder();

                    String bufferedStrChunk = null;

                    while((bufferedStrChunk = bufferedReader.readLine()) != null){
                        stringBuilder.append(bufferedStrChunk);
                    }

                    return stringBuilder.toString();

                } catch (ClientProtocolException cpe) {
                    cpe.printStackTrace();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                JSONOutput = result;

/*                if(result.equals("Joomla! Authentication was successful!")){
                    Toast.makeText(getApplicationContext(), "Welcome " + JSONOutput, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent("com.sauvola.jussi.serendipity.ProfileActivity");
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "Username or password is invalid", Toast.LENGTH_LONG).show();
                }*/

            }
        }

        HttpGetAsyncTask httpGetAsyncTask = new HttpGetAsyncTask();
        httpGetAsyncTask.execute(userID);

    }



}


