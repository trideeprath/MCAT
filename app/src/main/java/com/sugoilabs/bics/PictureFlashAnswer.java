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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.Locale;


public class PictureFlashAnswer extends ActionBarActivity implements View.OnClickListener{

    Toolbar toolbar;
    Button done;
    Button typeInButton;
    Button speakOutButton;
    final String Prefs_Score = "Prefs_Score";
    SharedPreferences scoreSharedPreference;
    SharedPreferences.Editor editor;
    LinearLayout answerLayout ;
    LinearLayout nextButtonLayout;
    EditText answerText;
    Intent nextActivity;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    int attempt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_flash_answer);
        //toolbar = (Toolbar) findViewById(R.id.app_bar);
        //setSupportActionBar(toolbar);
        showInstructionDialogBox();
        initializeLayout();

    }

    private void initializeLayout() {

        answerLayout = (LinearLayout) findViewById(R.id.picture_flash_answer_answer_layout);
        answerLayout.setVisibility(View.INVISIBLE);

        answerText = (EditText) findViewById(R.id.picture_flash_answer_answers_texview);

        done = (Button)findViewById(R.id.picture_flash_answer_done);
        done.setOnClickListener(this);
        done.setVisibility(View.INVISIBLE);

        nextButtonLayout = (LinearLayout) findViewById(R.id.picture_flash_answer_start_done);
        nextButtonLayout.setVisibility(View.INVISIBLE);

        typeInButton = (Button)findViewById(R.id.picture_flash_answer_typein);
        typeInButton.setOnClickListener(this);

        speakOutButton = (Button)findViewById(R.id.picture_flash_answer_speakout);
        speakOutButton.setOnClickListener(this);

        scoreSharedPreference = getSharedPreferences(Prefs_Score,0);
        editor = scoreSharedPreference.edit();

    }

    public void showInstructionDialogBox(){
        MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(this).title(R.string.instruction).content(R.string.pictureFlashAnswerInstruction).positiveText(R.string.next).cancelable(false);
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
        getMenuInflater().inflate(R.menu.menu_picture_flash_answer, menu);
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
        if(v.getId() == R.id.picture_flash_answer_typein){
            showAnswerTextPrompt();

        }
        if(v.getId() == R.id.picture_flash_answer_speakout){
            showAnswerSpeakPrompt();
        }
        if(v.getId() == R.id.picture_flash_answer_done){
            int points = calculatePoints();
            Log.d("score", String.valueOf(points));
            saveToSharedPreference(points);
            startActivity(new Intent(this, SelectShapes.class));
        }
    }

    private int calculatePoints() {
        int point;
        int flag=0;
        String[] answerArray = answerText.getText().toString().split(" ");
        ArrayList<String> answerArrayList = new ArrayList<String>();
        //String[] baseAnswerArray = getResources().getString(R.string.base_picture_flash_answer).toString().split(",");
        String[] baseAnswerArray = getResources().getString(R.string.base_picture_flash_answer).toString().split(",");
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
            case 4:
                point = 4;
                break;
            case 3:
                point = 3;
                break;
            case 2:
                point = 2;
                break;
            default:
                point =0;
                break;
        }

        return point;
    }

    private void saveToSharedPreference(int points) {
        double totalScore=0;
        points = points *2;
        if(scoreSharedPreference.contains("score")){
            totalScore = Double.valueOf(scoreSharedPreference.getString("score","0")) + Integer.valueOf(points);
            editor.putString("score",String.valueOf(totalScore));
            editor.commit();
            String ScoreToast = String.valueOf(totalScore);
            if(ScoreToast.length() >3){
                ScoreToast = ScoreToast.substring(0,3);
            }
            Log.d("score",String.valueOf(totalScore));
            Toast.makeText(this,"Points is: " + String.valueOf(points)+ " Total Score is:" + ScoreToast,Toast.LENGTH_SHORT).show();
            reportScoreSave.scoreSave.add(getResources().getString(R.string.visual_name_delayed_recall)+":" +String.valueOf(points));
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
                                done.setVisibility(View.VISIBLE);
                                nextButtonLayout.setVisibility(View.VISIBLE);
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
                    done.setVisibility(View.VISIBLE);
                    nextButtonLayout.setVisibility(View.VISIBLE);
                }
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
    }
}
