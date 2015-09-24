package com.sugoilabs.bics;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.Timer;
import java.util.TimerTask;


public class SelectLetter extends ActionBarActivity implements View.OnClickListener {

    Button selectVButton;
    TextView letter_text_view;
    int timeForShapeToBeVisible = 1200;
    int timeFlash =200;
    int correctAnswerCount=0;
    final String Prefs_Score = "Prefs_Score";
    SharedPreferences scoreSharedPreference;
    SharedPreferences.Editor editor;
    int size =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_letter);
        initializeLayout();
        showInstructionDialogBox();
    }

    private void initializeLayout() {

        selectVButton = (Button) findViewById(R.id.select_letter_tap);
        selectVButton.setOnClickListener(this);

        letter_text_view = (TextView) findViewById(R.id.select_letter_flash_text_view);

        scoreSharedPreference = getSharedPreferences(Prefs_Score,0);
        editor = scoreSharedPreference.edit();


    }

    public void showInstructionDialogBox() {

        MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(this).title(R.string.instruction).content(R.string.selectLetterInstruction).positiveText(R.string.next).cancelable(false);
        dialogBuilder.show();
        dialogBuilder.callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                Log.d("OK ", "great");
                AsyncTask<String, Integer, Void> Response = new playSelectLetterGame().execute();
            }
        });


    }

    private class playSelectLetterGame extends AsyncTask<String, Integer, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                //publishProgress(100);
                Thread.sleep(2000);
                changeTextViewLetter("A");
                Thread.sleep(timeForShapeToBeVisible);
                changeTextViewLetter("");
                Thread.sleep(timeFlash);
                changeTextViewLetter("W");
                Thread.sleep(timeForShapeToBeVisible);
                changeTextViewLetter("");
                Thread.sleep(timeFlash);
                changeTextViewLetter("V");
                Thread.sleep(timeForShapeToBeVisible);
                changeTextViewLetter("");
                Thread.sleep(timeFlash);
                //publishProgress(140);
                changeTextViewLetter(">");
                //publishProgress(100);
                Thread.sleep(timeForShapeToBeVisible);
                changeTextViewLetter("");
                Thread.sleep(timeFlash);
                changeTextViewLetter("V");
                Thread.sleep(timeForShapeToBeVisible);
                changeTextViewLetter("");
                Thread.sleep(timeFlash);
                changeTextViewLetter("A");
                Thread.sleep(timeForShapeToBeVisible);
                changeTextViewLetter("");
                Thread.sleep(timeFlash);
                changeTextViewLetter("V");
                Thread.sleep(timeForShapeToBeVisible);
                changeTextViewLetter("");
                Thread.sleep(timeFlash);
                changeTextViewLetter("U");
                Thread.sleep(timeForShapeToBeVisible);
                changeTextViewLetter("");
                //publishProgress(140);
                Thread.sleep(timeFlash);
                changeTextViewLetter("<");
                //publishProgress(100);
                Thread.sleep(timeForShapeToBeVisible);
                changeTextViewLetter("");
                Thread.sleep(timeFlash);
                changeTextViewLetter("V");
                Thread.sleep(timeForShapeToBeVisible);
                changeTextViewLetter("");
                Thread.sleep(timeFlash);
                changeTextViewLetter("Y");
                Thread.sleep(timeForShapeToBeVisible);
                changeTextViewLetter("");
                Thread.sleep(timeFlash);
                changeTextViewLetter("A");
                Thread.sleep(timeForShapeToBeVisible);
                changeTextViewLetter("");
                Thread.sleep(timeFlash);
                changeTextViewLetter("W");
                Thread.sleep(timeForShapeToBeVisible);
                changeTextViewLetter("");
                Thread.sleep(timeFlash);
                changeTextViewLetter("V");
                Thread.sleep(timeForShapeToBeVisible);
                changeTextViewLetter("");
                //publishProgress(140);
                Thread.sleep(timeFlash);
                changeTextViewLetter(">");
                //publishProgress(100);
                Thread.sleep(timeForShapeToBeVisible);
                changeTextViewLetter("");
                Thread.sleep(timeFlash);
                changeTextViewLetter("V");
                Thread.sleep(timeForShapeToBeVisible);
                changeTextViewLetter("");
                //publishProgress(140);
                Thread.sleep(timeFlash);
                changeTextViewLetter("<");
                //publishProgress(100);
                Thread.sleep(timeForShapeToBeVisible);
                changeTextViewLetter("");
                Thread.sleep(timeFlash);
                changeTextViewLetter("W");
                Thread.sleep(timeForShapeToBeVisible);
                changeTextViewLetter("");
                Thread.sleep(timeFlash);
                changeTextViewLetter("V");
                Thread.sleep(timeForShapeToBeVisible);
                changeTextViewLetter("");
                Thread.sleep(timeFlash);
                changeTextViewLetter("A");
                Thread.sleep(timeForShapeToBeVisible);
                changeTextViewLetter("");
                Thread.sleep(timeFlash);
                changeTextViewLetter("V");
                Thread.sleep(timeForShapeToBeVisible);
                changeTextViewLetter("");
                Thread.sleep(timeFlash);
                changeTextViewLetter("V");
                Thread.sleep(timeForShapeToBeVisible);
                changeTextViewLetter("");
                Thread.sleep(timeFlash);
                changeTextViewLetter("W");
                Thread.sleep(timeForShapeToBeVisible);
                changeTextViewLetter("");
                Thread.sleep(timeFlash);
                changeTextViewLetter("A");
                Thread.sleep(timeForShapeToBeVisible);
                changeTextViewLetter("");
                Thread.sleep(timeFlash);
                changeTextViewLetter("V");
                Thread.sleep(timeForShapeToBeVisible);
                changeTextViewLetter("");
                Thread.sleep(timeFlash);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            double points = calculatePoints();
            Log.d("score", String.valueOf(points));
            saveToSharedPreference(points);
            callNextActivity();

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.d("MyAsyncTask", "onProgressUpdate - " + values[0]);
            letter_text_view.setTextSize(values[0]);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_letter, menu);
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
        if(v.getId() == R.id.select_letter_tap){
            if(letter_text_view.getText().equals("V")){
                correctAnswerCount++;
            }
            else{
                correctAnswerCount--;
            }
            Log.d("correctAnswerCount",String.valueOf(correctAnswerCount));
        }

    }

    public void changeTextSize(final int size){
        this.size = size;
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                letter_text_view.setTextSize(size);
                // Code here will run in UI thread
            }
        });
    }

    public void startLetterFlash() throws InterruptedException {
        Thread.sleep(2000);
        changeTextViewLetter("A");
        Thread.sleep(timeForShapeToBeVisible);
        changeTextViewLetter("");
        Thread.sleep(timeFlash);
        changeTextViewLetter("W");
        Thread.sleep(timeForShapeToBeVisible);
        changeTextViewLetter("");
        Thread.sleep(timeFlash);
        changeTextViewLetter("V");
        Thread.sleep(timeForShapeToBeVisible);
        changeTextViewLetter("");
        Thread.sleep(timeFlash);
        changeTextViewLetter(">");
        changeTextSize(100);
        Thread.sleep(timeForShapeToBeVisible);
        changeTextViewLetter("");
        Thread.sleep(timeFlash);
        changeTextViewLetter("V");
        Thread.sleep(timeForShapeToBeVisible);
        changeTextViewLetter("");
        Thread.sleep(timeFlash);
        changeTextViewLetter("A");
        Thread.sleep(timeForShapeToBeVisible);
        changeTextViewLetter("");
        Thread.sleep(timeFlash);
        changeTextViewLetter("V");
        Thread.sleep(timeForShapeToBeVisible);
        changeTextViewLetter("");
        Thread.sleep(timeFlash);
        changeTextViewLetter("U");
        Thread.sleep(timeForShapeToBeVisible);
        changeTextViewLetter("");
        changeTextSize(140);
        Thread.sleep(timeFlash);
        changeTextViewLetter("<");
        changeTextSize(100);
        Thread.sleep(timeForShapeToBeVisible);
        changeTextViewLetter("");
        Thread.sleep(timeFlash);
        changeTextViewLetter("V");
        Thread.sleep(timeForShapeToBeVisible);
        changeTextViewLetter("");
        Thread.sleep(timeFlash);
        changeTextViewLetter("Y");
        Thread.sleep(timeForShapeToBeVisible);
        changeTextViewLetter("");
        Thread.sleep(timeFlash);
        changeTextViewLetter("A");
        Thread.sleep(timeForShapeToBeVisible);
        changeTextViewLetter("");
        Thread.sleep(timeFlash);
        changeTextViewLetter("W");
        Thread.sleep(timeForShapeToBeVisible);
        changeTextViewLetter("");
        Thread.sleep(timeFlash);
        changeTextViewLetter("V");
        Thread.sleep(timeForShapeToBeVisible);
        changeTextViewLetter("");
        changeTextSize(140);
        Thread.sleep(timeFlash);
        changeTextViewLetter(">");
        changeTextSize(100);
        Thread.sleep(timeForShapeToBeVisible);
        changeTextViewLetter("");
        Thread.sleep(timeFlash);
        changeTextViewLetter("V");
        Thread.sleep(timeForShapeToBeVisible);
        changeTextViewLetter("");
        changeTextSize(140);
        Thread.sleep(timeFlash);
        changeTextViewLetter("<");
        changeTextSize(100);
        Thread.sleep(timeForShapeToBeVisible);
        changeTextViewLetter("");
        Thread.sleep(timeFlash);
        changeTextViewLetter("W");
        Thread.sleep(timeForShapeToBeVisible);
        changeTextViewLetter("");
        Thread.sleep(timeFlash);
        changeTextViewLetter("V");
        Thread.sleep(timeForShapeToBeVisible);
        changeTextViewLetter("");
        Thread.sleep(timeFlash);
        changeTextViewLetter("A");
        Thread.sleep(timeForShapeToBeVisible);
        changeTextViewLetter("");
        Thread.sleep(timeFlash);
        changeTextViewLetter("V");
        Thread.sleep(timeForShapeToBeVisible);
        changeTextViewLetter("");
        Thread.sleep(timeFlash);
        changeTextViewLetter("V");
        Thread.sleep(timeForShapeToBeVisible);
        changeTextViewLetter("");
        Thread.sleep(timeFlash);
        changeTextViewLetter("W");
        Thread.sleep(timeForShapeToBeVisible);
        changeTextViewLetter("");
        Thread.sleep(timeFlash);
        changeTextViewLetter("A");
        Thread.sleep(timeForShapeToBeVisible);
        changeTextViewLetter("");
        Thread.sleep(timeFlash);
        changeTextViewLetter("V");
        Thread.sleep(timeForShapeToBeVisible);
        changeTextViewLetter("");
        Thread.sleep(timeFlash);

    }

    public void changeTextViewLetter(final String letter) {

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // this code will be executed after 2 seconds

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                            if(letter.equals("<") || letter.equals(">")){
                                letter_text_view.setTextSize(300);
                                letter_text_view.setText(letter);
                            }
                            else{
                                letter_text_view.setTextSize(200);
                                letter_text_view.setText(letter);
                            }

                    }
                });
            }
        }, 200);
    }

    private double calculatePoints() {
        double point=0;
        point = correctAnswerCount * 0.50;
        return point;
    }

    private void saveToSharedPreference(double points) {
        double totalScore=0;
        points = points *2;
        if(points<0){
            points =0;
        }
        if(scoreSharedPreference.contains("score")){
            totalScore = Double.valueOf(scoreSharedPreference.getString("score","0")) + Double.valueOf(points);
            editor.putString("score",String.valueOf(totalScore));
            editor.commit();
            Log.d("score",String.valueOf(totalScore));
            String pointsToast= String.valueOf(points);
            if(pointsToast.length() >3){
               pointsToast =  pointsToast.substring(0,3);
            }
            String ScoreToast = String.valueOf(totalScore);
            if(ScoreToast.length() >3){
                ScoreToast = ScoreToast.substring(0,3);
            }
            Toast.makeText(this, "Points is: " + pointsToast + " Total Score is:" + ScoreToast, Toast.LENGTH_SHORT).show();
            //reportScoreSave.scoreSave.add(getResources().getString(R.string.visual_motor_coordination)+":" +String.valueOf(points));
            reportScoreSave.scoreSave.add(getResources().getString(R.string.visual_motor_coordination_new)+":" +String.valueOf(points));
        }

    }

    private void callNextActivity(){
        startActivity(new Intent(this, WordFlashAnswerDelayed.class));
    }

    @Override
    public void onBackPressed() {
    }

}
