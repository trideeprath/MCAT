package com.sugoilabs.bics;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


public class FinalScreen extends ActionBarActivity implements View.OnClickListener{


    final String Prefs_Score = "Prefs_Score";
    final String Prefs_Injury = "Within2Days";
    final String Prefs_TBI = "TBI";
    final String Prefs_Injury_Difference = "OccurrenceDifference";
    SharedPreferences scoreSharedPreference;
    SharedPreferences injurySharedPreference;
    SharedPreferences TBISharedPreference;
    SharedPreferences injuryOccurrenceDifferenceSharedPreference;

    boolean hasSufferedTBI;
    boolean injury;
    boolean injuryDifference3Months;
    String points;
    double finalScore;
    String finalResultText;
    String cautionResultText="";
    TextView resultTextView;
    TextView cautionResultTextView;
    ArrayList<String> reportScore;
    Button reportButton;
    Button exitButton;
    protected static final int SUB_ACTIVITY_REQUEST_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_screen);

        reportButton = (Button) findViewById(R.id.final_report_button);
        exitButton = (Button) findViewById(R.id.final_exit_button);
        reportButton.setOnClickListener(this);
        exitButton.setOnClickListener(this);
        reportScore = reportScoreSave.scoreSave;

        Log.d("reportScore",reportScore.toString());

        resultTextView = (TextView)findViewById(R.id.result_textview);
        cautionResultTextView = (TextView) findViewById(R.id.caution_resulttextview);
        scoreSharedPreference = getSharedPreferences(Prefs_Score,0);
        points = scoreSharedPreference.getString("score","0");
        Log.d("finalScore", points);

        injurySharedPreference = getSharedPreferences(Prefs_Injury,0);
        injury = injurySharedPreference.getBoolean("traumaticInjuryWithin2day",false);
        Log.d("injury occured", String.valueOf(injury));

        TBISharedPreference = getSharedPreferences(Prefs_TBI,0);
        hasSufferedTBI = TBISharedPreference.getBoolean("TBISuffered",false);
        Log.d("has Sufferend TBI", String.valueOf(hasSufferedTBI));

        injuryOccurrenceDifferenceSharedPreference = getSharedPreferences(Prefs_Injury_Difference,0);
        injuryDifference3Months = injuryOccurrenceDifferenceSharedPreference.getBoolean("InjuryOccurrenceDifference3Months",false);
        Log.d("injury Occurrence Difference" , String.valueOf(injuryDifference3Months));


        finalScore = Double.valueOf(points);

        String finalScoreString = String.valueOf(finalScore);
        if(finalScoreString.length() >3){
            finalScoreString = finalScoreString.substring(0,4);
        }

        if(injuryDifference3Months){
            finalResultText="BICS Recommends: You have suffered Traumatic Brain Injury, Please consult brain injury or concussion specialist.";
        }
        else if(finalScore >=15){
            if(injury){
              finalResultText="You have Scored "+ finalScoreString + " on Brain injury screening test";
              cautionResultText= "Caution: Brain Screening Test is not accurate within 2 days of injury, repeat the test 1 week after";
            }
            else if(hasSufferedTBI){
                finalResultText="You have Scored "+ finalScoreString + " on Brain injury screening Test";
                //cautionResultText= "BICS recommends you to see a physician for a possible brain injury with cognitive impairment";
                cautionResultText= "BICS Recommends: You have suffered Traumatic Brain Injury, Please consult brain injury or concussion specialist.";
            }
            else if(!hasSufferedTBI){
                finalResultText="You have Scored "+ finalScoreString + " on Brain injury screening Test";
                //cautionResultText= "BICS recommends you to see a physician for a possible brain injury with cognitive impairment";
                cautionResultText= "BICS Recommends: Please Email and save your score for future reference. Repeat the BICS after 2 years is also recommended.";
            }
            else{
                finalResultText="You have Scored "+ finalScoreString + " on Brain injury screening test";
            }
        }
        else{
            finalResultText="You have Scored "+ finalScoreString + " on Brain injury screening Test";
            if(hasSufferedTBI == true) {
                if (finalScore > 13) {
                    finalResultText = finalResultText + "\n BICS Recommends: You have suffered Traumatic Brain Injury, Please consult brain injury or concussion specialist.";
                } else {
                    finalResultText = finalResultText + "\n BICS Recommends: You have suffered Traumatic Brain Injury, Please consult brain injury or concussion specialist.";
                }
            }
        }

        resultTextView.setText(finalResultText);
        cautionResultTextView.setText(cautionResultText);

        Intent i = new Intent(FinalScreen.this,reportDisplay.class);
        i.putExtra("final_point", points);
        startActivityForResult(i, SUB_ACTIVITY_REQUEST_CODE);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_final_screen, menu);
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
        if(v.getId() == R.id.final_report_button){
            Intent i = new Intent(FinalScreen.this,reportDisplay.class);
            i.putExtra("final_point",points);
            startActivityForResult(i, SUB_ACTIVITY_REQUEST_CODE);

        }
        if(v.getId() == R.id.final_exit_button){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SUB_ACTIVITY_REQUEST_CODE){

        }
    }

    @Override
    public void onBackPressed() {
    }

}
