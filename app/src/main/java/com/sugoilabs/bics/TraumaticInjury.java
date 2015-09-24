package com.sugoilabs.bics;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class TraumaticInjury extends ActionBarActivity implements View.OnClickListener {

    private RadioGroup radioGroupButton;
    private RadioButton selectedAnswerButton;
    private RadioGroup question2radioGroupButton;
    private RadioButton question2selectedAnswerButton;
    private Button done;
    Toolbar toolbar;
    final String Prefs_Injury = "Within2Days";
    final String Prefs_Injury_Difference = "OccurrenceDifference";
    SharedPreferences scoreSharedPreference;
    SharedPreferences injuryOccurenceDifference;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor2;

    final String Prefs_TBI = "TBI";
    SharedPreferences TBISharedPreference;
    boolean hasSufferedTBI;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traumatic_injury);
        //toolbar = (Toolbar) findViewById(R.id.app_bar);
        //setSupportActionBar(toolbar);
        initializeLayout();
        scoreSharedPreference = getSharedPreferences(Prefs_Injury,0);
        editor = scoreSharedPreference.edit();
        editor.clear().commit();
        editor.putBoolean("traumaticInjuryWithin2day", false);
        editor.commit();

        injuryOccurenceDifference = getSharedPreferences(Prefs_Injury_Difference,0);
        editor2 = injuryOccurenceDifference.edit();
        editor2.clear().commit();
        editor2.putBoolean("injuryOccurrenceDifference", false);
        editor2.commit();

        TBISharedPreference = getSharedPreferences(Prefs_TBI,0);
        hasSufferedTBI = TBISharedPreference.getBoolean("TBISuffered",false);
        Log.d("has Sufferend TBI", String.valueOf(hasSufferedTBI));
        if(hasSufferedTBI == false){
            intent = new Intent(this, PrimaryColor.class);
            startActivity(intent);
        }

    }

    private void initializeLayout() {
        radioGroupButton = (RadioGroup) findViewById(R.id.radio_injury_occurrence);
        done = (Button) findViewById(R.id.traumatic_injury_button_done);
        done.setOnClickListener(this);

        question2radioGroupButton = (RadioGroup) findViewById(R.id.radio_injury_difference);

    }

    private void saveToSharedPreference() {
        editor.putBoolean("traumaticInjuryWithin2day", true);
        editor.commit();
    }

    private void saveQuestion2SharedPreference(){
        editor2.putBoolean("InjuryOccurrenceDifference3Months",true);
        editor2.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_traumatic_injury, menu);
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
        if(v.getId() == R.id.traumatic_injury_button_done){
            int selected = radioGroupButton.getCheckedRadioButtonId();
            selectedAnswerButton = (RadioButton) findViewById(selected);

            int question2Selected = question2radioGroupButton.getCheckedRadioButtonId();
            if(selected == R.id.radio2day){
                saveToSharedPreference();
            }
            if(question2Selected == R.id.radio_diff_1week || question2Selected == R.id.radio_diff_2week || question2Selected == R.id.radio_diff_1month || question2Selected == R.id.radio_diff_3month){
                saveQuestion2SharedPreference();
            }

            intent = new Intent(this, PrimaryColor.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
    }

}
