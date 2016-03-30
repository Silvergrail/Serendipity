package com.sauvola.jussi.serendipity;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Intent;
import android.os.AsyncTask;


public class LoginActivity extends AppCompatActivity {

    EditText usernameField, passwordField;
    private static final String TAG_USER_NAME = "username";
    private static final String TAG_PASSWORD = "password";
    Button login;
    Button map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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

    private void connectWithHttpGet(String username, String password) {

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

                if(result.equals("Joomla! Authentication was successful!")){
                    Toast.makeText(getApplicationContext(), "Joomla! Authentication was successful!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent("com.sauvola.jussi.serendipity.ProfileActivity");
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "Username or password is invalid", Toast.LENGTH_LONG).show();
                }
            }
        }

        HttpGetAsyncTask httpGetAsyncTask = new HttpGetAsyncTask();
        httpGetAsyncTask.execute(username, password);

    }
}

