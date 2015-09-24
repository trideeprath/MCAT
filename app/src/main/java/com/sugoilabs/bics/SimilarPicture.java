package com.sugoilabs.bics;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
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
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.Timer;
import java.util.TimerTask;


public class SimilarPicture extends ActionBarActivity implements View.OnClickListener{

    AnimationDrawable animation;
    AnimationDrawable animation_temp;
    ImageView imageView;
    int count=0;
    Drawable imageIdOptionOne;
    Drawable imageIdOptionTwo;
    Drawable imageIdOptionThree;
    Drawable imageIdOptionFour;
    int correctAnswerCount =0;

    ImageButton optionOneImageView , optionTwoImageView , optionThreeImageView , optionFourImageView;

    LinearLayout optionView;

    LinearLayout mainImageLinearLayout;
    final String Prefs_Score = "Prefs_Score";
    SharedPreferences scoreSharedPreference;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similar_picture);
        initializeLayout();
        showInstructionDialogBox();


        //YoYo.with(Techniques.FadeIn).duration(1000).playOn(findViewById(R.id.activity_similar_layout));

    }

    private void initializeLayout() {

        imageView = (ImageView) findViewById((R.id.activity_similar_image_view));
        //imageView.setVisibility(View.INVISIBLE);

        optionView = (LinearLayout) findViewById(R.id.activity_similar_options);
        optionView.setVisibility(View.INVISIBLE);

        optionOneImageView = (ImageButton) findViewById(R.id.activity_similar_image_one);
        optionOneImageView.setOnClickListener(this);
        optionTwoImageView = (ImageButton) findViewById(R.id.activity_similar_image_two);
        optionTwoImageView.setOnClickListener(this);
        optionThreeImageView = (ImageButton) findViewById(R.id.activity_similar_image_three);
        optionThreeImageView.setOnClickListener(this);
        optionFourImageView = (ImageButton) findViewById(R.id.activity_similar_image_four);
        optionFourImageView.setOnClickListener(this);

        mainImageLinearLayout = (LinearLayout) findViewById(R.id.activity_similar_layout_main_image);
        mainImageLinearLayout.setVisibility(View.INVISIBLE);

        scoreSharedPreference = getSharedPreferences(Prefs_Score,0);
        editor = scoreSharedPreference.edit();


    }

    public void showInstructionDialogBox() {

        MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(this).title(R.string.instruction).content(R.string.similarPictureInstruction).positiveText(R.string.next).cancelable(false);
        dialogBuilder.show();
        dialogBuilder.callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                Log.d("OK ", "great");

                startAnimation();
            }
        });


    }

    private void startAnimation() {

        //imageView.setVisibility(View.VISIBLE);
        mainImageLinearLayout.setVisibility(View.VISIBLE);
        //animation = new AnimationDrawable();
        Log.d("count" ,String.valueOf(count));
        if(count ==0 ) {
            //animation.addFrame(getResources().getDrawable(R.drawable.cow), 5000);
            animation_temp = new AnimationDrawable();
            animation_temp.addFrame(getResources().getDrawable(R.drawable.cuboid_main_image), 5000);
        }
        if(count ==1){
            animation_temp = new AnimationDrawable();
            animation_temp.addFrame(getResources().getDrawable(R.drawable.cube_main_image), 5000);
        }
        if(count ==2){
            animation_temp = new AnimationDrawable();
            animation_temp.addFrame(getResources().getDrawable(R.drawable.square_main_image), 5000);
        }

        if(count ==3){
            animation_temp = new AnimationDrawable();
            animation_temp.addFrame(getResources().getDrawable(R.drawable.prism_main_image), 5000);
        }
        if(count == 4){
            animation_temp = new AnimationDrawable();
            animation_temp.addFrame(getResources().getDrawable(R.drawable.v5pic1), 5000);
        }

        if(count == 5){
            animation_temp = new AnimationDrawable();
            animation_temp.addFrame(getResources().getDrawable(R.drawable.v6pic1), 5000);
        }
        if(count == 6){
            animation_temp = new AnimationDrawable();
            animation_temp.addFrame(getResources().getDrawable(R.drawable.v7pic1), 5000);
        }
        if(count == 7){
            animation_temp = new AnimationDrawable();
            animation_temp.addFrame(getResources().getDrawable(R.drawable.v8pic1), 5000);
        }
        if(count == 8){
            animation_temp = new AnimationDrawable();
            animation_temp.addFrame(getResources().getDrawable(R.drawable.v9pic1), 5000);
        }
        if(count == 9){
            animation_temp = new AnimationDrawable();
            animation_temp.addFrame(getResources().getDrawable(R.drawable.v10pic1), 5000);
        }





        animation = animation_temp;
        animation.setOneShot(true);
        imageView.setImageDrawable(animation);
        // start the animation!
        animation.start();
        count++;

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // this code will be executed after 2 seconds

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //imageView.setVisibility(View.INVISIBLE);
                        mainImageLinearLayout.setVisibility(View.INVISIBLE);
                        showOptions(count);

                    }
                });
            }
        }, 5000);


    }

    private void showOptions(int count_temp){

        optionView.setVisibility(View.VISIBLE);
        switch(count_temp){
            case 1:
                imageIdOptionOne = getResources().getDrawable(R.drawable.cuboid_option_one);
                imageIdOptionTwo = getResources().getDrawable(R.drawable.cuboid_option_two);
                imageIdOptionThree = getResources().getDrawable(R.drawable.cuboid_option_three);
                imageIdOptionFour = getResources().getDrawable(R.drawable.cuboid_option_four);
                break;
            case 2:
                imageIdOptionOne = getResources().getDrawable(R.drawable.cube_option_one);
                imageIdOptionTwo = getResources().getDrawable(R.drawable.cube_option_two);
                imageIdOptionThree = getResources().getDrawable(R.drawable.cube_option_three);
                imageIdOptionFour = getResources().getDrawable(R.drawable.cube_option_four);
                break;
            case 3:
                imageIdOptionOne = getResources().getDrawable(R.drawable.square_option_one);
                imageIdOptionTwo = getResources().getDrawable(R.drawable.square_option_two_new);
                imageIdOptionThree = getResources().getDrawable(R.drawable.square_option_three);
                imageIdOptionFour = getResources().getDrawable(R.drawable.square_option_four);
                break;
            case 4:
                imageIdOptionOne = getResources().getDrawable(R.drawable.prism_option_one);
                imageIdOptionTwo = getResources().getDrawable(R.drawable.prism_option_two);
                imageIdOptionThree = getResources().getDrawable(R.drawable.prism_option_three);
                imageIdOptionFour = getResources().getDrawable(R.drawable.prism_option_four);
                break;
            case 5:
                imageIdOptionOne = getResources().getDrawable(R.drawable.v5pic1);
                imageIdOptionTwo = getResources().getDrawable(R.drawable.v5pic2);
                imageIdOptionThree = getResources().getDrawable(R.drawable.v5pic3);
                imageIdOptionFour = getResources().getDrawable(R.drawable.v5pic4);
                break;
            case 6:
                imageIdOptionOne = getResources().getDrawable(R.drawable.v6pic2);
                imageIdOptionTwo = getResources().getDrawable(R.drawable.v6pic1);
                imageIdOptionThree = getResources().getDrawable(R.drawable.v6pic3);
                imageIdOptionFour = getResources().getDrawable(R.drawable.v6pic4);
                break;
            case 7:
                imageIdOptionOne = getResources().getDrawable(R.drawable.v7pic4);
                imageIdOptionTwo = getResources().getDrawable(R.drawable.v7pic2);
                imageIdOptionThree = getResources().getDrawable(R.drawable.v7pic3);
                imageIdOptionFour = getResources().getDrawable(R.drawable.v7pic1);
                break;
            case 8:
                imageIdOptionOne = getResources().getDrawable(R.drawable.v8pic3);
                imageIdOptionTwo = getResources().getDrawable(R.drawable.v8pic2);
                imageIdOptionThree = getResources().getDrawable(R.drawable.v8pic1);
                imageIdOptionFour = getResources().getDrawable(R.drawable.v8pic4);
                break;
            case 9:
                imageIdOptionOne = getResources().getDrawable(R.drawable.v9pic2);
                imageIdOptionTwo = getResources().getDrawable(R.drawable.v9pic1);
                imageIdOptionThree = getResources().getDrawable(R.drawable.v9pic3);
                imageIdOptionFour = getResources().getDrawable(R.drawable.v9pic4);
                break;
            case 10:
                imageIdOptionOne = getResources().getDrawable(R.drawable.v10pic1);
                imageIdOptionTwo = getResources().getDrawable(R.drawable.v10pic2);
                imageIdOptionThree = getResources().getDrawable(R.drawable.v10pic3);
                imageIdOptionFour = getResources().getDrawable(R.drawable.v10pic4);
                break;


        }

        optionOneImageView.setImageDrawable(imageIdOptionOne);
        optionTwoImageView.setImageDrawable(imageIdOptionTwo);
        optionThreeImageView.setImageDrawable(imageIdOptionThree);
        optionFourImageView.setImageDrawable(imageIdOptionFour);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_similar_picture, menu);
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


        switch (count){
            case 1:
                if(v.getId() == R.id.activity_similar_image_four){
                    correctAnswerCount++;
                }
                break;

            case 2:
                if(v.getId() == R.id.activity_similar_image_three){
                    correctAnswerCount++;
                }
                break;

            case 3:
                if(v.getId() == R.id.activity_similar_image_four){
                    correctAnswerCount++;
                }
                break;

            case 4:
                if(v.getId() == R.id.activity_similar_image_four){
                    correctAnswerCount++;
                }
                break;
            case 5:
                if(v.getId() == R.id.activity_similar_image_one){
                    correctAnswerCount++;
                }
            case 6:
                if(v.getId() == R.id.activity_similar_image_two){
                    correctAnswerCount++;
                }
                break;
            case 7:
                if(v.getId() == R.id.activity_similar_image_four){
                    correctAnswerCount++;
                }
                break;
            case 8:
                if(v.getId() == R.id.activity_similar_image_three){
                    correctAnswerCount++;
                }
                break;
            case 9:
                if(v.getId() == R.id.activity_similar_image_two){
                    correctAnswerCount++;
                }
                break;
            case 10:
                if(v.getId() == R.id.activity_similar_image_one){
                    correctAnswerCount++;
                }




                double points = calculatePoints();
                saveToSharedPreference(points);
                //Toast.makeText(this,"points " + String.valueOf(points),Toast.LENGTH_SHORT ).show();
                //reportScoreSave.scoreSave.add("Similar Picture:" +String.valueOf(correctAnswerCount));

                startActivity(new Intent(this, ReverseNumberRecall.class));

                //startActivity(new Intent(this, FinalScreen.class));

        }

        optionView.setVisibility(View.INVISIBLE);
        startAnimation();
    }

    private double calculatePoints() {
        double point=0;
        point = correctAnswerCount;
        return point;
    }

    private void saveToSharedPreference(double points) {
        double totalScore=0;
        if(scoreSharedPreference.contains("score")){
            totalScore = Double.valueOf(scoreSharedPreference.getString("score","0")) + Double.valueOf(points);
            editor.putString("score",String.valueOf(totalScore));
            editor.apply();
            Log.d("score",String.valueOf(totalScore));
            String pointsToast= String.valueOf(points);
            if(pointsToast.length() >3){
                pointsToast.substring(0,3);
            }
            String ScoreToast = String.valueOf(totalScore);
            if(ScoreToast.length() >3){
                ScoreToast = ScoreToast.substring(0,3);
            }
            Toast.makeText(this, "Points is: " + pointsToast + " Total Score is:" + ScoreToast, Toast.LENGTH_SHORT).show();
            //reportScoreSave.scoreSave.add(getResources().getString(R.string.visual_spatial_immedite_task )+":" +String.valueOf(points));
            reportScoreSave.scoreSave.add(getResources().getString(R.string.visual_spatial_immedite_task_new )+":" +String.valueOf(points));

        }

    }

    @Override
    public void onBackPressed() {
    }
}
