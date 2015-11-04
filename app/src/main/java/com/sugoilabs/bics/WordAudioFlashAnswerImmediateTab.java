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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sugoilabs.bics.R;

import java.util.ArrayList;

public class WordAudioFlashAnswerImmediateTab extends ActionBarActivity implements View.OnClickListener {

    final String Prefs_Score = "Prefs_Score";
    SharedPreferences scoreSharedPreference;
    SharedPreferences.Editor editor;
    LinearLayout answerLayout ;
    LinearLayout nextButtonLayout;

    EditText answerText;
    Intent nextActivity;
    ArrayList<Button> answerButtons = new ArrayList<Button>();
    Button done;
    Button none;
    String currentAnswerString="";
    LinearLayout buttonLayout;
    wordFlashData wcd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_audio_flash_answer_immediate_tab);
        initializeLayout();
        showInstructionDialogBox();
        wcd = new wordFlashData(this);
    }


    private void initializeLayout() {

        answerLayout = (LinearLayout) findViewById(R.id.word_audio_immediate_answer_answer_layout_tab);
        answerLayout.setVisibility(View.INVISIBLE);

        answerText = (EditText) findViewById(R.id.word_audio_immediate_answer_answers_textview_tab);

        done = (Button)findViewById(R.id.word_audio_immediate_answer_done);
        done.setOnClickListener(this);
        done.setVisibility(View.INVISIBLE);

        none = (Button) findViewById(R.id.waiit_none);
        none.setOnClickListener(this);

        scoreSharedPreference = getSharedPreferences(Prefs_Score, 0);
        editor = scoreSharedPreference.edit();

        //notes = (TextView)findViewById(getResources().getIdentifier(VIEW_NAME, "id", getPackageName()));

        String identifierString;
        for(int i =1 ; i<6 ; i++) {
            identifierString = "waait_word"+String.valueOf(i);
            answerButtons.add((Button) findViewById(getResources().getIdentifier(identifierString, "id", getPackageName())));
            answerButtons.get(i-1).setOnClickListener(this);
            answerButtons.get(i-1).setText(wcd.answerString.split(",")[i - 1].toUpperCase());
        }

        buttonLayout = (LinearLayout)findViewById(R.id.waiit_buttons_layout);

    }


    public void showInstructionDialogBox(){
        MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(this).title(R.string.instruction).content(R.string.audioWordAnswerImmediateInstruction).positiveText(R.string.next).cancelable(false);
        dialogBuilder.show();
        dialogBuilder.callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                Log.d("OK ", "great");
                buttonLayout.setVisibility(View.VISIBLE);
            }
        });


    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_word_audio_flash_answer_immediate_tab, menu);
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
    public void onClick(View view) {
        if(viewForAnswerButtonsClicked(view)){
            Button button = (Button) view;
            Log.d("text",button.getText().toString());
            answerLayout.setVisibility(View.VISIBLE);
            done.setVisibility(View.VISIBLE);
            none.setVisibility(View.INVISIBLE);
            currentAnswerString = answerText.getText().toString();
            currentAnswerString = currentAnswerString + button.getText().toString() +" ";
            answerText.setText(currentAnswerString);
            button.setVisibility(View.INVISIBLE);
        }
        if(view.getId() == R.id.word_audio_immediate_answer_done || view.getId() == R.id.waiit_none){
            Log.d("done","done");
            int points = calculatePoints();
            Log.d("score", String.valueOf(points));
            saveToSharedPreference(points);

            //original flow
            startActivity(new Intent(this, Orientation.class));
            //startActivity(new Intent(this, WordFlashAnswerDelayed.class));
        }
    }


    private void saveToSharedPreference(int points) {
        double totalScore=0;
        points = points *2;
        if(scoreSharedPreference.contains("score")){
            totalScore = Double.valueOf(scoreSharedPreference.getString("score","0")) + Integer.valueOf(points);
            editor.putString("score",String.valueOf(totalScore));
            editor.commit();
            Log.d("score",String.valueOf(totalScore));
            String ScoreToast = String.valueOf(totalScore);
            if(ScoreToast.length() >3){
                ScoreToast = ScoreToast.substring(0,3);
            }
            Toast.makeText(this, "Points is: " + String.valueOf(points) + " Total Score is:" + ScoreToast, Toast.LENGTH_SHORT).show();
            //reportScoreSave.scoreSave.add(getResources().getString(R.string.auditory_immediate_recall) + ":" + String.valueOf(points));
            reportScoreSave.scoreSave.add(getResources().getString(R.string.auditory_immediate_recall_new) + ":" + String.valueOf(points));

        }
    }


    private int calculatePoints() {
        int point;
        int flag=0;
        String[] answerArray = answerText.getText().toString().split(" ");
        ArrayList<String> answerArrayList = new ArrayList<String>();
        String[] baseAnswerArray = wordFlashData.answerString.split(",");
        //String[] baseAnswerArray = getResources().getString(R.string.base_word_audio_flash_answer).toString().split(",");
        ArrayList<String> baseAnswerArrayList = new ArrayList<String>();

        for(String ans: answerArray){
            answerArrayList.add(ans.toLowerCase());
        }

        for(String ans: baseAnswerArray){
            baseAnswerArrayList.add(ans.toLowerCase());
        }

        if(answerArrayList.size() != 0){
            for(String ans : baseAnswerArrayList){
                if(answerArrayList.contains(ans)){
                    flag++;
                }
            }
        }

        switch(flag){
            case 5:
                point = 5;
                break;
            case 4:
                point = 4;
                break;
            case 3:
                point = 3;
                break;
            default:
                point =0;
                break;
        }

        return point;
    }



    private boolean viewForAnswerButtonsClicked(View view) {
        boolean flag = false;
        for(int i =0 ; i<answerButtons.size() ; i++){
            if(view.getId() == answerButtons.get(i).getId()){
                flag = true;
                return flag;
            }
        }
        return flag;
    }

    @Override
    public void onBackPressed() {
    }


}
