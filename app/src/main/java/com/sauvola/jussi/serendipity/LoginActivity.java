package com.sauvola.jussi.serendipity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Objects;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;


public class LoginActivity extends AppCompatActivity {

   // public static final String MyPREFERENCES = "MyPrefs" ;
    //public static final String prefUserName = "nameKey";
    //public static final String prefLoggedUserID = "idKey";
    //SharedPreferences sharedpreferences;

    EditText usernameField, passwordField;
    Button login;
    Button map;
    String in = "";

    String JSONOutput = "";

    public static String loggedUserId = "";

    //SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        usernameField = (EditText) findViewById(R.id.email);
        passwordField = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.email_sign_in_button);
        map = (Button) findViewById(R.id.map_button);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();





                connectWithHttpGet(username, password);

                Log.e("Lol", JSONOutput);





            }
        });



        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.sauvola.jussi.serendipity.MapsActivity");
                startActivity(intent);
            }

        });




    }

    private void connectWithHttpGet(final String username, String password) {

        class HttpGetAsyncTask extends AsyncTask<String, Void, String>{

            @Override
            protected String doInBackground(String... params) {



                String paramUsername = params[0];
                String paramPassword = params[1];
                System.out.println("username" + paramUsername + " paramPassword is :" + paramPassword);

                // Create an intermediate to connect with the Internet
                HttpClient httpClient = new DefaultHttpClient();

                HttpGet httpGet = new HttpGet("http://serendipitydemo.com/loginuser.php?username=" + paramUsername + "&password=" + paramPassword);

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

                try {
                    //SharedPreferences.Editor editor = sharedpreferences.edit();
                    JSONObject reader = new JSONObject(JSONOutput);
                    loggedUserId = reader.getString("id");
                    //editor.putString(prefLoggedUserID, loggedUserId);
                    String loggedUsername = reader.getString("username");
                    //editor.putString(prefUserName, loggedUsername);
                    //editor.commit();
                    if (Objects.equals(username, loggedUsername)) {
                        //session.createLoginSession(loggedUserId, loggedUsername);
                        Toast.makeText(getApplicationContext(), "Welcome " + loggedUsername, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent("com.sauvola.jussi.serendipity.ProfileActivity");
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Username or password is invalid", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Username or password is invalid", Toast.LENGTH_SHORT).show();
                }

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
        httpGetAsyncTask.execute(username, password);

    }
}

