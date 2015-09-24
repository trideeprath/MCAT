package com.sugoilabs.bics;

/*
Questionaire.
The person will be asked two question about brain injury. He has to answer both of them.
The first  question does not carry any points.
The second question if answered yes give -2 points.
 */

import android.app.Activity;
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
import android.widget.Toast;


public class Step1 extends ActionBarActivity implements View.OnClickListener {

    Toolbar toolbar;
    int[][] answerMatrix;
    Intent intent;
    final String Prefs_Score = "Prefs_Score";
    final String Prefs_Pre_Post_Injury = "Prefs_Pre_Post";
    SharedPreferences scoreSharedPreference;
    SharedPreferences prePostInjurySharedPreference;
    SharedPreferences.Editor editor;
    final String Prefs_TBI = "TBI";
    SharedPreferences TBISharedPreference;
    SharedPreferences.Editor TBIeditor;
    SharedPreferences.Editor prePostEditor;
    Button q1_yes;
    Button q1_no;
    Button q2_yes;
    Button q2_no;
    Button done_Button;
    private RadioGroup radioGroupButton;
    private RadioButton selectedAnswerButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        answerMatrix = new int[2][2];
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step1);
        //toolbar = (Toolbar) findViewById(R.id.app_bar);
        //setSupportActionBar(toolbar);

        //Setup Shared Preference to save points
        scoreSharedPreference = getSharedPreferences(Prefs_Score, 0);
        editor = scoreSharedPreference.edit();
        editor.clear().commit();

        TBISharedPreference = getSharedPreferences(Prefs_TBI, 0);
        TBIeditor = TBISharedPreference.edit();
        TBIeditor.clear().commit();

        prePostInjurySharedPreference = getSharedPreferences(Prefs_Pre_Post_Injury, 0);
        prePostEditor = prePostInjurySharedPreference.edit();
        prePostEditor.clear().commit();


        q1_yes = (Button) findViewById(R.id.q1_button_yes);
        q1_no = (Button) findViewById(R.id.q1_button_no);
        q2_yes = (Button) findViewById(R.id.q2_button_yes);
        q2_no = (Button) findViewById(R.id.q2_button_no);
        done_Button = (Button) findViewById(R.id.step1_button_done);

        q1_yes.setOnClickListener(this);
        q1_no.setOnClickListener(this);
        q2_yes.setOnClickListener(this);
        q2_no.setOnClickListener(this);
        done_Button.setOnClickListener(this);

        intent = new Intent(this, PictureFlash.class);
        //intent = new Intent(this, SelectShapes.class);

        radioGroupButton = (RadioGroup) findViewById(R.id.radio_pre_post_injury);

    }




    private void saveScoreToSharedPreference() {

        if (answerMatrix[0][0] == 1) {
            TBIeditor.putBoolean("TBISuffered", true);
            TBIeditor.commit();
        } else {
            TBIeditor.putBoolean("TBISuffered", false);
            TBIeditor.commit();
        }

        if (answerMatrix[1][0] == 1) {
            editor.putString("score", String.valueOf(0));
            editor.commit();
            //reportScoreSave.scoreSave.add("Questionnaire One:-2");
        } else {
            editor.putString("score", String.valueOf(0));
            editor.commit();
            //reportScoreSave.scoreSave.add("Questionnaire One:0");
        }

        int selected = radioGroupButton.getCheckedRadioButtonId();
        if(selected == R.id.radio_pre_injury){
            prePostEditor.putString("pre_post_injury", "Pre-Injury Screening");
            prePostEditor.commit();
        }
        else if (selected == R.id.radio_post_injury){
            prePostEditor.putString("pre_post_injury", "Post-Injury Screening");
            prePostEditor.commit();
        }


    }

    private boolean answeredAllQuestions() {
        boolean status = false;
        boolean a = (answerMatrix[0][0] == 1) || (answerMatrix[0][1] == 1);
        boolean b = (answerMatrix[1][0] == 1) || (answerMatrix[1][1] == 1);

        if (a && b) {
            status = true;
        }
        return status;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_step1, menu);
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
        switch (v.getId()) {
            case R.id.q1_button_yes:
                if (answerMatrix[0][1] == 1) {
                    q1_no.setTextColor(getResources().getColor(R.color.primary_text));
                    answerMatrix[0][1] = 0;
                    q1_yes.setTextColor(getResources().getColor(R.color.accent));
                }
                q1_yes.setTextColor(getResources().getColor(R.color.accent));
                answerMatrix[0][0] = 1;
                if (answeredAllQuestions()) {
                    saveScoreToSharedPreference();
                    Log.d("Score 1", "Done" + scoreSharedPreference.getString("score", "9"));
                    startActivity(intent);
                }
                break;
            case R.id.q1_button_no:
                if (answerMatrix[0][0] == 1) {
                    q1_yes.setTextColor(getResources().getColor(R.color.primary_text));
                    answerMatrix[0][0] = 0;
                }
                q1_no.setTextColor(getResources().getColor(R.color.accent));
                answerMatrix[0][1] = 1;
                if (answeredAllQuestions()) {
                    saveScoreToSharedPreference();
                    Log.d("Score 1", "Done" + scoreSharedPreference.getString("score", "9"));
                    startActivity(intent);
                }
                break;

            case R.id.q2_button_yes:
                if (answerMatrix[1][1] == 1) {
                    q2_no.setTextColor(getResources().getColor(R.color.primary_text));
                    answerMatrix[1][1] = 0;
                    q2_yes.setTextColor(getResources().getColor(R.color.accent));
                }
                q2_yes.setTextColor(getResources().getColor(R.color.accent));
                answerMatrix[1][0] = 1;
                if (answeredAllQuestions()) {
                    saveScoreToSharedPreference();
                    Log.d("Score 1", "Done" + scoreSharedPreference.getString("score", "9"));
                    startActivity(intent);
                }
                break;
            case R.id.q2_button_no:
                if (answerMatrix[1][0] == 1) {
                    q2_yes.setTextColor(getResources().getColor(R.color.primary_text));
                    answerMatrix[1][0] = 0;
                }
                q2_no.setTextColor(getResources().getColor(R.color.accent));
                answerMatrix[1][1] = 1;
                if (answeredAllQuestions()) {
                    saveScoreToSharedPreference();
                    Log.d("Score 1", "Done" + scoreSharedPreference.getString("score", "9"));
                    startActivity(intent);
                }
                break;
            case R.id.step1_button_done:
                //Toast.makeText(this, "Done" ,Toast.LENGTH_SHORT).show();
                if (answeredAllQuestions()) {
                    saveScoreToSharedPreference();
                    Log.d("Score 1", "Done" + scoreSharedPreference.getString("score", "9"));
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Please Answer All questions", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    @Override
    public void onBackPressed() {
    }

    /*
    @Override
    public void onPause(){
        super.onPause();
        Log.d("OnPause", "Step1 on pause");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.d("OnStop","Step1 on Stop");
        //startActivity(new Intent(this , Welcome.class));
        //finish();
    }

    @Override
    protected  void onResume(){
        super.onResume();
        Log.d("OnResume", "Step1 On Resume called");
        //startActivity(new Intent(this , Welcome.class));
    }
    */


}
