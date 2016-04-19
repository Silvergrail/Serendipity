package com.sauvola.jussi.serendipity;

/**
 * Created by Juze on 14.4.2016.
 */

import static com.sauvola.jussi.serendipity.MapsActivity.TAG_FILE_TITLE;
import static com.sauvola.jussi.serendipity.MapsActivity.TAG_FILE_GPS;
import static com.sauvola.jussi.serendipity.MapsActivity.TAG_FILE_DESCRIPTION;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class FileDetailActivityMaps extends AppCompatActivity {

    public static final String TAG_FILE_DESCRIPTION = "description";
    public static final String TAG_FILE_GPS = "GPS";

    Button deleteButton;
    Button saveButton;
    private String JSONOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_detail);

        Intent in = getIntent();

        String file_title = "";
        String file_gps = "";
        String description = "";

        if (null != in) {
            file_title = in.getStringExtra(TAG_FILE_TITLE);
        }

        TextView file_title_text = (TextView) findViewById(R.id.file_title_text);
        file_title_text.setText(file_title);

        deleteButton = (Button) findViewById(R.id.delete_btn);

        saveButton = (Button) findViewById(R.id.save_btn);

        connectWithHttpGet(file_title);
    }

    private void connectWithHttpGet(String file_title) {

        class HttpGetAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String fileTitle = params[0];
                fileTitle = fileTitle.replaceAll(" ", "-");
                Log.e("Lol", fileTitle);

                // Create an intermediate to connect with the Internet
                HttpClient httpClient = new DefaultHttpClient();

                HttpGet httpGet = new HttpGet("http://serendipitydemo.com/getmusicbytitle.php?file_title=" + fileTitle);

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

                    Log.e("Lol", stringBuilder.toString());
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

                try {
                    JSONArray reader = new JSONArray(JSONOutput);
                    for (int i = 0; i < reader.length(); i++) {
                        JSONObject m = reader.getJSONObject(i);
                        String file_description = m.getString(TAG_FILE_DESCRIPTION);
                        TextView file_description_text = (TextView) findViewById(R.id.file_description_text);
                        file_description_text.setText(file_description);
                        String file_gps = m.getString(TAG_FILE_GPS);
                        TextView file_gps_text = (TextView) findViewById(R.id.file_gps_text);
                        file_gps_text.setText(file_gps);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), JSONOutput, Toast.LENGTH_SHORT).show();
                }

            }
        }

        HttpGetAsyncTask httpGetAsyncTask = new HttpGetAsyncTask();
        httpGetAsyncTask.execute(file_title);

    }
}
