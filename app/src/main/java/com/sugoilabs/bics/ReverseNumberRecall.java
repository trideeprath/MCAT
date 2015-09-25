package com.sugoilabs.bics;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class ReverseNumberRecall extends ActionBarActivity implements View.OnClickListener {

    Toolbar toolbar;
    LinearLayout mediaLayout;
    EditText mediaStatusTextView;
    Button start;
    Button done;
    MediaPlayer player;
    TextView text_shown;
    LinearLayout answerLayout;
    Button type;
    Button speak;
    Handler seekHandler = new Handler();
    int audioPlayedCount =0;
    int audioNumber=0;
    int score = 0;
    Intent nextActivityIntent;
    int totalNumberAudioCount=0;
    int correctAnswerCount=0;
    ArrayList<Integer> numbersArrayList = new ArrayList<Integer>();
    ArrayList<String> numbersAnswerArrayList = new ArrayList<String>();
    private final int REQ_CODE_SPEECH_INPUT = 100;
    final String Prefs_Score = "Prefs_Score";
    SharedPreferences scoreSharedPreference;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reverse_number_recall);
        showInstructionDialogBox();
        initializeLayouts();
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_number_recall, menu);
        return true;
    }
    */

    private void initializeLayouts() {
        mediaLayout = (LinearLayout) findViewById(R.id.number_recall_media);
        start = (Button) findViewById(R.id.number_recall_start);
        start.setOnClickListener(this);
        done = (Button) findViewById(R.id.number_recall_done);
        done.setOnClickListener(this);
        player = MediaPlayer.create(this, R.raw.audio_apple);
        mediaStatusTextView = (EditText) findViewById(R.id.number_recall_playing_status);
        mediaStatusTextView.setText("PRESS START");
        setAllAudio();

        answerLayout = (LinearLayout) findViewById(R.id.number_recall_answer_layout);
        type = (Button) findViewById(R.id.number_recall_typein);
        speak = (Button) findViewById(R.id.number_recall_speakout);
        type.setOnClickListener(this);
        speak.setOnClickListener(this);
        scoreSharedPreference = getSharedPreferences(Prefs_Score,0);
        editor = scoreSharedPreference.edit();

    }

    private void setAllAudio() {
        numbersArrayList.add(R.raw.reverse_218);
        numbersArrayList.add(R.raw.reverse_435);
        numbersArrayList.add(R.raw.reverse_6958);
        numbersArrayList.add(R.raw.reverse_8439);
        numbersArrayList.add(R.raw.reverse_43679);
        numbersArrayList.add(R.raw.reverse_74392);
        numbersArrayList.add(R.raw.reverse_642198);
        numbersArrayList.add(R.raw.reverse_965721);
        numbersArrayList.add(R.raw.reverse_7435218);
        numbersArrayList.add(R.raw.reverse_9765832);


        //Setting up the answer list

        numbersAnswerArrayList.add("812");
        numbersAnswerArrayList.add("534");
        numbersAnswerArrayList.add("8596");
        numbersAnswerArrayList.add("9348");
        numbersAnswerArrayList.add("97634");
        numbersAnswerArrayList.add("29347");
        numbersAnswerArrayList.add("891246");
        numbersAnswerArrayList.add("127569");
        numbersAnswerArrayList.add("8125347");
        numbersAnswerArrayList.add("2385679");

    }


    private void showInstructionDialogBox() {
        MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(this).title(R.string.instruction).content(R.string.reverseNumberRecallInstruction).positiveText(R.string.next).cancelable(false);
        dialogBuilder.show();
        dialogBuilder.callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                Log.d("OK ", "great");
            }
        });
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
        if(view.getId() == R.id.number_recall_start){
            if(audioPlayedCount > numbersArrayList.size()){
                done.setVisibility(View.VISIBLE);
            }
            else{
                playAudioWithCount(audioPlayedCount);
            }

            //audioPlayedCount++;

        }

        if(view.getId() == R.id.number_recall_typein){
            showAnswerTextPrompt();
        }
        if(view.getId() == R.id.number_recall_speakout){
            showAnswerSpeakPrompt();
        }
        if(view.getId() == R.id.number_recall_done){

            if(mediaStatusTextView.getText().toString().matches("[0-9]+") ) {
            if(audioPlayedCount < numbersArrayList.size()) {
                //start.setVisibility(View.VISIBLE);

                done.setVisibility(View.INVISIBLE);
                answerLayout.setVisibility(View.INVISIBLE);

                String answerProvided = mediaStatusTextView.getText().toString();
                if (answerProvided.equals(numbersAnswerArrayList.get(audioPlayedCount - 1))) {
                    correctAnswerCount++;
                    Toast.makeText(this, "correct answer " + correctAnswerCount, Toast.LENGTH_SHORT).show();
                    Log.d("given, answer", mediaStatusTextView.getText().toString() + " "+
                            numbersAnswerArrayList.get(audioPlayedCount -1));
                } else {
                    Toast.makeText(this, "Incorrect answer", Toast.LENGTH_SHORT).show();
                    Log.d("given, answer", mediaStatusTextView.getText().toString() + " " +
                            numbersAnswerArrayList.get(audioPlayedCount - 1));
                }

                playAudioWithCount(audioPlayedCount);
                disableEditing();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mediaStatusTextView.getWindowToken(), 0);
            }
            else{
                Log.d("all questions ","over" );
                int points = calculatePoints();
                Log.d("score", String.valueOf(points));
                saveToSharedPreference(points);
                startActivity(new Intent(this, PrimaryColor.class));

            }

        }
        else {
            Toast.makeText(this, "Please enter a valid number",Toast.LENGTH_SHORT).show();
        }
        }

    }

    private void playAudioWithCount(int audioPlayedCount) {
        AsyncTask<String, Integer, Void> Response = new playAudio().execute();
    }

    private class playAudio extends AsyncTask<String, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setMediaPlayingText("Playing...");
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    // this code will be executed after 2 seconds
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            start.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }, 0);

        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                player = MediaPlayer.create(ReverseNumberRecall.this, numbersArrayList.get(audioPlayedCount));
                player.start();
                audioNumber++;
                int count=0;
                publishProgress(audioNumber);
                Thread.sleep(player.getDuration());



            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            setMediaPlayingText("Type or Speak the number");
            audioPlayedCount++;
            answerLayout.setVisibility(View.VISIBLE);
            done.setVisibility(View.VISIBLE);



        }

        //Not being called
        protected void onProgressUpdate(Integer... progress){
            int progressWord = progress[0];
            //setWordNumberText(progressWord);
        }
    }

    private void setMediaPlayingText(String status) {
        mediaStatusTextView.setText(status);
    }

    private void showAnswerSpeakPrompt() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
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
        View promptsView = li.inflate(R.layout.number_typein_prompt, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView.findViewById(R.id.prompt_editext);
        // set dialog message
        alertDialogBuilder.setCancelable(false).
                setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                Log.d("Words", userInput.getText().toString());
                                answerLayout.setVisibility(View.VISIBLE);
                                mediaStatusTextView.setText(userInput.getText().toString());
                                done.setVisibility(View.VISIBLE);
                                start.setVisibility(View.INVISIBLE);
                                enableEditing();
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


    private void enableEditing() {
        mediaStatusTextView.setFocusable(true);
        mediaStatusTextView.setClickable(true);
        mediaStatusTextView.setFocusableInTouchMode(true);
        mediaStatusTextView.setFocusable(true);
        mediaStatusTextView.setCursorVisible(true);

    }

    private void disableEditing(){
        mediaStatusTextView.setFocusable(false);
        mediaStatusTextView.setClickable(false);
        mediaStatusTextView.setFocusableInTouchMode(false);
        mediaStatusTextView.setFocusable(false);
        mediaStatusTextView.setCursorVisible(false);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    answerLayout.setVisibility(View.VISIBLE);
                    mediaStatusTextView.setText(result.get(0));
                    done.setVisibility(View.VISIBLE);
                    start.setVisibility(View.INVISIBLE);
                    enableEditing();

                }
                break;
            }
        }
    }

    private int calculatePoints() {
        int point=0;
        if(correctAnswerCount >5){
            point = 5;
        }
        else{
            point = (int)correctAnswerCount/2;
        }

        if(point >5){
            point =5;
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
            //reportScoreSave.scoreSave.add(getResources().getString(R.string.number_recall_revers) + ":" + String.valueOf(points));
            reportScoreSave.scoreSave.add(getResources().getString(R.string.number_recall_reverse_new)+":" +String.valueOf(points));

        }

    }


    @Override
    public void onBackPressed() {
    }

}
