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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class InitialQuestionForm extends ActionBarActivity implements View.OnClickListener {

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
    Button done_Button;
    private RadioGroup radioGroupButtonPrePost;
    private RadioButton selectedPrePost;
    private RadioGroup radioGroupButtonPostConsciousness;
    private RadioButton selectedLostConsciousness;
    LinearLayout secondQuestionLayout;
    Button done;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_initial_question_form);

        secondQuestionLayout = (LinearLayout) findViewById(R.id.post_lost_consciousness);
        secondQuestionLayout.setVisibility(View.INVISIBLE);
        done = (Button) findViewById(R.id.initial_question_form_done);
        done.setOnClickListener(this);
        selectedPrePost = (RadioButton) findViewById(R.id.radio_pre_injury_concussion);



        //Setup Shared Preference to save points
        scoreSharedPreference = getSharedPreferences(Prefs_Score, 0);
        editor = scoreSharedPreference.edit();
        editor.clear().commit();

        TBISharedPreference = getSharedPreferences(Prefs_TBI, 0);
        TBIeditor = TBISharedPreference.edit();
        TBIeditor.clear().commit();
        TBIeditor.putBoolean("TBISuffered", false);
        TBIeditor.commit();


        prePostInjurySharedPreference = getSharedPreferences(Prefs_Pre_Post_Injury, 0);
        prePostEditor = prePostInjurySharedPreference.edit();
        prePostEditor.clear().commit();

        prePostEditor.putString("pre_post_injury", "Pre-Concussion Injury");
        prePostEditor.commit();



        intent = new Intent(this, PictureFlash.class);

        saveScoreToSharedPreference();
        startActivity(intent);


        //startActivity(intent);
        //intent = new Intent(this, SelectShapes.class);

        /*
        radioGroupButtonPrePost = (RadioGroup) findViewById(R.id.radio_pre_post_concussion);
        //radioGroupButtonPrePost.setOnClickListener(this);
        radioGroupButtonPrePost.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                selectedPrePost = (RadioButton) findViewById(checkedId);
                Log.d("Check Changed", "Changed");
                if (selectedPrePost.getId() == R.id.radio_pre_injury_concussion) {
                    Log.d("PreInjury", "Yes");
                    if(secondQuestionLayout.getVisibility() == View.VISIBLE){
                        secondQuestionLayout.setVisibility(View.INVISIBLE);
                    }
                } else if (selectedPrePost.getId() == R.id.radio_post_injury_concussion) {
                    Log.d("PostInjury", "Yes");
                    if(secondQuestionLayout.getVisibility() == View.INVISIBLE) {
                        secondQuestionLayout.setVisibility(View.VISIBLE);
                    }
                }

            }
        });

        radioGroupButtonPostConsciousness = (RadioGroup) findViewById(R.id.radio_post_lost_consciousness);
        radioGroupButtonPostConsciousness.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                selectedLostConsciousness = (RadioButton) findViewById(checkedId);
                Log.d("Check Changed", "Changed");
                if (selectedLostConsciousness.getId() == R.id.radio_pre_injury_concussion) {
                    Log.d("PreInjury", "Yes");
                } else if (selectedLostConsciousness.getId() == R.id.radio_post_injury_concussion) {
                    Log.d("PostInjury", "Yes");
                    secondQuestionLayout.setVisibility(View.VISIBLE);
                }

            }
        });

        */

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_initial_question_form, menu);
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
        if (v.getId() == R.id.initial_question_form_done){
            saveScoreToSharedPreference();
            startActivity(intent);
        }
    }

    private void saveScoreToSharedPreference(){

        if (selectedPrePost.getId() == R.id.radio_post_injury_concussion) {
            TBIeditor.putBoolean("TBISuffered", true);
            TBIeditor.commit();
        } else {
            TBIeditor.putBoolean("TBISuffered", false);
            TBIeditor.commit();
        }

        editor.putString("score", String.valueOf(0));
        editor.commit();

        if (selectedPrePost.getId() == R.id.radio_pre_injury_concussion) {
            prePostEditor.putString("pre_post_injury", "Pre-Concussion Injury");
            prePostEditor.commit();
        } else if (selectedPrePost.getId() == R.id.radio_post_injury_concussion) {
            prePostEditor.putString("pre_post_injury", "Post-Concussion Injury");
            prePostEditor.commit();
        }

    }

}
