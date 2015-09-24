package com.sugoilabs.bics;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class SelectShapes extends ActionBarActivity implements View.OnClickListener {

    Button square;
    Button triangle;
    ImageView shape_image_view;
    int timeForShapeToBeVisible = 1200;
    int timeFlash =200;
    int correctAnswerCount=0;
    final String Prefs_Score = "Prefs_Score";
    SharedPreferences scoreSharedPreference;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_shapes);
        initializeLayout();
        showInstructionDialogBox();
    }


    private void initializeLayout() {

        square = (Button) findViewById(R.id.select_shapes_square);
        triangle = (Button) findViewById(R.id.select_shapes_triangle);
        square.setOnClickListener(this);
        triangle.setOnClickListener(this);

        shape_image_view = (ImageView) findViewById(R.id.select_shapes_flash_image_view);

        scoreSharedPreference = getSharedPreferences(Prefs_Score,0);
        editor = scoreSharedPreference.edit();


    }

    public void showInstructionDialogBox() {

        MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(this).title(R.string.instruction).content(R.string.selectShapeInstruction).positiveText(R.string.next).cancelable(false);
        dialogBuilder.show();
        dialogBuilder.callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                Log.d("OK ", "great");
                AsyncTask<String, Integer, Void> Response = new playSelectShapeGame().execute();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_shapes, menu);
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

    public void changeImageViewShape(final String shape) {

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // this code will be executed after 2 seconds

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (shape.equals("triangle")) {
                            shape_image_view.setImageDrawable(getResources().getDrawable(R.drawable.triangle));
                        }
                        if (shape.equals("square")) {
                            shape_image_view.setImageDrawable(getResources().getDrawable(R.drawable.square));
                        }
                        if (shape.equals("screen")) {
                            shape_image_view.setImageDrawable(getResources().getDrawable(R.drawable.screen));
                        }


                    }
                });
            }
        }, 200);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.select_shapes_square){
            if(shape_image_view.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.triangle).getConstantState())){
                correctAnswerCount++;
            }else{
                correctAnswerCount--;
            }
            Log.d("correctAnswerCount",String.valueOf(correctAnswerCount));
        }
    }

    private class playSelectShapeGame extends AsyncTask<String, Integer, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                startImageFlash();


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
    }

    public void startImageFlash() throws InterruptedException {
        Thread.sleep(2000);
        changeImageViewShape("square");
        Thread.sleep(timeForShapeToBeVisible);
        changeImageViewShape("screen");
        Thread.sleep(timeFlash);
        changeImageViewShape("triangle");
        Thread.sleep(timeForShapeToBeVisible);
        changeImageViewShape("screen");
        Thread.sleep(timeFlash);
        changeImageViewShape("square");
        Thread.sleep(timeForShapeToBeVisible);
        changeImageViewShape("screen");
        Thread.sleep(timeFlash);
        changeImageViewShape("triangle");
        Thread.sleep(timeForShapeToBeVisible);
        changeImageViewShape("screen");
        Thread.sleep(timeFlash);
        changeImageViewShape("triangle");
        Thread.sleep(timeForShapeToBeVisible);
        changeImageViewShape("screen");
        Thread.sleep(timeFlash);
        changeImageViewShape("square");
        Thread.sleep(timeForShapeToBeVisible);
        changeImageViewShape("screen");
        Thread.sleep(timeFlash);
        changeImageViewShape("triangle");
        Thread.sleep(timeForShapeToBeVisible);
        changeImageViewShape("screen");
        Thread.sleep(timeFlash);
        changeImageViewShape("square");
        Thread.sleep(timeForShapeToBeVisible);
        changeImageViewShape("screen");
        Thread.sleep(timeFlash);
        changeImageViewShape("square");
        Thread.sleep(timeForShapeToBeVisible);
        changeImageViewShape("screen");
        Thread.sleep(timeFlash);
        changeImageViewShape("triangle");
        Thread.sleep(timeForShapeToBeVisible);
        changeImageViewShape("screen");
        Thread.sleep(timeFlash);
        changeImageViewShape("triangle");
        Thread.sleep(timeForShapeToBeVisible);
        changeImageViewShape("screen");
        Thread.sleep(timeFlash);
        changeImageViewShape("square");
        Thread.sleep(timeForShapeToBeVisible);
        changeImageViewShape("screen");
        Thread.sleep(timeFlash);
        changeImageViewShape("triangle");
        Thread.sleep(timeForShapeToBeVisible);
        changeImageViewShape("screen");
        Thread.sleep(timeFlash);
        changeImageViewShape("square");
        Thread.sleep(timeForShapeToBeVisible);
        changeImageViewShape("screen");
        Thread.sleep(timeFlash);
        changeImageViewShape("triangle");
        Thread.sleep(timeForShapeToBeVisible);
        changeImageViewShape("screen");
        Thread.sleep(timeFlash);
        changeImageViewShape("triangle");
        Thread.sleep(timeForShapeToBeVisible);
        changeImageViewShape("screen");
        Thread.sleep(timeFlash);
        changeImageViewShape("triangle");
        Thread.sleep(timeForShapeToBeVisible);
        changeImageViewShape("screen");
        Thread.sleep(timeFlash);
        changeImageViewShape("square");
        Thread.sleep(timeForShapeToBeVisible);
        changeImageViewShape("screen");
        Thread.sleep(timeFlash);
        changeImageViewShape("square");
        Thread.sleep(timeForShapeToBeVisible);
        changeImageViewShape("screen");
        Thread.sleep(timeFlash);
        changeImageViewShape("square");
        Thread.sleep(timeForShapeToBeVisible);
        changeImageViewShape("screen");
        Thread.sleep(timeFlash);

    }

    private double calculatePoints() {
        double point=0;
        point = correctAnswerCount * 0.25;
        return point;
    }

    private void saveToSharedPreference(double points) {
        double totalScore=0;
        points = points *4;
        if(points<0){
            points=0;
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
            //reportScoreSave.scoreSave.add(getResources().getString(R.string.dis_inhibition_attention)+":" +String.valueOf(points));
            reportScoreSave.scoreSave.add(getResources().getString(R.string.dis_inhibition_attention_new)+":" +String.valueOf(points));


        }

    }

    private void callNextActivity(){
        startActivity(new Intent(this, DelayedRecallStoryOne.class));
    }


    @Override
    public void onBackPressed() {
    }
}
