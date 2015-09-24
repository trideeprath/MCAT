package com.sugoilabs.bics;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.RadialGradient;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.afollestad.materialdialogs.MaterialDialog;
import com.doomonafireball.betterpickers.datepicker.DatePickerBuilder;
import com.doomonafireball.betterpickers.datepicker.DatePickerDialogFragment;
import com.doomonafireball.betterpickers.radialtimepicker.RadialTimePickerDialog;


public class Clock extends ActionBarActivity implements View.OnClickListener,RadialTimePickerDialog.OnTimeSetListener{
    Toolbar toolbar;
    Button draw;
    Button done;
    LinearLayout answerLayout;
    LinearLayout TimeAskedLayout;
    TextView clockTimeAnswer;
    final String Prefs_Score = "Prefs_Score";
    SharedPreferences scoreSharedPreference;
    SharedPreferences.Editor editor;
    Intent nextActivity;
    private static final String FRAG_TAG_TIME_PICKER = "timePickerDialogFragment";
    String answerHour = null;
    String answerMinute = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        showInstructionDialogBox();
        initializeLayout();

    }

    private void initializeLayout() {
        draw = (Button) findViewById(R.id.clock_draw);
        draw.setOnClickListener(this);

        done = (Button) findViewById(R.id.clock_done);
        done.setOnClickListener(this);

        answerLayout = (LinearLayout) findViewById(R.id.clock_answers);
        answerLayout.setVisibility(View.INVISIBLE);

        TimeAskedLayout = (LinearLayout) findViewById(R.id.clock_time_layout);

        clockTimeAnswer = (TextView) findViewById(R.id.clock_answers_texview);

        scoreSharedPreference = getSharedPreferences(Prefs_Score,0);
        editor = scoreSharedPreference.edit();
    }

    public void showInstructionDialogBox(){
        MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(this).title(R.string.instruction).content(R.string.clockInstruction).positiveText(R.string.got_it).cancelable(false);
        dialogBuilder.show();
        dialogBuilder.callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                Log.d("OK ", "great");
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_clock, menu);
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
        if(v.getId()== R.id.clock_draw){
            RadialTimePickerDialog radialTimePickerDialog = RadialTimePickerDialog.newInstance(this,0,0,false);
            radialTimePickerDialog.show(getSupportFragmentManager(),FRAG_TAG_TIME_PICKER);
        }
        if(v.getId() == R.id.clock_done){
            int points = calculatePoints();
            Log.d("point", String.valueOf(points));
            saveToSharedPreference(points);
            nextActivity = new Intent(this, WordAudioFlash.class);
            startActivity(nextActivity);
        }
    }

    private int calculatePoints() {
        int point=0;
        if(answerHour == null && answerMinute == null){
            point =0;
        }
        else{
            if(answerHour.equals("8") && !answerMinute.equals("10")){
                point=1;
            }
            else if(!answerHour.equals("8") && answerMinute.equals("10")){
               point =1;
            }
            else if(answerHour.equals("8") && answerMinute.equals("10")){
                point = 3;
            }

        }
        return point;
    }

    private void saveToSharedPreference(int points) {
        int totalScore=0;
        if(scoreSharedPreference.contains("score")){
            totalScore = Integer.valueOf(scoreSharedPreference.getString("score","0")) + Integer.valueOf(points);
            editor.putString("score",String.valueOf(totalScore));
            editor.commit();
            Log.d("score",String.valueOf(totalScore));
        }
    }

    @Override
    public void onTimeSet(RadialTimePickerDialog radialTimePickerDialog, int hour, int minute) {
        TimeAskedLayout.setVisibility(View.INVISIBLE);
        answerLayout.setVisibility(View.VISIBLE);
        answerHour = String.valueOf(hour);
        answerMinute = String.valueOf(minute);
        clockTimeAnswer.setText("Answer: "+ hour + " : " + minute);

    }

    @Override
    public void onBackPressed() {
    }
}
