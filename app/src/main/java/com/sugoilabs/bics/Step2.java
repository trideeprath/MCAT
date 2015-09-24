package com.sugoilabs.bics;

/*
Flashing words.
5 words will flash(Rose, Table , Apple , Penny , Lip) each after 2second interval. The user has to memorize the words.
He will be given three attempts to see the flashing words. He could answer by typing or speaking.
If he answers all of them ?? points will be awarded

 */

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Step2 extends ActionBarActivity implements View.OnClickListener {

    Toolbar toolbar;
    Button start;
    Button typeInButton;
    Button speakOutButton;
    Button done;
    final String Prefs_Score = "Prefs_Score";
    SharedPreferences scoreSharedPreference;
    SharedPreferences.Editor scoreEditor;
    Animation myFadeInAnimation;
    Animation myFadeInAnimation1;
    AnimationSet animationSet;
    LinearLayout inputLayout;
    LinearLayout rose_layout;
    LinearLayout table_layout;
    LinearLayout apple_layout;
    LinearLayout penny_layout;
    LinearLayout lip_layout;
    LinearLayout answers_layout;
    TextView answers_textview;
    int attempts;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private TextView txtSpeechInput;
    Intent nextActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step2);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        scoreSharedPreference = getSharedPreferences(Prefs_Score, 0);
        scoreEditor = scoreSharedPreference.edit();

        showInstructionDialogBox();




        answers_layout =(LinearLayout)findViewById(R.id.step2_answers);
        answers_textview = (TextView)findViewById(R.id.step2_answers_texview);

        answers_layout.setVisibility(View.INVISIBLE);

        start = (Button) findViewById(R.id.step2_start);
        start.setOnClickListener(this);

        inputLayout = (LinearLayout) findViewById(R.id.step2_input);
        inputLayout.setVisibility(View.INVISIBLE);

        typeInButton = (Button) findViewById(R.id.step2_typein);
        typeInButton.setOnClickListener(this);

        speakOutButton = (Button) findViewById(R.id.step2_speakout);
        speakOutButton.setOnClickListener(this);

        done = (Button) findViewById(R.id.step2_done);
        done.setOnClickListener(this);

        setAllWordLayouts();


    }

    public void showInstructionDialogBox(){

        MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(this).title(R.string.instruction).content(R.string.step2_insturction_content).positiveText(R.string.got_it).cancelable(false);
        dialogBuilder.show();
        dialogBuilder.callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                Log.d("OK ", "great");
            }
        });
    }

    public void setAllWordLayouts() {
        animationSet = new AnimationSet(true);
        rose_layout = (LinearLayout) findViewById(R.id.step2_rose);
        rose_layout.setVisibility(View.INVISIBLE);

        table_layout = (LinearLayout) findViewById(R.id.step2_table);
        table_layout.setVisibility(View.INVISIBLE);

        apple_layout = (LinearLayout) findViewById(R.id.step2_apple);
        apple_layout.setVisibility(View.INVISIBLE);

        penny_layout = (LinearLayout) findViewById(R.id.step2_penny);
        penny_layout.setVisibility(View.INVISIBLE);

        lip_layout = (LinearLayout) findViewById(R.id.step2_lip);
        lip_layout.setVisibility(View.INVISIBLE);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_step2, menu);
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
        if (v.getId() == R.id.step2_start) {
            if (attempts < 3) {
                answers_layout.setVisibility(View.INVISIBLE);
                done.setVisibility(View.VISIBLE);
                setAndShowAllAnimations();
                start.setText(R.string.try_again);
                inputLayout.setVisibility(View.VISIBLE);
                attempts++;
            } else {
                Toast.makeText(this, R.string.attemps_over, Toast.LENGTH_SHORT).show();
            }
        }

        if (v.getId() == R.id.step2_typein) {
            LayoutInflater li = LayoutInflater.from(this);
            View promptsView = li.inflate(R.layout.step2_typein_prompt, null);
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
                                    answers_layout.setVisibility(View.VISIBLE);
                                    answers_textview.setText(userInput.getText().toString());
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

        if(v.getId() == R.id.step2_speakout){
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,R.string.speak_words);
            try {
                startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
            } catch (ActivityNotFoundException a) {
                Toast.makeText(getApplicationContext(),"Speech not supported",Toast.LENGTH_SHORT).show();
            }
        }

        if(v.getId() == R.id.step2_done){
            if(answers_textview.getText().toString().equals("")  ){
                Toast.makeText(this,R.string.provide_answers , Toast.LENGTH_SHORT ).show();
            }
            else{
                int score = calculateScore();
                Log.d("Score",String.valueOf(score));
                startActivity(new Intent(this, Step4.class));
            }
        }
    }

    private int calculateScore() {
        int score=1;
        boolean isCorrect= true;
        String answers = answers_textview.getText().toString();
        String[] answersArray = answers.split(" ");

        ArrayList<String> answerList = new ArrayList<String>();
        ArrayList<String> baseAnswerList = new ArrayList<String>();
        baseAnswerList.add("rose");baseAnswerList.add("table");baseAnswerList.add("apple");baseAnswerList.add("penny");baseAnswerList.add("lip");

        for(String ans : answersArray){
            answerList.add(ans.toLowerCase());
        }

        Log.d("answers", answerList.toString());

        for(String ans: baseAnswerList){
            if(!answerList.contains(ans)){
                 isCorrect = false;
            }
        }
        if(isCorrect ){
            score =1;
        }
        else{
            score= 0;
        }
        return score;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    answers_layout.setVisibility(View.VISIBLE);
                    answers_textview.setText(result.get(0));
                }
                break;
            }
        }
    }

    private void setAndShowAllAnimations() {
        myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.layout_flash);
        myFadeInAnimation.setStartOffset(500);
        rose_layout.setAnimation(myFadeInAnimation);
        animationSet.addAnimation(rose_layout.getAnimation());

        myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.layout_flash);
        myFadeInAnimation.setStartOffset(1500);
        table_layout.setAnimation(myFadeInAnimation);
        animationSet.addAnimation(table_layout.getAnimation());


        myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.layout_flash);
        myFadeInAnimation.setStartOffset(2500);
        apple_layout.setAnimation(myFadeInAnimation);
        animationSet.addAnimation(apple_layout.getAnimation());

        myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.layout_flash);
        myFadeInAnimation.setStartOffset(3500);
        penny_layout.setAnimation(myFadeInAnimation);
        animationSet.addAnimation(penny_layout.getAnimation());

        myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.layout_flash);
        myFadeInAnimation.setStartOffset(4500);
        lip_layout.setAnimation(myFadeInAnimation);
        animationSet.addAnimation(lip_layout.getAnimation());

        List<Animation> animations = animationSet.getAnimations();
        rose_layout.requestLayout();
        table_layout.requestLayout();
        apple_layout.requestLayout();
        penny_layout.requestLayout();
        lip_layout.requestLayout();

        for (Animation animation : animations) {
            animation.start();
        }

    }

    private void checkAndSaveScoreToSharedPreference() {

    }

    @Override
    public void onBackPressed() {
    }
}
