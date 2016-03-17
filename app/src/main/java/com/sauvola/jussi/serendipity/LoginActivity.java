package com.sauvola.jussi.serendipity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    //Defining views
    private EditText email;
    private EditText password;
    private Button emailSignInButton;

    //boolean variable to check user is logged in or not
    //initially it is false
    private boolean loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initializing views
        email = (EditText) findViewById(R.id.email);
        new GetData(email).execute("");
        String emailInserted = email.getText().toString();

        password = (EditText) findViewById(R.id.password);
        String passwordInserted = password.getText().toString();

        emailSignInButton = (Button) findViewById(R.id.email_sign_in_button);

    }

    private class GetData extends AsyncTask<String, Void, String> {
        private EditText display;


        GetData(EditText view){
            this.display = view;
            display = (EditText) findViewById(R.id.email);
        }

        protected String doInBackground(String... message){
            HttpClient httpclient;
            HttpGet request;
            HttpResponse response = null;
            String result = "";
            try{
                httpclient = new DefaultHttpClient();
                request = new HttpGet(Config.LOGIN_URL);
                response = httpclient.execute(request);
            } catch (Exception e){
                result = "error1";
            }

            try{
                BufferedReader rd = new BufferedReader(new InputStreamReader(
                        response.getEntity().getContent()));
                String line="";
                while((line = rd.readLine()) != null){
                    result = result + line;
                }
            } catch(Exception e){
                result = "error2";
            }
            return result;
        }

        protected void onPostExecute(String result){
            this.display.setText(result);
        }
    }

    private void loginPost() {





    };



}

