package com.sugoilabs.bics;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.Arrays;

public class GroupMatching extends ActionBarActivity implements View.OnTouchListener, View.OnDragListener, View.OnClickListener {

    public int buttonMovedCount=0;
    public int gameCount=0;
    public ArrayList<String> groupOneAnswer = new ArrayList<String>();
    public ArrayList<String> groupTwoAnswer = new ArrayList<String>();
    public ArrayList<ArrayList<String>> allButtonStringArrayList = new ArrayList<ArrayList<String>>();
    int points =0;
    SharedPreferences scoreSharedPreference;
    SharedPreferences.Editor editor;
    final String Prefs_Score = "Prefs_Score";
    TextView groupOneTextView;
    TextView groupTwoTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_matching);
        showInstructionDialogBox();
        setButtonTextArrayList();
        for ( ArrayList<String> abc : allButtonStringArrayList){
            Log.d("abc", abc.toString());
        }
        Log.d("size" , String.valueOf(allButtonStringArrayList.size()));
        initializeLayouts();
    }

    private void initializeLayouts() {
        Button button1 = (Button) findViewById(R.id.group_matching_button1);
        button1.setText(allButtonStringArrayList.get(gameCount).get(0));
        Button button2 = (Button) findViewById(R.id.group_matching_button2);
        button2.setText(allButtonStringArrayList.get(gameCount).get(2));
        Button button3 = (Button) findViewById(R.id.group_matching_button3);
        button3.setText(allButtonStringArrayList.get(gameCount).get(4));
        Button button4 = (Button) findViewById(R.id.group_matching_button4);
        button4.setText(allButtonStringArrayList.get(gameCount).get(1));
        Button button5 = (Button) findViewById(R.id.group_matching_button5);
        button5.setText(allButtonStringArrayList.get(gameCount).get(3));
        Button button6 = (Button) findViewById(R.id.group_matching_button6);
        button6.setText(allButtonStringArrayList.get(gameCount).get(5));
        LinearLayout containerLeft = (LinearLayout) findViewById(R.id.group_matching_container_one);
        LinearLayout containerRight = (LinearLayout) findViewById(R.id.group_matching_container_two);
        Button nextButton = (Button) findViewById(R.id.group_matching_next);
        groupOneTextView = (TextView) findViewById(R.id.group_matching_group1_textview);
        groupTwoTextView = (TextView) findViewById(R.id.group_matching_group2_textview);
        button1.setOnTouchListener(this);
        button2.setOnTouchListener(this);
        button3.setOnTouchListener(this);
        button4.setOnTouchListener(this);
        button5.setOnTouchListener(this);
        button6.setOnTouchListener(this);
        containerLeft.setOnDragListener(this);
        containerRight.setOnDragListener(this);
        nextButton.setOnClickListener(this);
        //scoreSharedPreference = getSharedPreferences(Prefs_Score,0);
        //editor = scoreSharedPreference.edit();
        scoreSharedPreference = getSharedPreferences(Prefs_Score,0);
        editor = scoreSharedPreference.edit();



    }

    private void setButtonTextArrayList() {

        ArrayList<String> temp = new ArrayList<String>();
        temp.add("PARIS");temp.add("ROME");temp.add("NEW YORK"); temp.add("INCH");temp.add("POUND");temp.add("LITRE");
        allButtonStringArrayList.add(temp);
        //temp.clear();

        temp = new ArrayList<String>();
        temp.add("STEAK");temp.add("SAUSAGE");temp.add("GROUND PORK"); temp.add("CAKE");temp.add("PIE");temp.add("COOKIES");
        allButtonStringArrayList.add(temp);

        //temp.clear();
        temp = new ArrayList<String>();
        temp.add("PEN");temp.add("CHALK");temp.add("INK"); temp.add("BAT");temp.add("DISC");temp.add("BALL");
        allButtonStringArrayList.add(temp);

        //temp.clear();
        temp = new ArrayList<String>();
        temp.add("DOG");temp.add("CAT");temp.add("HAMSTER"); temp.add("HOUSE");temp.add("HOTEL");temp.add("RESORT");
        allButtonStringArrayList.add(temp);

        //temp.clear();
        temp = new ArrayList<String>();
        temp.add("PHONE");temp.add("EMAIL");temp.add("TEXT MESSAGE"); temp.add("CAR");temp.add("TRAIN");temp.add("PLANE");
        allButtonStringArrayList.add(temp);



    }


    private void showInstructionDialogBox() {
        MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(this).title(R.string.instruction).content(R.string.groupMatchingInstruction).positiveText(R.string.next).cancelable(false);
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
        getMenuInflater().inflate(R.menu.menu_group_matching, menu);
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
    public boolean onTouch(View v, MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(null, shadowBuilder, v, 0);
            //v.setVisibility(View.INVISIBLE);
            return true;
        } else {
            return false;
        }

    }

    @Override
    public boolean onDrag(View v, DragEvent e) {
        if (e.getAction() == DragEvent.ACTION_DROP) {
            groupOneTextView.setVisibility(View.GONE);
            groupTwoTextView.setVisibility(View.GONE);
            View view = (View) e.getLocalState();
            Button button = (Button) view;
            view.setVisibility(View.INVISIBLE);
            Log.d("view id text", button.getText().toString());
            ViewGroup from = (ViewGroup) view.getParent();
            from.removeView(view);
            LinearLayout to = (LinearLayout) v;
            Log.d("group moved ", to.getTag().toString());
            String movedToGroup = to.getTag().toString();
            to.addView(view);
            view.setVisibility(View.VISIBLE);
            if(movedToGroup.equalsIgnoreCase("container_one")){
                groupOneAnswer.add(button.getText().toString());
            }
            else{
                groupTwoAnswer.add(button.getText().toString());
            }
            buttonMovedCount++;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.group_matching_next){
            Log.d("Button moved count", String.valueOf(buttonMovedCount));
            if(buttonMovedCount >=6){
                //compare answer

                boolean isCorrect = compareAnswer();
                if(isCorrect){
                    Log.d("answer" , "correct");
                    points++;
                }else {
                    Log.d("answer", "not correct");
                }

                //move to next groupmatching activity
                gameCount++;
                if(gameCount >=5){
                    Log.d("groupmatching","over");
                    Log.d("score", String.valueOf(points));
                    saveToSharedPreference(points);
                    //startActivity(new Intent(this, PictureFlashAnswer.class));
                    startActivity(new Intent(this, PictureFlashAnswerDelayedTab.class));
                }
                else {
                    setContentView(R.layout.activity_group_matching);
                    initializeLayouts();
                    clearAllVariables();
                }


            }
            else {
                Toast.makeText(this,"Please move all the items to each group" , Toast.LENGTH_SHORT ).show();
            }
        }
    }


    @Override
    public void onBackPressed() {
    }

    private void clearAllVariables() {
        groupTwoAnswer.clear();
        groupTwoAnswer.clear();
        buttonMovedCount=0;
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
            //reportScoreSave.scoreSave.add(getResources().getString(R.string.group_matching) + ":" + String.valueOf(points));

            reportScoreSave.scoreSave.add(getResources().getString(R.string.group_matching_new)+":" +String.valueOf(points));

        }

    }


    private boolean compareAnswer() {

        int correctAnswerGroupOne=0;
        int correctAnswerGroupTwo=0;
        int correctAnswerGroupOne_other=0;
        int correctAnswerGroupTwo_other=0;
        boolean isCorrect=false;



        for(String answerInGroupOne : groupOneAnswer){
            if(answerInGroupOne.equalsIgnoreCase(allButtonStringArrayList.get(gameCount).get(0))
                    || answerInGroupOne.equalsIgnoreCase(allButtonStringArrayList.get(gameCount).get(1)) || answerInGroupOne.equalsIgnoreCase(allButtonStringArrayList.get(gameCount).get(2))){
                correctAnswerGroupOne++;
            }
        }

        for(String answerInGroupOne : groupOneAnswer){
            if(answerInGroupOne.equalsIgnoreCase(allButtonStringArrayList.get(gameCount).get(3)) || answerInGroupOne.equalsIgnoreCase(allButtonStringArrayList.get(gameCount).get(4))
                    || answerInGroupOne.equalsIgnoreCase(allButtonStringArrayList.get(gameCount).get(5))){
                correctAnswerGroupOne_other++;
            }
        }

        for(String answerInGroupTwo : groupTwoAnswer){
            if(answerInGroupTwo.equalsIgnoreCase(allButtonStringArrayList.get(gameCount).get(0)) ||
                    answerInGroupTwo.equalsIgnoreCase(allButtonStringArrayList.get(gameCount).get(1))
                    || answerInGroupTwo.equalsIgnoreCase(allButtonStringArrayList.get(gameCount).get(2))){
                correctAnswerGroupTwo++;
            }
        }

        for(String answerInGroupTwo : groupTwoAnswer){
            if(answerInGroupTwo.equalsIgnoreCase(allButtonStringArrayList.get(gameCount).get(3))
                    || answerInGroupTwo.equalsIgnoreCase(allButtonStringArrayList.get(gameCount).get(4))
                    || answerInGroupTwo.equalsIgnoreCase(allButtonStringArrayList.get(gameCount).get(5))){
                correctAnswerGroupTwo_other++;
            }
        }


        if((correctAnswerGroupOne >=3 || correctAnswerGroupOne_other>=3) && (correctAnswerGroupTwo >=3 ||correctAnswerGroupTwo_other >=3)){
            isCorrect = true;
        }
        else {
            isCorrect = false;
        }
 
        return isCorrect;
    }
}
