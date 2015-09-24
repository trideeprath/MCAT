package com.sugoilabs.bics;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
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
import java.util.Locale;

public class DelayedRecallStory extends ActionBarActivity implements View.OnClickListener{


    Toolbar toolbar;
    Button done;
    Button typeInButton;
    Button speakOutButton;
    final String Prefs_Score = "Prefs_Score";
    SharedPreferences scoreSharedPreference;
    SharedPreferences.Editor editor;
    LinearLayout answerLayout;
    EditText answerText;
    Intent nextActivity;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    int attempt;
    int answerCount=0;
    ArrayList<String> answersArrayList = new ArrayList<String>();
    int score=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delayed_recall_story);
        showInstructionDialogBox();
        initializeLayout();
    }


    private void initializeLayout() {

        answerLayout = (LinearLayout) findViewById(R.id.delayed_recall_story_answer_layout);
        answerLayout.setVisibility(View.INVISIBLE);

        answerText = (EditText) findViewById(R.id.delayed_recall_story_answer_textview);

        done = (Button)findViewById(R.id.delayed_recall_done);
        done.setOnClickListener(this);

        typeInButton = (Button)findViewById(R.id.delayed_recall_answer_typein);
        typeInButton.setOnClickListener(this);

        speakOutButton = (Button)findViewById(R.id.delayed_recall_answer_speakout);
        speakOutButton.setOnClickListener(this);

        scoreSharedPreference = getSharedPreferences(Prefs_Score,0);
        editor = scoreSharedPreference.edit();

    }

    public void showInstructionDialogBox(){
        MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(this).title(R.string.instruction).content(R.string.delayedRecallStoryOneInstruction).positiveText(R.string.next).cancelable(false);
        dialogBuilder.show();
        dialogBuilder.callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                Log.d("OK ", "great");
            }
        });

    }

    public void showInstructionDialogBoxForStoryTwo(){
        MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(this).title(R.string.instruction).content(R.string.delayedRecallStoryTwoInstruction).positiveText(R.string.next).cancelable(false);
        dialogBuilder.show();
        dialogBuilder.callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                Log.d("OK ", "great");
            }
        });

    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.delayed_recall_answer_typein){
            showAnswerTextPrompt();
        }
        if(v.getId() == R.id.delayed_recall_answer_speakout){
            showAnswerSpeakPrompt();
        }
        if(v.getId() == R.id.delayed_recall_done) {

            if (answerText.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please provide answer" , Toast.LENGTH_SHORT).show();
            } else {
                if (answerCount < 1) {
                    setContentView(R.layout.activity_delayed_recall_story);
                    answersArrayList.add(answerText.getText().toString());
                    initializeLayout();
                    showInstructionDialogBoxForStoryTwo();
                    done.setText("NEXT");
                    score = calculateScore(answersArrayList.get(0), 1);

                } else {
                    answersArrayList.add(answerText.getText().toString());
                    score = score + calculateScore(answersArrayList.get(1), 2);
                    int points = score;
                    Log.d("score", String.valueOf(points));
                    saveToSharedPreference(points);
                    startActivity(new Intent(this, SelectLetter.class));
                }
                answerCount++;
            }
        }
    }


    private int calculateScore(String sentence, int audioNumber) {
        String baseString;
        double similarityScore;
        String answerString = sentence;
        answerString = answerString.toLowerCase();
        int score;
        if (audioNumber == 1) {
            baseString = "John's neighbor Robert visit his vacation home every other month, He bring white dog and golf club, Laptop and  plenty of cigars.";
        } else {
            baseString = "Mary had loaned her lawnmower, shovel, weed spray and pair of gloves to the her nephew, last year. Her nephew only returned Lawnmower, She decided not to loan any more.";
        }


        similarityScore = similarity(baseString,answerString );
        Log.d("similarityScore", String.valueOf(similarityScore));

        if(similarityScore > Double.valueOf(".90"))
            score = 5;
        else if(similarityScore> Double.valueOf(".80") )
            score = 4;
        else if(similarityScore> Double.valueOf(".70") )
            score = 3;
        else if(similarityScore> Double.valueOf(".60") )
            score = 2;
        else if(similarityScore> Double.valueOf(".50") )
            score = 1;
        else
            score = 0;

        Log.d("score", String.valueOf(score));
        return score;
    }


    public double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if(s1.length() < s2.length()) { // longer should always have greater length
            longer = s2;
            shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) {
            return 1.0; /* both strings are zero length */
        }
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;

    }

    public int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();
        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
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
            reportScoreSave.scoreSave.add(getResources().getString(R.string.delayed_story_recall) + ":" + String.valueOf(points));
        }
    }

    private void showAnswerSpeakPrompt() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,R.string.speak_words);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 5000);
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), "Speech not supported", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAnswerTextPrompt() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.step2_typein_prompt, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView.findViewById(R.id.step2_prompt_editext);
        // set dialog message
        alertDialogBuilder.setCancelable(false).
                setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                Log.d("Words", userInput.getText().toString());
                                answerLayout.setVisibility(View.VISIBLE);
                                answerText.setText(userInput.getText().toString());
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    answerLayout.setVisibility(View.VISIBLE);
                    answerText.setText(result.get(0));
                }
                break;
            }
        }
    }


    private int calculatePoints() {
        int point=0;

        return point;
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_delayed_recall_story, menu);
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
    public void onBackPressed() {
    }

}
