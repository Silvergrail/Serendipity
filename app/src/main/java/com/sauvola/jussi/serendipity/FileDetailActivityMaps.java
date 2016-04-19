package com.sauvola.jussi.serendipity;

/**
 * Created by Juze on 14.4.2016.
 */

import static com.sauvola.jussi.serendipity.MapsActivity.TAG_FILE_TITLE;
import static com.sauvola.jussi.serendipity.MapsActivity.TAG_FILE_GPS;
import static com.sauvola.jussi.serendipity.MapsActivity.TAG_FILE_DESCRIPTION;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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
import android.media.MediaPlayer;

public class FileDetailActivityMaps extends AppCompatActivity {

    public static final String TAG_FILE_DESCRIPTION = "description";
    public static final String TAG_FILE_GPS = "GPS";
    public static final String TAG_FILE_SOURCE = "url_download";
    public static String audioSource = "";

    MediaPlayer mPlayer = new MediaPlayer();

    private boolean intialStage = true;
    private boolean playPause;

    Button deleteButton;
    Button saveButton;
    ImageButton playButton;
    private String JSONOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_detail);
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);;

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

        playButton = (ImageButton) findViewById(R.id.record_play_button);

        connectWithHttpGet(file_title);

        playButton.setOnClickListener(pausePlay);

    }

    private View.OnClickListener pausePlay = new View.OnClickListener() {

        @Override
        public void onClick(View v) {


            if (!playPause) {
                playButton.setBackgroundResource(R.drawable.ic_record_pause);
                if (intialStage)
                    new Player()
                            .execute("http://www.serendipitydemo.com/jdownloads/_uncategorised_files/" + audioSource);
                else {
                    if (!mPlayer.isPlaying())
                        mPlayer.start();
                }
                playPause = true;
            } else {
                playButton.setBackgroundResource(R.drawable.ic_record_play);
                if (mPlayer.isPlaying())
                    mPlayer.pause();
                playPause = false;
            }
        }
    };

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

                    while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
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
                        audioSource = m.getString(TAG_FILE_SOURCE);
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

    class Player extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog progress;

        @Override
        protected Boolean doInBackground(String... params) {
            Boolean prepared;
            try {

                mPlayer.setDataSource(params[0]);

                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        intialStage = true;
                        playPause = false;
                        playButton.setBackgroundResource(R.drawable.ic_record_play);
                        mPlayer.stop();
                        mPlayer.reset();
                    }
                });
                mPlayer.prepare();
                prepared = true;
            } catch (IllegalArgumentException e) {
                Log.d("IllegarArgument", e.getMessage());
                prepared = false;
                e.printStackTrace();
            } catch (SecurityException e) {
                prepared = false;
                e.printStackTrace();
            } catch (IllegalStateException e) {
                prepared = false;
                e.printStackTrace();
            } catch (IOException e) {
                prepared = false;
                e.printStackTrace();
            }
            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (progress.isShowing()) {
                progress.cancel();
            }
            Log.d("Prepared", "//" + result);
            mPlayer.start();

            intialStage = false;
        }

        public Player() {
            progress = new ProgressDialog(FileDetailActivityMaps.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progress.setMessage("Buffering...");
            this.progress.show();

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPlayer != null) {
            mPlayer.reset();
            mPlayer.release();
            mPlayer = null;
        }
    }
    }


