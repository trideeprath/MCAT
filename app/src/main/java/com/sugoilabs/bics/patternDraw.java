package com.sugoilabs.bics;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class patternDraw extends Activity implements View.OnClickListener {


    LineView myview;
    ArrowView arrowView;
    ArrayList<Button> buttonArrayList = new ArrayList<Button>();
    Button done;
    Button redo;
    int buttonClickCount =0;
    float[] previousButtonPosition = new float[2];
    float[] currentButtonPosition = new float[2];
    StringBuilder stringBuilder = new StringBuilder("");
    final String Prefs_Score = "Prefs_Score";
    SharedPreferences scoreSharedPreference;
    SharedPreferences.Editor editor;
    String answer = null;
    int timePassed;
    String startTime;
    String endTime;
    public int timeTaken;
    long millisInFuture = 180000;
    final myTimer waveTimer = new myTimer(millisInFuture +100 , 1000);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern_draw);
        setButtonArrayList();
        done = (Button) findViewById(R.id.pattern_done);
        done.setOnClickListener(this);

        redo =(Button) findViewById(R.id.pattern_redo);
        redo.setOnClickListener(this);

        scoreSharedPreference = getSharedPreferences(Prefs_Score,0);
        editor = scoreSharedPreference.edit();
        //startTime = new SimpleDateFormat("mmss").format(Calendar.getInstance().getTime());
        showInstructionDialogBox();

    }

    public void showInstructionDialogBox(){
        MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(this).title(R.string.instruction).content(R.string.patternDrawInstruction).positiveText(R.string.next).cancelable(false);
        dialogBuilder.show();
        dialogBuilder.callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                Log.d("OK ", "great");
                startTime = new SimpleDateFormat("mmss").format(Calendar.getInstance().getTime());
                showArrows();
                waveTimer.start();
                /*
                int[] location5= new int[2];
                int[] locationK= new int[2];
                int[] location6= new int[2];
                int[] locationL= new int[2];
                int location5CenterX, location5CenterY, locationKCenterX , locationKCenterY , location6CenterX , location6CenterY, locationLCenterX, locationLCenterY;
                Button button5 = (Button) findViewById(R.id.pattern_button5);
                Button buttonK = (Button) findViewById(R.id.pattern_buttonK);
                Button button6 = (Button) findViewById(R.id.pattern_button6);
                Button buttonL = (Button) findViewById(R.id.pattern_buttonL);

                button5.getLocationOnScreen(location5);
                location5CenterX =location5[0]+ button5.getWidth()/2;
                location5CenterY =location5[1];

                buttonK.getLocationOnScreen(locationK);
                locationKCenterX =locationK[0]+ buttonK.getWidth()/2;
                locationKCenterY =locationK[1];

                button6.getLocationOnScreen(location6);
                location6CenterX =location6[0]+ button6.getWidth()/2;
                location6CenterY =location6[1];

                buttonL.getLocationOnScreen(locationL);
                locationLCenterX =locationL[0]+ buttonL.getWidth()/2;
                locationLCenterY =locationL[1];

                arrowView = new ArrowView(getApplicationContext(), location5CenterX,location5CenterY,locationKCenterX,locationKCenterY);
                addContentView(arrowView, new ViewGroup.LayoutParams(1000,1000));

                arrowView = new ArrowView(getApplicationContext(), locationKCenterX,locationKCenterY,location6CenterX,location6CenterY);
                addContentView(arrowView, new ViewGroup.LayoutParams(1000,1000));

                arrowView = new ArrowView(getApplicationContext(), location6CenterX,location6CenterY,locationLCenterX,locationLCenterY);
                addContentView(arrowView, new ViewGroup.LayoutParams(1000,1000));
                */

            }
        });
    }

    public  void showArrows(){
        int[] location5= new int[2];
        int[] locationK= new int[2];
        int[] location6= new int[2];
        int[] locationL= new int[2];
        int location5CenterX, location5CenterY, locationKCenterX , locationKCenterY , location6CenterX , location6CenterY, locationLCenterX, locationLCenterY;
        Button button5 = (Button) findViewById(R.id.pattern_button5);
        Button buttonK = (Button) findViewById(R.id.pattern_buttonK);
        Button button6 = (Button) findViewById(R.id.pattern_button6);
        Button buttonL = (Button) findViewById(R.id.pattern_buttonL);

        button5.getLocationOnScreen(location5);
        location5CenterX =location5[0]+ button5.getWidth()/2;
        location5CenterY =location5[1];

        buttonK.getLocationOnScreen(locationK);
        locationKCenterX =locationK[0]+ buttonK.getWidth()/2;
        locationKCenterY =locationK[1];

        button6.getLocationOnScreen(location6);
        location6CenterX =location6[0]+ button6.getWidth()/2;
        location6CenterY =location6[1];

        buttonL.getLocationOnScreen(locationL);
        locationLCenterX =locationL[0]+ buttonL.getWidth()/2;
        locationLCenterY =locationL[1];

        arrowView = new ArrowView(getApplicationContext(), location5CenterX,location5CenterY,locationKCenterX,locationKCenterY);
        addContentView(arrowView, new ViewGroup.LayoutParams(1000,1000));

        arrowView = new ArrowView(getApplicationContext(), locationKCenterX,locationKCenterY,location6CenterX,location6CenterY);
        addContentView(arrowView, new ViewGroup.LayoutParams(1000,1000));

        arrowView = new ArrowView(getApplicationContext(), location6CenterX,location6CenterY,locationLCenterX,locationLCenterY);
        addContentView(arrowView, new ViewGroup.LayoutParams(1000,1000));


    }

    public int calculateTimePassedInSeconds(String startmmss , String endmmss){
        Log.d("Start time" , startmmss);
        Log.d("End time " , endmmss);
        int time=0;
        int startMin,startSec, endMin, endSec;
        startMin = Integer.valueOf(startmmss.substring(0,2));
        startSec = Integer.valueOf(startmmss.substring(startmmss.length() -2));
        endMin = Integer.valueOf(endmmss.substring(0,2));
        endSec = Integer.valueOf(endmmss.substring(endmmss.length() -2));
        time = ((endMin - startMin) *60 )+ (endSec - startSec);
        return time;
    }

    private void setButtonArrayList() {
        buttonArrayList.add((Button) findViewById(R.id.pattern_buttonN));
        buttonArrayList.add((Button) findViewById(R.id.pattern_buttonO));
        buttonArrayList.add((Button)findViewById(R.id.pattern_button9));
        buttonArrayList.add((Button)findViewById(R.id.pattern_button8));
        buttonArrayList.add((Button)findViewById(R.id.pattern_buttonM));
        buttonArrayList.add((Button)findViewById(R.id.pattern_button10));
        buttonArrayList.add((Button)findViewById(R.id.pattern_buttonP));
        buttonArrayList.add((Button)findViewById(R.id.pattern_buttonK));
        buttonArrayList.add((Button)findViewById(R.id.pattern_button5));
        buttonArrayList.add((Button)findViewById(R.id.pattern_button6));
        buttonArrayList.add((Button)findViewById(R.id.pattern_buttonL));
        buttonArrayList.add((Button)findViewById(R.id.pattern_button7));

        for(Button b : buttonArrayList){
            b.setOnClickListener(this);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pattern_draw, menu);
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
        if(v.getId() == R.id.pattern_done){
            endTime = new SimpleDateFormat("mmss").format(Calendar.getInstance().getTime());
            timePassed = calculateTimePassedInSeconds(startTime,endTime);
            Log.d("timePassed",String.valueOf(timePassed));
            int points = calculatePoints();
            Log.d("score", String.valueOf(points));
            saveToSharedPreference(points);
            //startActivity(new Intent(this, Clock.class));
            //startActivity(new Intent(this, DrawingActivity.class));
            startActivity(new Intent(this, NumberRecall.class));
        }
        else if(v.getId() == R.id.pattern_redo){
            //setContentView(R.layout.activity_pattern_draw);
            //showArrows();
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
        else if(v.getId() == R.id.pattern_button5 || v.getId() == R.id.pattern_buttonK || v.getId() == R.id.pattern_button6){
            Toast.makeText(this, "Please start tapping from 'L'  " , Toast.LENGTH_LONG).show();
        }
        else{
            int[] location= new int[2];
            String text ="";
            float centerX=0;
            float centerY=0;
            int id = v.getId();
            Button b = (Button)findViewById(id);
            text = b.getText().toString();
            stringBuilder.append(text);
            b.getLocationOnScreen(location);
            centerX =location[0]+ b.getWidth()/2;
            centerY =location[1];
            Log.d("text , x , y", text + " " + centerX + " " + centerY);
            if(buttonClickCount==0){
                previousButtonPosition[0] =  centerX;
                previousButtonPosition[1] =  centerY;
            }
            else if(buttonClickCount ==1){
                currentButtonPosition[0] = centerX;
                currentButtonPosition[1] = centerY;
            }
            else{
                previousButtonPosition[0] = currentButtonPosition[0];
                previousButtonPosition[1] = currentButtonPosition[1];
                currentButtonPosition[0] = centerX;
                currentButtonPosition[1] = centerY;
            }

            if(buttonClickCount>0){
                myview = new LineView(this,previousButtonPosition[0],previousButtonPosition[1],currentButtonPosition[0],currentButtonPosition[1]);
                addContentView(myview, new ViewGroup.LayoutParams(1000,1000));

            }
            buttonClickCount++;
        }

    }

    private int calculatePoints() {
        int points=0;
        Log.d("string answer", stringBuilder.toString());

        //The following is time based scoring
        /*
        if(stringBuilder.toString().equals("5K6L7M8N9O10P") ||stringBuilder.toString().equals("L7M8N9O10P") ){
            if(timePassed<=60){
                points=2;
            }
            else if(timePassed >60 && timePassed<180){
                points=1;
            }
            else {
                points=0;
            }
        }
        else{
            points=0;
        }
        */
        //The following code is non time based scoring
        /*
        if(stringBuilder.toString().equals("5K6L7M8N9O10P") ||stringBuilder.toString().equals("L7M8N9O10P") || stringBuilder.toString().equals("6L7M8N9O10P")  ) {
            points = 10;
        }
        */

        String firstAnswer = "5K6L7M8N9O10P"; //12
        String secondAnswer = "L7M8N9O10P";   //9
        String thirdAnswer = "6L7M8N9O10P";   //10

        String answer = stringBuilder.toString();

        if(answer.charAt(0)=='5'){
            for(int x =0; x < answer.length() && x < firstAnswer.length() ; x++){
                if(answer.charAt(x) == firstAnswer.charAt(x)){
                    points++;
                }
            }
        }
        else if(answer.charAt(0) == 'L'){
            points++;
            for(int x =0; x < answer.length() && x < secondAnswer.length() ; x++){
                if(answer.charAt(x) == secondAnswer.charAt(x)){
                    points++;
                }
            }
        }
        else if(answer.charAt(0) == '6') {
            for(int x =0; x < answer.length() && x < thirdAnswer.length() ; x++){
                if(answer.charAt(x) == thirdAnswer.charAt(x)){
                    points++;
                }
            }
        }
        else {
            points =0;
        }

        /*
        else if(stringBuilder.toString().equals("5K") ||stringBuilder.toString().equals("L7") || stringBuilder.toString().equals("6L")){
            points =1;
        }
        else if(stringBuilder.toString().equals("5K6") ||stringBuilder.toString().equals("L7M") || stringBuilder.toString().equals("6L7")  ) {
            points = 2;
        }
        else if(stringBuilder.toString().equals("5K6L") ||stringBuilder.toString().equals("L7M8") || stringBuilder.toString().equals("6L7M")  ) {
            points = 3;
        }
        else if(stringBuilder.toString().equals("5K6L7") ||stringBuilder.toString().equals("L7M8N") || stringBuilder.toString().equals("6L7M8")  ) {
            points = 4;
        }
        else if(stringBuilder.toString().equals("5K6L7M") ||stringBuilder.toString().equals("L7M8N9O10P") || stringBuilder.toString().equals("6L7M8N9O10P")  ) {
            points = 10;
        }
        */

        if(points >10){
            points =10;
        }

        return points;
    }



    private void saveToSharedPreference(int points) {
        /*
        int totalScore=0;
        if(scoreSharedPreference.contains("score")){
            totalScore = Integer.valueOf(scoreSharedPreference.getString("score","0")) + Integer.valueOf(points);
            editor.putString("score",String.valueOf(totalScore));
            editor.commit();
            Log.d("score",String.valueOf(totalScore));
            Toast.makeText(this,"Points is: " + String.valueOf(points)+" Time Passed: "+ timePassed+ " Total Score is:" + String.valueOf(totalScore),Toast.LENGTH_SHORT).show();
            reportScoreSave.scoreSave.add(getResources().getString(R.string.visual_cognitive_attention_task) +":" +String.valueOf(points)+","+String.valueOf(timePassed)+"sec");
        }
        */
        double totalScore=0;
        points = points ;
        if(scoreSharedPreference.contains("score")){
            totalScore = Double.valueOf(scoreSharedPreference.getString("score","0")) + Integer.valueOf(points);
            editor.putString("score",String.valueOf(totalScore));
            editor.commit();
            String ScoreToast = String.valueOf(totalScore);
            if(ScoreToast.length() >3){
                ScoreToast = ScoreToast.substring(0,3);
            }
            Log.d("score", String.valueOf(totalScore));
            Toast.makeText(this,"Points is: " + String.valueOf(points)+ " Total Score is:" + ScoreToast,Toast.LENGTH_SHORT).show();
            //reportScoreSave.scoreSave.add(getResources().getString(R.string.visual_cognitive_attention_task)+":" +String.valueOf(points));
            reportScoreSave.scoreSave.add(getResources().getString(R.string.visual_cognitive_attention_task_new)+":" +String.valueOf(points));
            reportTimeSave.timeSave.add(timeTaken);
            waveTimer.cancel();
            Log.d("time", String.valueOf(timeTaken));
        }

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

    class LineView extends View
    {
        float xPositionP1;
        float yPositionP1;
        float xPositionP2;
        float yPositionP2;

        public LineView(Context context,float x1 , float y1 , float x2 , float y2) {
            super(context);
            xPositionP1 = x1;
            yPositionP1 = y1;
            xPositionP2 = x2;
            yPositionP2 = y2;

            // TODO Auto-generated constructor stub
        }


        @Override
        protected void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);
            int x=20;
            int y=300;
            int dpSize = 3;
            int radius=40;
            Paint paint=new Paint();
            // Use Color.parseColor to define HTML colors
            paint.setColor(Color.parseColor("#000000"));
            DisplayMetrics dm = getResources().getDisplayMetrics() ;
            float strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpSize, dm);
            paint.setStrokeWidth(strokeWidth);
            //canvas.drawCircle(x,x, radius, paint);
            canvas.drawLine(xPositionP1,yPositionP1,xPositionP2,yPositionP2,paint);
        }
    }

    class ArrowView extends View
    {
        float xPositionP1;
        float yPositionP1;
        float xPositionP2;
        float yPositionP2;


        public ArrowView(Context context,float x1 , float y1 , float x2 , float y2) {
            super(context);
            xPositionP1 = x1;
            yPositionP1 = y1;
            xPositionP2 = x2;
            yPositionP2 = y2;

            // TODO Auto-generated constructor stub
        }


        @Override
        protected void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);
            int x=20;
            int y=300;
            int dpSize = 3;
            int radius=40;
            Paint paint=new Paint();
            // Use Color.parseColor to define HTML colors
            paint.setColor(Color.parseColor("#000000"));
            DisplayMetrics dm = getResources().getDisplayMetrics() ;
            float strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpSize, dm);
            paint.setStrokeWidth(strokeWidth);
            //canvas.drawCircle(x,x, radius, paint);
            //canvas.drawLine(xPositionP1,yPositionP1,xPositionP2,yPositionP2,paint);
            float h=(float) 30.0;
            float x1 , y1, x2 , y2 , x3 , y3 , x4 , y4;
            x1 = xPositionP1;
            y1 = yPositionP1;
            x2 = (xPositionP2 + xPositionP1)/2;
            y2 = (yPositionP2 + yPositionP1)/2;

            float phi = (float) Math.atan2(y2 - y1, x2 - x1);
            float angle1 = (float) (phi - Math.PI / 6);
            float angle2 = (float) (phi + Math.PI / 6);

            x3 = (float) (x2 - h * Math.cos(angle1));
            x4 = (float) (x2 - h * Math.cos(angle2));
            y3 = (float) (y2 -  h * Math.sin(angle1));
            y4 = (float) (y2 -  h * Math.sin(angle2));
            canvas.drawLine(x1, y1,x2,y2 ,paint);
            canvas.drawLine(x2, y2,x3,y3 ,paint);
            canvas.drawLine(x2, y2,x4,y4 ,paint);


        }
    }

    @Override
    public void onBackPressed() {
    }



}
