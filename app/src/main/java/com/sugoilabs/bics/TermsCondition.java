package com.sugoilabs.bics;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class TermsCondition extends ActionBarActivity implements View.OnClickListener {
    protected static final int SUB_ACTIVITY_REQUEST_CODE = 100;
    Button agree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //InputStream ins = getResources().openRawResource(R.raw.disclaimer);
        setContentView(R.layout.activity_terms_condition);
        WebView webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        //webView.loadData(readTextFromResources(R.raw.disclaimer), "text/html" , "utf-8");
        if(isOnline()) {
            webView.loadUrl("http://regainmemory.org/bics/terms/eula.html");
        }
        else{
            Toast.makeText(this, "Unable to Connect to Internet", Toast.LENGTH_SHORT).show();
        }
        //webView.loadUrl("http://104.236.93.178/bics_disclaimer.html");
        agree =(Button)findViewById(R.id.agree);
        agree.setOnClickListener(this);
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

    private String readTextFromResources(int resourceId) throws IOException {
        InputStream raw = getResources().openRawResource(resourceId);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int i;
        i = raw.read();
        while (i != -1){
            stream.write(i);
            i = raw.read();
        }
        raw.close();
        return stream.toString();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_terms_condition, menu);
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
        if(v.getId() == R.id.agree){
            finish();
        }
    }
    @Override
    public void onBackPressed() {
    }
}
