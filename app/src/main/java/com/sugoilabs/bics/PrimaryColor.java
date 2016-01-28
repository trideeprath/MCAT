package com.sugoilabs.bics;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.Locale;

/*
Primary colors.
The person has to speak or type as many major colors as he can. He would be given maximum of three minutes and max of two chances.
Color more than or equal to 7 time is less than 30 sec the clock will be running 2 one point
30 to 60 sec 1 point
6 0 to 180 sec .5 points

Color > 7 && Time <30second ==> 2points
???

 */

public class PrimaryColor extends ActionBarActivity implements View.OnClickListener{

    Toolbar toolbar;
    LinearLayout answersLayout;
    LinearLayout inputLayout;
    LinearLayout timerLayout;
    LinearLayout nextLayout;
    TextView timerText;
    EditText answerTextView;
    Button start;
    Button done;
    Button typeInButton;
    Button speakOutButton;
    long millisInFuture = 180000;
    public int timeTaken;
    public int currentTimeTaken;
    int answerCount;
    boolean timerClockStatus= true;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    final myTimer waveTimer = new myTimer(millisInFuture +100 , 1000);
    final myTimer waveTimer2 = new myTimer(millisInFuture+100 , 1000);
    boolean speechToTextStarted= false;
    String answer_colors="";
    int points;
    final String Prefs_Score = "Prefs_Score";
    SharedPreferences scoreSharedPreference;
    SharedPreferences.Editor editor;


    ArrayList<Button> answerButtons = new ArrayList<Button>();
    Button none;
    LinearLayout buttonsLayout;
    String currentAnswerString="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.primary_color);
        //toolbar = (Toolbar) findViewById(R.id.app_bar);
        //setSupportActionBar(toolbar);
        initializeLayouts();
        resetText();
        showInstructionDialogBox();
        scoreSharedPreference = getSharedPreferences(Prefs_Score,0);
        editor = scoreSharedPreference.edit();

    }


    public void showInstructionDialogBox(){
        MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(this).title(R.string.instruction).contentColor(Color.RED).content(R.string.primary_color_instruction).positiveText(R.string.next).cancelable(false);
        dialogBuilder.show();
        dialogBuilder.callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                Log.d("OK ", "great");
                buttonsLayout.setVisibility(View.VISIBLE);
                timeTaken=0;
                waveTimer.start();
            }
        });

    }

    private void initializeLayouts() {
        answersLayout = (LinearLayout) findViewById(R.id.primary_color_answers);
        answersLayout.setVisibility(View.INVISIBLE);

        timerLayout = (LinearLayout) findViewById(R.id.primary_color_timer);
        timerText = (TextView) findViewById(R.id.timerText);

        answerTextView = (EditText) findViewById(R.id.primary_color_answers_texview);

        inputLayout = (LinearLayout) findViewById(R.id.primary_color_input);

        nextLayout =(LinearLayout)findViewById(R.id.primary_color_start_done);
        //nextLayout.setVisibility(View.INVISIBLE);

        typeInButton = (Button) findViewById(R.id.primary_color_typein);
        typeInButton.setOnClickListener(this);

        speakOutButton = (Button) findViewById(R.id.primary_color_speakout);
        speakOutButton.setOnClickListener(this);

        done = (Button) findViewById(R.id.primary_color_done);
        done.setOnClickListener(this);



        none = (Button) findViewById(R.id.primary_color_none);
        none.setOnClickListener(this);


        String identifierString;
        for(int i =1 ; i<19 ; i++) {
            identifierString = "color_word"+String.valueOf(i);
            answerButtons.add((Button) findViewById(getResources().getIdentifier(identifierString, "id", getPackageName())));
            answerButtons.get(i-1).setOnClickListener(this);
        }

        buttonsLayout = (LinearLayout) findViewById(R.id.primary_button_layout);
        buttonsLayout.setVisibility(View.INVISIBLE);


    }

    protected void resetText() {
        timerText.setText("Time Left: " + millisInFuture / 1000);
    }

    public class myTimer extends CountDownTimer  {

        private long millisActual;

        public myTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            millisActual = millisInFuture;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (millisUntilFinished <= millisActual) {
                timerText.setText("Time Left: " + millisUntilFinished / 1000);
                timeTaken = (int) (180 - millisUntilFinished / 1000);
            }
        }

        @Override
        public void onFinish()
        {

        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_step5, menu);
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

        if(viewForAnswerButtonsClicked(v)){
            Button button = (Button) v;
            Log.d("text",button.getText().toString());
            answersLayout.setVisibility(View.VISIBLE);
            done.setVisibility(View.VISIBLE);
            none.setVisibility(View.INVISIBLE);
            currentAnswerString = answerTextView.getText().toString();
            currentAnswerString = currentAnswerString + button.getText().toString() +" ";
            answerTextView.setText(currentAnswerString);
            button.setVisibility(View.INVISIBLE);
        }

        if(v.getId() == R.id.primary_color_typein ){
            if(answerCount >1){
                Toast.makeText(this, R.string.attemps_over, Toast.LENGTH_SHORT).show();
            }
            else{
                showTextDialogAndStartTimer();
            }
        }

        if(v.getId() == R.id.primary_color_speakout){

            if(answerCount >1){
                Toast.makeText(this, R.string.attemps_over, Toast.LENGTH_SHORT).show();
            }
            else{
                showSpeakDialogAndStartTimer();
            }

        }

        if(v.getId() == R.id.primary_color_done || v.getId() == R.id.primary_color_none){
            Log.d("answers", answerTextView.getText().toString());
            Log.d("timeTaken", String.valueOf(timeTaken));
            waveTimer.cancel();
            points = calculatePoints(answerTextView.getText().toString(),currentTimeTaken);
            Log.d("score", String.valueOf(points));
            saveToSharedPreference(points);
            startActivity(new Intent(this, GroupMatching.class));
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
            Toast.makeText(this,"Points is: " + String.valueOf(points)+ " Total Score is:" + ScoreToast +" Time Taken: "+ String.valueOf(timeTaken) ,Toast.LENGTH_LONG).show();
            //reportScoreSave.scoreSave.add(getResources().getString(R.string.declaratory_language_memory)+":" +String.valueOf(points)+ ","+currentTimeTaken+"sec");
            reportScoreSave.scoreSave.add(getResources().getString(R.string.declaratory_language_memory_new)+":" +String.valueOf(points)+ ","+currentTimeTaken+"sec");
            reportTimeSave.timeSave.add(timeTaken);
            //startActivity(new Intent(this, TraumaticInjury.class));
        }
    }

    private int calculatePoints(String colors , int time) {
        int point=0;
        String baseColors = getResources().getString(R.string.base_colors);
        String[] baseColorArray = baseColors.split(",");
        ArrayList<String> baseColorArrayList = new ArrayList<String>();

        String[] colorsAnswerdArray = colors.split(" ");
        ArrayList<String> colorsAnswerArrayList = new ArrayList<String>();

        for(String color: baseColorArray)
            baseColorArrayList.add(color);

        for(String color: colorsAnswerdArray)
            colorsAnswerArrayList.add(color.toLowerCase());

        Log.d("answered Array List" , colorsAnswerArrayList.toString());
        Log.d("base Array List" , baseColorArrayList.toString());

        if(colorsAnswerArrayList.size()>=3){
            int flag=0;
            for(String color : colorsAnswerArrayList){
                if(baseColorArrayList.contains(color)){
                    flag++;
                }
            }
            if(flag >=7 ){
                 point=5;
            }
            else if(flag >= 5){
                point = 3;
            }
            else if(flag >=3){
                point = 2;
            }
        }

        else{
            point =0;
        }

        return point;
    }

    private void showSpeakDialogAndStartTimer() {
        speechToTextStarted = true;
        if(answerCount ==0 ){
            timeTaken =0;
            waveTimer.start();
        }
        if(answerCount ==1){
            timeTaken=0;
            Toast.makeText(this,R.string.final_attempt, Toast.LENGTH_SHORT).show();
            waveTimer.cancel();
            waveTimer2.start();
        }

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,R.string.speak_words);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 5000);
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), "Speech not supported", Toast.LENGTH_SHORT).show();
            if(answerCount == 0)
                waveTimer.cancel();
            if(answerCount ==1)
                waveTimer2.cancel();
            speechToTextStarted =false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        speechToTextStarted=false;
        if(answerCount == 0)
            waveTimer.cancel();
        if(answerCount == 1)
            waveTimer2.cancel();
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    answersLayout.setVisibility(View.VISIBLE);
                    answer_colors = result.get(0);
                    currentTimeTaken = timeTaken;
                    answerTextView.setText(answer_colors);
                    timerLayout.setVisibility(View.INVISIBLE);
                    nextLayout.setVisibility(View.VISIBLE);
                    answerCount++;
                }
                break;
            }
        }
    }

    private void showTextDialogAndStartTimer() {
        answersLayout.setVisibility(View.INVISIBLE);
        timerLayout.setVisibility(View.VISIBLE);
        Log.d("answerCount",String.valueOf(answerCount));

        if(answerCount ==0 ){
            timeTaken=0;
            waveTimer.start();
        }
        if(answerCount ==1){
            timeTaken=0;
            Toast.makeText(this,R.string.final_attempt, Toast.LENGTH_SHORT).show();
            waveTimer.cancel();
            waveTimer2.start();
        }

        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.primary_color_typein_prompt, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView.findViewById(R.id.primary_color_prompt_editext);
        // set dialog message
        alertDialogBuilder.setCancelable(false).
                setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.d("Words", userInput.getText().toString());
                                answersLayout.setVisibility(View.VISIBLE);
                                answer_colors = userInput.getText().toString();
                                currentTimeTaken = timeTaken;
                                answerTextView.setText(answer_colors);
                                timerLayout.setVisibility(View.INVISIBLE);
                                nextLayout.setVisibility(View.VISIBLE);
                                answerCount++;
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                waveTimer.cancel();
                                waveTimer2.cancel();
                            }
                        });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }



    @Override
    public void onBackPressed() {
        if(speechToTextStarted==true){
            if(answerCount ==0)
                waveTimer.cancel();

            if(answerCount ==1)
                waveTimer2.cancel();
        }
    }

}
