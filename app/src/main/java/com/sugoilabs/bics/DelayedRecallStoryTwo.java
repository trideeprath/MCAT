package com.sugoilabs.bics;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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

public class DelayedRecallStoryTwo extends ActionBarActivity implements View.OnClickListener {


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
    int storyOneScore =0;
    Intent intent;
    LinearLayout buttonLayout;
    storyData sd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delayed_recall_story_two);
        initializeLayout();
        showInstructionDialogBox();
        intent = getIntent();
        storyOneScore = intent.getIntExtra("StoryOneScore", 0);
        Log.d("StoryOneScore",String.valueOf(storyOneScore));
        sd = new storyData(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_delayed_recall_story_two, menu);
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


    public void showInstructionDialogBox(){
        MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(this).title(R.string.instruction).contentColor(Color.RED).content(R.string.delayedRecallStoryTwoInstructionNew).positiveText(R.string.next).cancelable(false);
        dialogBuilder.show();
        dialogBuilder.callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                Log.d("OK ", "great");
                buttonLayout.setVisibility(View.VISIBLE);
            }
        });

    }



    private void initializeLayout() {

        answerLayout = (LinearLayout) findViewById(R.id.delayed_recall_story_answer_answer_layout_tab);
        answerLayout.setVisibility(View.INVISIBLE);

        answerText = (EditText) findViewById(R.id.delayed_recall_story_answer_answers_textview_tab);

        done = (Button)findViewById(R.id.picture_flash_immediate_answer_done);
        done.setOnClickListener(this);
        done.setVisibility(View.INVISIBLE);

        none = (Button) findViewById(R.id.drst_none);
        none.setOnClickListener(this);

        scoreSharedPreference = getSharedPreferences(Prefs_Score, 0);
        editor = scoreSharedPreference.edit();

        //notes = (TextView)findViewById(getResources().getIdentifier(VIEW_NAME, "id", getPackageName()));

        String identifierString;
        for(int i =1 ; i<13 ; i++) {
            identifierString = "drst_word"+String.valueOf(i);
            answerButtons.add((Button) findViewById(getResources().getIdentifier(identifierString, "id", getPackageName())));
            answerButtons.get(i-1).setOnClickListener(this);
            answerButtons.get(i-1).setText(sd.answerStringArray.get(1).split(",")[i - 1]);

        }

        buttonLayout = (LinearLayout)findViewById(R.id.drst_button_layout);
        buttonLayout.setVisibility(View.INVISIBLE);

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
        if(view.getId() == R.id.picture_flash_immediate_answer_done || view.getId() == R.id.drst_none){
            Log.d("done","done");
            String sentence1 = answerText.getText().toString();
            //score = calculateScore(sentence1, audioNumber);
            int score = storyOneScore + calculateScoreByWords(sentence1, 2);
            Log.d("score", String.valueOf(score));
            saveToSharedPreference(score);
            //startActivity(new Intent(this, patternDraw.class));
            //startActivity(new Intent(this, WordAudioFlash.class));
            //startActivity(new Intent(this , SimilarPicture.class));
            //startActivity(new Intent(this, SelectLetter.class));
            startActivity(new Intent(this, VisualNumberRecall.class));
        }
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


    private int calculateScoreByWords(String sentence1, int audioNumber) {
        int score = 0;
        String baseString;
        String[] correctWords;

        if (audioNumber == 1) {
            baseString = "John's neighbor Robert visit his vacation home every other month, He bring white dog and golf club, Laptop and  plenty of cigars.";
            String[] correctWords1 = sd.answerStringArray.get(0).split(",");
            //String[] correctWords1 = {"John", "neighbour", "Robert", "vacation home", "other month", "white dog", "golf club", "laptop", "plenty", "cigar","every other","visit"};
            correctWords = correctWords1;
        } else {
            baseString = "Mary had loaned her lawnmower, shovel, weed spray and pair of gloves to the her nephew, last year. Her nephew only returned Lawnmower, She decided not to loan any more.";
            String[] correctWords1 = sd.answerStringArray.get(1).split(",");
            //String[] correctWords1 = {"Mary", "loaned", "lawnmower,", "lawnmower", "shovel", "weed spray", "gloves", "nephew", "last year", "returned", "loan"};
            correctWords = correctWords1;
        }
        String[] words = sentence1.split(" ");
        for (int i = 0; i < correctWords.length; i++) {
            if (sentence1.toLowerCase().contains(correctWords[i].toLowerCase())) {
                score++;
            }
        }

        if(score>10){
            score=10;
        }
        return score / 2;
    }


    private void saveToSharedPreference(int points) {
        double totalScore=0;
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
            reportScoreSave.scoreSave.add(getResources().getString(R.string.delayed_story_recall_new) + ":" + String.valueOf(points));
        }
    }


}
