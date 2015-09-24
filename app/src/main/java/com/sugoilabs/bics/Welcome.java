package com.sugoilabs.bics;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class Welcome extends ActionBarActivity implements View.OnClickListener {

    Button getStarted;
    protected static final int SUB_ACTIVITY_REQUEST_CODE = 100;
    TextView disclaimer_text;
    final String Prefs_Score = "Prefs_Score";
    final String Prefs_Pre_Post_Injury = "Prefs_Pre_Post";
    SharedPreferences scoreSharedPreference;
    SharedPreferences prePostInjurySharedPreference;
    SharedPreferences.Editor editor;
    final String Prefs_TBI = "TBI";
    SharedPreferences TBISharedPreference;
    SharedPreferences.Editor TBIeditor;
    SharedPreferences.Editor prePostEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_tab);
        getStarted = (Button)findViewById(R.id.get_started);
        getStarted.setOnClickListener(this);
        disclaimer_text = (TextView) findViewById(R.id.disclaimer_text);
        disclaimer_text.setOnClickListener(this);
        SpannableString content = new SpannableString(getResources().getString(R.string.read_disclaimer));
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        disclaimer_text.setText(content);

    }

    @Override
    protected  void onResume(){
        super.onResume();
        Log.d("OnResume", "Welcome On Resume called");
        scoreSharedPreference = getSharedPreferences(Prefs_Score, 0);
        editor = scoreSharedPreference.edit();
        editor.clear().commit();

        TBISharedPreference = getSharedPreferences(Prefs_TBI, 0);
        TBIeditor = TBISharedPreference.edit();
        TBIeditor.clear().commit();

        prePostInjurySharedPreference = getSharedPreferences(Prefs_Pre_Post_Injury, 0);
        prePostEditor = prePostInjurySharedPreference.edit();
        prePostEditor.clear().commit();

        reportScoreSave.scoreSave.clear();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
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
        if(v.getId() == R.id.get_started){

            //Intent nextActivity = new Intent(this,FalsePositiveCaution.class);
            Intent nextActivity = new Intent(this,Registration.class);
            startActivity(nextActivity);

        }
        if(v.getId() == R.id.disclaimer_text){
            Intent i = new Intent(Welcome.this,TermsCondition.class);
            startActivityForResult(i, SUB_ACTIVITY_REQUEST_CODE);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SUB_ACTIVITY_REQUEST_CODE){
            Intent nextActivity = new Intent(this,FalsePositiveCaution.class);
            //Intent nextActivity = new Intent(this,Step4.class);
            startActivity(nextActivity);
        }
    }

    @Override
    public void onBackPressed() {
    }
}
