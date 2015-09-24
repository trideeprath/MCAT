package com.sugoilabs.bics;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Arrays;


public class DrawingActivity extends ActionBarActivity {

    float[][] rectangleArray;
    float[][] straightLineArray;
    int points = 0;
    final String Prefs_Score = "Prefs_Score";
    SharedPreferences scoreSharedPreference;
    SharedPreferences.Editor editor;

    public int timeTaken;
    long millisInFuture = 180000;
    final myTimer waveTimer = new myTimer(millisInFuture +100 , 1000);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing_activity);
        scoreSharedPreference = getSharedPreferences(Prefs_Score, 0);
        editor = scoreSharedPreference.edit();
        waveTimer.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_draw_rectangle, menu);
        return true;
    }

    public class myTimer extends CountDownTimer {

        private long millisActual;

        public myTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            millisActual = millisInFuture;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (millisUntilFinished <= millisActual) {
                //timerText.setText("Time Left: " + millisUntilFinished / 1000);
                timeTaken = (int) (180 - millisUntilFinished / 1000);
            }
        }

        @Override
        public void onFinish()
        {

        }

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

    public void rectangleClicked(View view) {
        //use chosen color
        SelectedShape.selectedShape = 1;
        Log.d("shape", String.valueOf(SelectedShape.selectedShape));
    }

    public void lineClicked(View view) {
        //use chosen color
        SelectedShape.selectedShape = 2;
        Log.d("shape", String.valueOf(SelectedShape.selectedShape));

    }

    public void undoClicked(View view) {
        //use chosen color
        setContentView(R.layout.activity_drawing_activity);
    }

    public void shapeNextClicked(View view) {


        rectangleArray = SelectedShape.rectangleArray;
        straightLineArray = SelectedShape.straightLineArray;

        Log.d("rectangles", Arrays.deepToString(rectangleArray));
        Log.d("straightLines", Arrays.deepToString(straightLineArray));

        if (rectangleArray[0][0] != 0 && straightLineArray[0][0] != 0) {
            points = calculatePoints();

            saveToSharedPreference(points);


            Intent nextActivity = new Intent(this, SimilarPicture.class);
            startActivity(nextActivity);
        } else {
            Toast.makeText(this, "Draw the picture you have been shown in the last screen using tool tab", Toast.LENGTH_LONG).show();
        }


    }




    public int calculatePoints() {
        int points = 0;
        int rectangleCount = 0, straightLineCount = 0;
        for (int i = 0; i < 3; i++) {
            if (rectangleArray[i][0] != 0) {
                rectangleCount++;
            }
        }

        for (int i = 0; i < 5; i++) {
            if (straightLineArray[i][0] != 0) {
                straightLineCount++;
            }
        }

        Log.d("rectangle count, Straight count ", String.valueOf(rectangleCount) + ", " + String.valueOf(straightLineCount));

        if (rectangleCount == 3 && straightLineCount == 5 || rectangleCount == 3 && straightLineCount == 6) {
            Log.d("Calculate points", "yes");
            points = 5;

        }
        else if(rectangleCount==3 &&  straightLineCount == 1 || rectangleCount == 3 && straightLineCount == 2 || rectangleCount == 3 && straightLineCount == 3){
            points =3;
        }
        else {
            points = 0;
            Log.d("Calculate points", "no");
        }


        //The below code was the scoring logic when using the rectangle inside rectangle image
        /*

        if(rectangleCount == 2 && straightLineCount ==4){
            Log.d("Calculate points","yes");
            points = rectangleScore();
            if(points ==1){
                points = points + straightLineScore();
            }


        }

        else{
            points =0;
            Log.d("Calculate points", "no");
        }

        */
        return points;
    }

    public int rectangleScore() {
        int point = 0;
        if (rectangleArray[0][0] < rectangleArray[1][0] && rectangleArray[0][1] < rectangleArray[1][1] && rectangleArray[0][2] > rectangleArray[1][2] && rectangleArray[0][3] > rectangleArray[1][3]) {
            Log.d("First ", "Bigger");
            point = 1;

        } else if (rectangleArray[0][0] > rectangleArray[1][0] && rectangleArray[0][1] > rectangleArray[1][1] && rectangleArray[0][2] < rectangleArray[1][2] && rectangleArray[0][3] < rectangleArray[1][3]) {
            Log.d("Second", "Smaller");
            point = 1;
        } else if (rectangleArray[0][0] < rectangleArray[1][0] && rectangleArray[0][1] > rectangleArray[1][1] && rectangleArray[0][2] > rectangleArray[1][2] && rectangleArray[0][3] < rectangleArray[1][3]) {
            Log.d("Second", "Bigger");
            point = 1;
        } else if (rectangleArray[0][0] > rectangleArray[1][0] && rectangleArray[0][1] < rectangleArray[1][1] && rectangleArray[0][2] < rectangleArray[1][2] && rectangleArray[0][3] > rectangleArray[1][3]) {
            Log.d("Second", "smaller");
            point = 1;
        }
        return point;

    }


    public int straightLineScore() {
        int point = 0;
        int x1, x2;
        int leastI = 0;
        int leastJ = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j = j + 2) {
                if (straightLineArray[i][j] < straightLineArray[leastI][leastJ]) {
                    leastI = i;
                    leastJ = j;
                }
            }
        }

        int maxI = 0;
        int maxJ = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j = j + 2) {
                if (straightLineArray[i][j] > straightLineArray[maxI][maxJ]) {
                    maxI = i;
                    maxJ = j;
                }
            }
        }


        Log.d("Least I , Least J", String.valueOf(leastI) + " " + String.valueOf(leastJ));

        Log.d("Max I , Max J", String.valueOf(maxI) + " " + String.valueOf(maxJ));

        float leftDiagonalLength = (float) ((float) Math.pow((straightLineArray[leastI][0] - straightLineArray[leastI][2]), 2) + Math.pow((straightLineArray[leastI][1] - straightLineArray[leastI][3]), 2));

        float rightDiagonalLength = (float) ((float) Math.pow((straightLineArray[maxI][0] - straightLineArray[maxI][2]), 2) + Math.pow((straightLineArray[maxI][1] - straightLineArray[maxI][3]), 2));

        if (leftDiagonalLength > rightDiagonalLength) {
            Log.d("leftDiagonalGreater", "true");
            point = 2;
        }


        return point;

    }

    private void saveToSharedPreference(int points) {
        /*
        int totalScore = 0;
        if (scoreSharedPreference.contains("score")) {
            totalScore = Integer.valueOf(scoreSharedPreference.getString("score", "0")) + Integer.valueOf(points);
            editor.putString("score", String.valueOf(totalScore));
            editor.commit();
            Log.d("score", String.valueOf(totalScore));
            String ScoreToast = String.valueOf(totalScore);
            if (ScoreToast.length() > 3) {
                ScoreToast = ScoreToast.substring(0, 3);
            }
            Toast.makeText(this, "Points is: " + String.valueOf(points) + " Total Score is:" + ScoreToast, Toast.LENGTH_SHORT).show();
            //Toast.makeText(this,"Points is: " + String.valueOf(points)+ " Total Score is:" + String.valueOf(totalScore),Toast.LENGTH_SHORT).show();
            reportScoreSave.scoreSave.add(getResources().getString(R.string.visual_recall_immediate_task) + ":" + String.valueOf(points));
        }
        */
        double totalScore=0;
        points = points *2;
        if(scoreSharedPreference.contains("score")){
            totalScore = Double.valueOf(scoreSharedPreference.getString("score","0")) + Integer.valueOf(points);
            editor.putString("score", String.valueOf(totalScore));
            editor.commit();
            String ScoreToast = String.valueOf(totalScore);
            if(ScoreToast.length() >3){
                ScoreToast = ScoreToast.substring(0,3);
            }
            Log.d("score", String.valueOf(totalScore));
            Toast.makeText(this,"Points is: " + String.valueOf(points)+ " Total Score is:" + ScoreToast,Toast.LENGTH_SHORT).show();
            //reportScoreSave.scoreSave.add(getResources().getString(R.string.visual_recall_immediate_task)+":" +String.valueOf(points));
            reportScoreSave.scoreSave.add(getResources().getString(R.string.visual_recall_immediate_task_new)+":" +String.valueOf(points));
            reportTimeSave.timeSave.add(timeTaken);
            waveTimer.cancel();
            Log.d("time", String.valueOf(timeTaken));
        }
    }

    @Override
    public void onBackPressed() {
    }


}
