package com.sauvola.jussi.serendipity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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



public class ProfileActivity extends AppCompatActivity {

    //SessionManager session;

    String JSONOutput = "";

    Button recordButton;

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //connectWithHttpGet(session.getUserID());

        recordButton = (Button) findViewById(R.id.record_button);

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent("com.sauvola.jussi.serendipity.RecordActivity");
                    startActivity(intent);
                }

        });


        list = (ListView) findViewById(R.id.listView);


    }

    private void connectWithHttpGet(String id) {

        class HttpGetAsyncTask extends AsyncTask<String, Void, String>{

            @Override
            protected String doInBackground(String... params) {

                String ParamID = params[0];
                System.out.println("id" + ParamID);

                // Create an intermediate to connect with the Internet
                HttpClient httpClient = new DefaultHttpClient();

                HttpGet httpGet = new HttpGet("http://serendipitydemo.com/getmusic.php?id=" + ParamID);

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
        httpGetAsyncTask.execute(id);

    }




}


