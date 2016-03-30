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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;



public class ProfileActivity extends AppCompatActivity {

    String myJSON;

    private static final String TAG_RESULTS="result";
    private static final String FILE_ID = "file_id";
    private static final String FILE_TITLE = "file_title";
    private static final String FILE_DESCRIPTION ="file_description";
    private static final String FILE_GPS ="GPS";

    Button recordButton;

    JSONArray musics = null;

    ArrayList<HashMap<String, String>> musicList;

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        recordButton = (Button) findViewById(R.id.record_button);

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent("com.sauvola.jussi.serendipity.RecordActivity");
                    startActivity(intent);
                }

        });

        list = (ListView) findViewById(R.id.listView);
        musicList = new ArrayList<HashMap<String,String>>();
        getData();
    }


    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            musics = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i=0;i< musics.length();i++){
                JSONObject c = musics.getJSONObject(i);
                String file_id = c.getString(FILE_ID);
                String file_title = c.getString(FILE_TITLE);
                String file_description = c.getString(FILE_DESCRIPTION);
                String file_gps = c.getString(FILE_GPS);


                HashMap<String,String> musics = new HashMap<String,String>();

                musics.put(FILE_ID, file_id);
                musics.put(FILE_TITLE, file_title);
                musics.put(FILE_DESCRIPTION, file_description);
                musics.put(FILE_GPS, file_gps);


                musicList.add(musics);
            }

            ListAdapter adapter = new SimpleAdapter(
                    ProfileActivity.this, musicList, R.layout.list_item,
                    new String[]{FILE_ID, FILE_TITLE, FILE_DESCRIPTION, FILE_GPS},
                    new int[]{R.id.file_id, R.id.file_title, R.id.file_description, R.id.file_gps}
            );

            list.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getData(){
        class GetDataJSON extends AsyncTask<String, Void, String>{

            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost("http://www.serendipitydemo.com/getmusic.php");

                // Depends on your web service
                // httppost.setHeader("Content-type", "application/json");

                InputStream inputStream = null;
                String result = null;
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();

                    inputStream = entity.getContent();
                    // json is UTF-8 by default
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (Exception e) {
                    // Oops
                }
                finally {
                    try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result){
                myJSON=result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }


}


