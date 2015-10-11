package com.sugoilabs.bics;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
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

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class Orientation extends ActionBarActivity implements View.OnClickListener {


    Toolbar toolbar;
    TextView questionTextView;
    Button next;
    EditText answerEditText;
    Button type;
    Button speak;
    int questionCount =0;
    int audioNumber=0;
    int score = 0;
    Intent nextActivityIntent;
    int correctAnswerCount=0;
    List<String> questionArrayList;
    ArrayList<String> answerAnswerArrayList = new ArrayList<String>();
    private final int REQ_CODE_SPEECH_INPUT = 100;
    final String Prefs_Score = "Prefs_Score";
    SharedPreferences scoreSharedPreference;
    SharedPreferences.Editor editor;
    String currentAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orientation);
        showInstructionDialogBox();
        //Initializing
        initializeLayouts();
    }

    private void showInstructionDialogBox() {
        MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(this).title(R.string.instruction).content(R.string.orientation_instruction).positiveText(R.string.next).cancelable(false);
        dialogBuilder.show();
        dialogBuilder.callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                Log.d("OK ", "great");
            }
        });
    }


    private void initializeLayouts() {
        setAllQuestionsAndAnswers();
        next = (Button) findViewById(R.id.orientation_next_button);
        next.setOnClickListener(this);
        questionTextView = (TextView) findViewById(R.id.orientation_question);
        questionTextView.setText(questionArrayList.get(0));

        answerEditText = (EditText) findViewById(R.id.orientation_answer);
        answerEditText.setInputType(InputType.TYPE_CLASS_NUMBER);

        type = (Button) findViewById(R.id.orientation_typein);
        speak = (Button) findViewById(R.id.orientation_speakout);
        type.setOnClickListener(this);
        speak.setOnClickListener(this);
        scoreSharedPreference = getSharedPreferences(Prefs_Score,0);
        editor = scoreSharedPreference.edit();

    }

    private void setAllQuestionsAndAnswers() {
        questionArrayList = Arrays.asList(getResources().getStringArray(R.array.orientation_questions1));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_orientation, menu);
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
        if(view.getId() == R.id.orientation_next_button){
            if(questionCount < (questionArrayList.size()-1)){

                // get the answer and calculate the score
                currentAnswer = answerEditText.getText().toString();
                if(answerCorrect(questionCount)){
                    correctAnswerCount++;
                    Log.d("answer","correct");
                }
                if(questionCount == 4){
                    saveToSharedPreference(calculatePoints(),1);
                    correctAnswerCount=0;
                }

                questionCount++;

                //set the next question
                questionTextView.setText(questionArrayList.get(questionCount));

                //clear the answer layout
                answerEditText.setText("");
                if(questionCount == (questionArrayList.size() -2) ){
                    next.setText("NEXT");
                }
                setInputType(questionCount);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(answerEditText.getWindowToken(), 0);

            }
            else{
                currentAnswer = answerEditText.getText().toString();
                if(answerCorrect(questionCount)){
                    correctAnswerCount++;
                    Log.d("answer","correct");
                }
                // get the final points
                int points = calculatePoints();

                //save to shared Preference

                Log.d("score", String.valueOf(points));
                saveToSharedPreference(points,2);

                //start next activity
                //startActivity(new Intent(this, Step4.class));
                startActivity(new Intent(this, StoryPlayOne.class));
                //startActivity(new Intent(this, FinalScreen.class));
            }
        }

        if(view.getId() == R.id.orientation_typein){
            showAnswerTextPrompt();
        }

        if(view.getId() == R.id.orientation_speakout){
            showAnswerSpeakPrompt();
        }
    }

    private void setInputType(int questionCount) {

        if(questionCount == 0 || questionCount == 3 || questionCount == 4 || questionCount == 6){
            answerEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        else{
            answerEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        }
    }

    private void saveToSharedPreference(int points , int group_type) {
        double totalScore=0;
        points = points ;
        String moduleString;
        if(group_type ==1){
            moduleString = "Question Attention";
        }
        else{
            moduleString = "Semantic Question";
        }
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
            reportScoreSave.scoreSave.add(moduleString + " : " + String.valueOf(points));
        }

    }

    private int calculatePoints() {
        return correctAnswerCount*2;
    }

    private boolean answerCorrect(int questionCount) {
        boolean answerStatus = false;
        DateTime dateTime = new DateTime();
        int date = dateTime.getDayOfMonth();
        int month = dateTime.getMonthOfYear();
        int year = dateTime.getYear();
        int dayOfWeek = dateTime.getDayOfWeek();
        switch (questionCount){
            case 0: if(isNumeric(currentAnswer)) {
                        if (Integer.valueOf(currentAnswer) > 12 && Integer.valueOf(currentAnswer) < 100) {
                        answerStatus = true;
                        }
                    }
                break;
            case 1:
                    if(!currentAnswer.isEmpty()){
                        answerStatus = true;
                    }
                break;
            case 2:
                    String twoDaysBeforeToday = "";
                    if(!currentAnswer.isEmpty()){
                        if((dayOfWeek-3) >= 0){
                            twoDaysBeforeToday = getDayOfWeek(dayOfWeek-2);
                        }
                        else{
                            if(dayOfWeek-3 == -1){
                                twoDaysBeforeToday = "Sunday";
                            }
                            else{
                                twoDaysBeforeToday = "Saturday";
                            }
                        }
                        Log.d("twoDaysBeforeToday", twoDaysBeforeToday);
                        if(currentAnswer.equalsIgnoreCase(twoDaysBeforeToday)){
                            answerStatus = true;
                        }
                    }
                break;
            case 3: if(isNumeric(currentAnswer)){
                        if(String.valueOf(Integer.valueOf(currentAnswer)-1).equalsIgnoreCase(String.valueOf(date))){
                            answerStatus = true;
                        }
                    }
                break;
            case 5: if(!currentAnswer.isEmpty()){
                        if(currentAnswer.equalsIgnoreCase("thursday")){
                            answerStatus = true;
                        }
                    }
                break;
            case 6: if(!currentAnswer.isEmpty()){
                    if(isNumeric(currentAnswer)){
                        if(String.valueOf(currentAnswer).equalsIgnoreCase(String.valueOf(year-1))){
                            answerStatus = true;
                        }
                    }
                }
                break;
            case 4: if(!currentAnswer.isEmpty()){
                    if(isNumeric(currentAnswer)) {
                        if (Integer.valueOf(currentAnswer) > 30 && Integer.valueOf(currentAnswer) < 90) {
                            answerStatus = true;
                        }
                    }
                }
                break;
            case 7: if(!currentAnswer.isEmpty()){
                    if(currentAnswer.equalsIgnoreCase("california")){
                        answerStatus = true;
                    }
                }
                break;
            case 8: if(!currentAnswer.isEmpty()){
                    if(currentAnswer.equalsIgnoreCase("washington dc")){
                        answerStatus = true;
                    }
                }
                break;
            case 9:  if(!currentAnswer.isEmpty()){
                        if(currentAnswer.equalsIgnoreCase("paris")){
                         answerStatus = true;
                    }
                }
                break;
            default: answerStatus = false;
        }

        return answerStatus;
    }

    public static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(.\\d+)?");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    answerEditText.setText(result.get(0));
                }
                break;
            }
        }
    }


    private void showAnswerTextPrompt() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = null;

        if(questionCount == 0 || questionCount == 3 || questionCount == 4 || questionCount == 6){
            promptsView = li.inflate(R.layout.step2_number_typein_prompt , null);
        }
        else{
            promptsView = li.inflate(R.layout.step2_typein_prompt, null);
        }

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
                                answerEditText.setText(userInput.getText().toString());
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


    private String getDayOfWeek(int dayOfWeek) {
        String dayOfWeekString;
        switch (dayOfWeek) {
            case 0:
                dayOfWeekString = "Monday";
                break;
            case 1:
                dayOfWeekString = "Tuesday";
                break;
            case 2:
                dayOfWeekString = "Wednesday";
                break;
            case 3:
                dayOfWeekString = "Thursday";
                break;
            case 4:
                dayOfWeekString = "Friday";
                break;
            case 5:
                dayOfWeekString = "Saturday";
                break;
            case 6:
                dayOfWeekString = "Sunday";
                break;
            default:
                dayOfWeekString = "Null";
        }
        return dayOfWeekString;
    }



    @Override
    public void onBackPressed() {
    }

}
