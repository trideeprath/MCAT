package com.sugoilabs.bics;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.LogRecord;


public class PictureFlash extends ActionBarActivity implements View.OnClickListener {

    Toolbar toolbar;
    Button start;
    Button done;
    final String Prefs_Score = "Prefs_Score";
    SharedPreferences scoreSharedPreference;
    SharedPreferences.Editor scoreEditor;
    LinearLayout imageLayout;
    ImageView imageView;
    int attempts;
    Intent nextActivity;
    AnimationDrawable animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_flash);
        //toolbar = (Toolbar) findViewById(R.id.app_bar);
        //setSupportActionBar(toolbar);
        showInstructionDialogBox();
        initializeLayout();

    }

    private void initializeLayout() {

        start = (Button) findViewById(R.id.picture_flash_start);
        start.setOnClickListener(this);

        done = (Button) findViewById(R.id.picture_flash_done);
        done.setOnClickListener(this);

        imageLayout = (LinearLayout) findViewById(R.id.picture_flash_layout);

        imageView = (ImageView) findViewById((R.id.picture_flash_image_view));

    }

    public void showInstructionDialogBox() {

        MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(this).title(R.string.instruction).content(R.string.pictureFlashInstruction).positiveText(R.string.next).cancelable(false);
        new MaterialDialog.Builder(this).title(R.string.instruction).content(R.string.pictureFlashInstruction).positiveText(R.string.next).cancelable(false);

        dialogBuilder.show();
        dialogBuilder.callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                Log.d("OK ", "great");
                if (attempts < 2) {
                    startAnimation();
                    attempts++;
                } else {
                    makeToast();
                }
            }
        });


    }

    public void makeToast() {
        Toast.makeText(this, R.string.attemps_over, Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_picture_flash, menu);
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
        if (v.getId() == R.id.picture_flash_start) {
            if (attempts < 2) {
                startAnimation();
                attempts++;
            } else {
                Toast.makeText(this, R.string.attemps_over, Toast.LENGTH_SHORT).show();
            }
        }
        if (v.getId() == R.id.picture_flash_done) {
            //nextActivity = new Intent(this, Pattern.class);
            //nextActivity = new Intent(this, PatternShowLayout.class);
            //nextActivity = new Intent(this, patternDraw.class);


            //Just for debugging
            nextActivity = new Intent(this, PictureFlashAnswerImmediateTab.class);
            startActivity(nextActivity);


            //nextActivity = new Intent(this, PrimaryColor.class);
            //startActivity(nextActivity);


            //

            //nextActivity = new Intent(this, GroupMatching.class);
            //startActivity(nextActivity);

            //nextActivity = new Intent(this, SelectShapes.class);
            //startActivity(nextActivity);

            //nextActivity = new Intent(this, rectangleShow.class);
            //startActivity(nextActivity);



            //nextActivity = new Intent(this, NumberRecall.class);
            //startActivity(nextActivity);


            //nextActivity = new Intent(this, DelayedRecallStory.class);
            //startActivity(nextActivity);


            //nextActivity = new Intent(this, SelectLetter.class);
            //startActivity(nextActivity);


            //
            //nextActivity = new Intent(this, Orientation.class);
           // startActivity(nextActivity);

            //nextActivity = new Intent(this, GroupMatching.class);
            //startActivity(nextActivity);


        }
    }


    private void startAnimation() {
        imageView.setVisibility(View.VISIBLE);
        animation = new AnimationDrawable();
        animation.addFrame(getResources().getDrawable(R.drawable.cow), 2000);
        animation.addFrame(getResources().getDrawable(R.drawable.bird), 3000);
        animation.addFrame(getResources().getDrawable(R.drawable.tree), 3000);
        animation.addFrame(getResources().getDrawable(R.drawable.boat), 3000);
        animation.addFrame(getResources().getDrawable(R.drawable.key),3000);
        animation.addFrame(getResources().getDrawable(R.drawable.screen), 3000);

        animation.setOneShot(true);
        imageView.setImageDrawable(animation);
        // start the animation!
        animation.start();


        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // this code will be executed after 2 seconds

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        start.setVisibility(View.VISIBLE);
                        done.setVisibility(View.VISIBLE);

                    }
                });
            }
        }, 11000);


    }

    @Override
    public void onBackPressed() {

    }


}
