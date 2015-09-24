package com.sugoilabs.bics;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Registration extends ActionBarActivity implements View.OnClickListener {
    EditText name;
    EditText phoneNumber;
    EditText emailId;
    TextView detailsMissing;
    EditText lastname;
    EditText code;
    Button next;
    ProgressBar progressBar;
    String nameString;
    String phoneNumberString;
    String emailIdString;
    String responseString;
    String lastNameString;
    String codeString;
    final String Prefs_DataSaved = "Prefs_Login_Data_Saved";
    SharedPreferences LoginDataSavedSharedPreference;
    SharedPreferences.Editor LoginDataSavedEditor;
    Intent intent;
    private boolean detailsValid;
    int invalidField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoginDataSavedSharedPreference = getSharedPreferences(Prefs_DataSaved, 0);
        LoginDataSavedEditor = LoginDataSavedSharedPreference.edit();
        intent = new Intent(this, FalsePositiveCaution.class);
        //LoginDataSavedEditor.clear();
        //LoginDataSavedEditor.commit();
        String loginDone = LoginDataSavedSharedPreference.getString("loginDone","no");

        //just used in debugging
        //startActivity(intent);

        if(loginDone.equals("yes")){
            startActivity(intent);
        }
        setContentView(R.layout.activity_registration);
        initializeLayouts();



    }

    private void initializeLayouts() {
        name = (EditText) findViewById(R.id.textview_name);
        phoneNumber = (EditText) findViewById(R.id.textview_number);
        emailId = (EditText)findViewById(R.id.textview_email);
        lastname = (EditText)findViewById(R.id.textview_last_name);
        code = (EditText)findViewById(R.id.textview_number_code);

        detailsMissing = (TextView) findViewById(R.id.registration_details_missing);
        next = (Button) findViewById(R.id.registration_next);
        next.setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.setIndeterminate(true);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.registration_next) {

            if (emailId.getText().toString().equals("") || phoneNumber.getText().toString().equals("") || name.getText().toString().equals("") ||
                    code.getText().toString().equals("") || lastname.getText().toString().equals("")) {
                detailsMissing.setText("Please fill all the details");
                detailsMissing.setVisibility(View.VISIBLE);
            } else if (!isDetailsValid()) {
                if(invalidField ==1){
                    detailsMissing.setText("Please check the Phone Number");
                    detailsMissing.setVisibility(View.VISIBLE);

                }
                else if(invalidField ==2){
                    detailsMissing.setText("Please check the Email Id");
                    detailsMissing.setVisibility(View.VISIBLE);
                }

            } else {
                //check internet , send the data via post , get acknowledgement , if acknowledgement is true then save to shared preference and next screen
                detailsMissing.setVisibility(View.INVISIBLE);
                if (isOnline()) {
                    nameString = name.getText().toString();
                    emailIdString = emailId.getText().toString();
                    lastNameString = lastname.getText().toString();
                    codeString = code.getText().toString();
                    phoneNumberString = phoneNumber.getText().toString();

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("first_name", nameString));
                    nameValuePairs.add(new BasicNameValuePair("last_name", lastNameString));
                    nameValuePairs.add(new BasicNameValuePair("country_code", codeString));
                    nameValuePairs.add(new BasicNameValuePair("mobile", phoneNumberString));
                    nameValuePairs.add(new BasicNameValuePair("email", emailIdString));
                    nameValuePairs.add(new BasicNameValuePair("register", "abc"));
                    nameValuePairs.add(new BasicNameValuePair("date_time", new SimpleDateFormat("yyyyMMdd_HHmm").format(Calendar.getInstance().getTime())));


                    AsyncTask<List<NameValuePair>, Void, String> Response = new sendLoginDataAndroid().execute(nameValuePairs);

                } else {
                    Toast.makeText(this, "Unable to Connect to Internet", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public void setVisibility(int visibility){
        if(visibility ==1){
            progressBar.setVisibility(View.VISIBLE);
            int visibility1 = progressBar.getVisibility();
            Log.d("visbility should be visible ",String.valueOf(visibility1)+ String.valueOf(progressBar.isShown()));

        }
        else{
            progressBar.setVisibility(View.INVISIBLE);
            int visibility1 = progressBar.getVisibility();
            Log.d("visbility should be invisible",String.valueOf(visibility1) + String.valueOf(progressBar.isShown()));
        }

    }

    public boolean isDetailsValid() {
        detailsValid=true;
        nameString = name.getText().toString();
        lastNameString = lastname.getText().toString();
        codeString = code.getText().toString();
        phoneNumberString = phoneNumber.getText().toString();
        emailIdString = emailId.getText().toString();

        invalidField=0;
        if(phoneNumberString.length()!=10){
            invalidField=1;
            detailsValid=false;
        }
        else if(!isValidEmailAddress(emailIdString)){
            invalidField=2;
            detailsValid=false;
        }

        return detailsValid;
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    private class sendLoginDataAndroid extends AsyncTask<List<NameValuePair>, Void , String> {

        @Override
        protected void onPreExecute() {
            setVisibility(1);


        }
        @Override
        protected String doInBackground(List<NameValuePair>... loginDetail) {
            AndroidHttpClient client = AndroidHttpClient.newInstance("AndroidAgent");
            //HttpPost httpPost = new HttpPost("http://planourmeet.herokuapp.com/register");
            HttpPost httpPost = new HttpPost("http://regainmemory.org/bics/registration/index.php");
            Log.d("background", "inBackground start");
            HttpResponse response;
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(loginDetail[0]));
                response = client.execute(httpPost);
                return EntityUtils.toString(response.getEntity());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                client.close();
                Log.d("background","inBackground end");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            setVisibility(0);
            Log.d("result", result);
            responseString = result;
            //responseString="yes";
            try {
                JSONObject json = new JSONObject(responseString);
                responseString = json.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(responseString.equals("yes")){
                //Save the shared Preference thing
                LoginDataSavedEditor.putString("loginDone", "yes");
                LoginDataSavedEditor.commit();
                startActivity(intent);
            }
            else if (responseString.equals("notUnique")){
                Toast.makeText(getApplicationContext(), "Email Id is already used", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Unable to connnect to Server", Toast.LENGTH_SHORT).show();
            }

            if(result.equals("Exception while sending")){
                Toast.makeText(getApplicationContext(), "Unable to connnect to internet , please retry", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
