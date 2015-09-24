package com.sugoilabs.bics;

/*
Remember audio sentences
The user has to hear two audios back to back and has to type or speak the sentence spoken.
1 point each will be awarded for each of them if correct. Comparision is done using semantic similarity not exactly same.
 */

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
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


public class Step4 extends ActionBarActivity implements View.OnClickListener, Runnable {

    Toolbar toolbar;
    LinearLayout answersLayout;
    LinearLayout inputLayout;
    LinearLayout mediaLayout;
    EditText answerTextView;
    TextView audioNumberTextView;
    Button start;
    Button done;
    Button typeInButton;
    Button speakOutButton;
    SeekBar seekbar;
    MediaPlayer player;
    MediaPlayer player2;
    TextView text_shown;
    Handler seekHandler = new Handler();
    int audioNumber = 1;
    int audioTimesPlayed = 0;
    int audio1TimesPlayed = 0;
    int audio2TimesPlayed = 0;
    int score = 0;
    String sentence1;
    String sentence2;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    final String Prefs_Score = "Prefs_Score";
    SharedPreferences scoreSharedPreference;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step4);
        //toolbar = (Toolbar) findViewById(R.id.app_bar);
        //setSupportActionBar(toolbar);
        showInstructionDialogBox();

        scoreSharedPreference = getSharedPreferences(Prefs_Score, 0);
        editor = scoreSharedPreference.edit();


        answersLayout = (LinearLayout) findViewById(R.id.step4_answers);
        answerTextView = (EditText) findViewById(R.id.step4_answers_texview);
        answersLayout.setVisibility(View.INVISIBLE);

        mediaLayout = (LinearLayout) findViewById(R.id.step4_media);
        audioNumberTextView = (TextView) findViewById(R.id.audio_number_textview);

        start = (Button) findViewById(R.id.step4_play);
        start.setOnClickListener(this);

        inputLayout = (LinearLayout) findViewById(R.id.step4_input);
        inputLayout.setVisibility(View.INVISIBLE);

        typeInButton = (Button) findViewById(R.id.step4_typein);
        typeInButton.setOnClickListener(this);

        speakOutButton = (Button) findViewById(R.id.step4_speakout);
        speakOutButton.setOnClickListener(this);

        done = (Button) findViewById(R.id.step4_done);
        done.setOnClickListener(this);
        //done.setVisibility(View.INVISIBLE);

        seekbar = (SeekBar) findViewById(R.id.seek_bar);

        //player = MediaPlayer.create(this, R.raw.audio1);
        player = MediaPlayer.create(this, R.raw.story_johnsneighbor_robert);
        seekbar.setMax(player.getDuration());
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                if (fromUser) {
                    player.seekTo(progress);
                    seekbar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Thread currentThread = new Thread(this);
        currentThread.start();
    }


    public void showInstructionDialogBox() {
        MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(this).title(R.string.instruction).content(R.string.step4_instruction_content).positiveText(R.string.next).cancelable(false);
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
        getMenuInflater().inflate(R.menu.menu_step4, menu);
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
    public void run() {
        int currentPosition = 0;
        int total = player.getDuration();
        seekbar.setMax(total);
        while (player != null && currentPosition < total) {
            try {
                Thread.sleep(500);
                currentPosition = player.getCurrentPosition();
            } catch (InterruptedException e) {
                return;
            } catch (Exception e) {
                return;
            }
            seekbar.setProgress(currentPosition);
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.step4_play) {
            if (!isAttemptOver()) {
                player.start();
                inputLayout.setVisibility(View.VISIBLE);
                increaseAudioPlayedTimes();
            } else {
                Toast.makeText(this, R.string.attemps_over, Toast.LENGTH_SHORT).show();
            }
        }

        if (v.getId() == R.id.step4_typein) {
            LayoutInflater li = LayoutInflater.from(this);
            View promptsView = li.inflate(R.layout.step4_typein_prompt, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(promptsView);
            final EditText userInput = (EditText) promptsView.findViewById(R.id.step4_prompt_editext);
            // set dialog message
            alertDialogBuilder.setCancelable(false).
                    setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // get user input and set it to result
                                    // edit text
                                    Log.d("Words", userInput.getText().toString());
                                    answersLayout.setVisibility(View.VISIBLE);
                                    answerTextView.setText(userInput.getText().toString());
                                    mediaLayout.setVisibility(View.INVISIBLE);
                                    done.setVisibility(View.VISIBLE);
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

        if (v.getId() == R.id.step4_speakout) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, R.string.speak_sentence);

            try {
                startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
            } catch (ActivityNotFoundException a) {
                Toast.makeText(getApplicationContext(), "Speech not supported", Toast.LENGTH_SHORT).show();
            }
        }

        if (v.getId() == R.id.step4_done) {

            if (answerTextView.getText().toString().equals("")) {
                Toast.makeText(this, R.string.provide_answers, Toast.LENGTH_SHORT).show();
            } else {
                if (audioNumber == 1) {
                    sentence1 = answerTextView.getText().toString();
                    //score = calculateScore(sentence1, audioNumber);
                    score = calculateScoreByWords(sentence1, audioNumber);
                    audioNumberTextView.setText(R.string.audio_second);
                    answersLayout.setVisibility(View.INVISIBLE);
                    mediaLayout.setVisibility(View.VISIBLE);
                    player = MediaPlayer.create(this, R.raw.story_marylwanmore);
                    seekbar.setMax(player.getDuration());
                    audioNumber++;
                    answerTextView.setText("");
                    //done.setVisibility(View.INVISIBLE);
                } else if (audioNumber == 2) {
                    sentence2 = answerTextView.getText().toString();
                    //score = score + calculateScore(sentence2, audioNumber);
                    score = score + calculateScoreByWords(sentence2, audioNumber);
                    saveToSharedPreference(score);
                    Log.d("score", String.valueOf(score));
                    startActivity(new Intent(this, patternDraw.class));
                    //startActivity(new Intent(this,FinalScreen.class));
                }
            }
        }
    }

    private void saveToSharedPreference(int points) {
        double totalScore = 0;
        points = points * 2;
        if (scoreSharedPreference.contains("score")) {
            totalScore = Double.valueOf(scoreSharedPreference.getString("score", "0")) + Integer.valueOf(points);
            editor.putString("score", String.valueOf(totalScore));
            editor.commit();
            Log.d("score", String.valueOf(totalScore));
            String ScoreToast = String.valueOf(totalScore);
            if (ScoreToast.length() > 3) {
                ScoreToast = ScoreToast.substring(0, 3);
            }
            Toast.makeText(this, "Points is: " + String.valueOf(points) + " Total Score is:" + ScoreToast, Toast.LENGTH_SHORT).show();
            reportScoreSave.scoreSave.add(getResources().getString(R.string.auditory_immediate_recall_story) + ":" + String.valueOf(points));
        }

    }

    public double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2;
            shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) {
            return 1.0; /* both strings are zero length */
        }
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;

    }

    public int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();
        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }


    private int calculateScoreByWords(String sentence1, int audioNumber) {
        int score = 0;
        String baseString;
        String[] correctWords;

        if (audioNumber == 1) {
            baseString = "John's neighbor Robert visit his vacation home every other month, He bring white dog and golf club, Laptop and  plenty of cigars.";
            String[] correctWords1 = {"John", "Johns", "Robert", "vacation home", "other month", "white dog", "golf club", "laptop", "plenty", "cigar"};
            correctWords = correctWords1;
        } else {
            baseString = "Mary had loaned her lawnmower, shovel, weed spray and pair of gloves to the her nephew, last year. Her nephew only returned Lawnmower, She decided not to loan any more.";
            String[] correctWords1 = {"Mary", "loaned", "lawnmower,", "lawnmower", "shovel", "weed spray", "gloves", "nephew", "last year", "returned", "loan"};
            correctWords = correctWords1;
        }
        String[] words = sentence1.split(" ");
        for (int i = 0; i < correctWords.length; i++) {
            if (sentence1.toLowerCase().contains(correctWords[i].toLowerCase())) {
                score++;
            }
        }

        return score / 2;
    }

    private int calculateScore(String sentence1, int audioNumber) {
        String baseString;
        double similarityScore;
        String answerString = answerTextView.getText().toString();
        answerString = answerString.toLowerCase();
        int score;
        if (audioNumber == 1) {
            baseString = "John's neighbor Robert visit his vacation home every other month, He bring white dog and golf club, Laptop and  plenty of cigars.";
        } else {
            baseString = "Mary had loaned her lawnmower, shovel, weed spray and pair of gloves to the her nephew, last year. Her nephew only returned Lawnmower, She decided not to loan any more.";
        }


        similarityScore = similarity(baseString, answerString);
        Log.d("similarityScore", String.valueOf(similarityScore));

        if (similarityScore > Double.valueOf(".75"))
            score = 5;
        else if (similarityScore > Double.valueOf(".60"))
            score = 4;
        else if (similarityScore > Double.valueOf(".40"))
            score = 3;
        else if (similarityScore > Double.valueOf(".30"))
            score = 2;
        else if (similarityScore > Double.valueOf(".20"))
            score = 1;
        else
            score = 0;

        Log.d("score", String.valueOf(score));
        return score;
    }

    private boolean isAttemptOver() {
        boolean isOver = false;
        if (audioNumber == 1) {
            if (audio1TimesPlayed < 2) {
                isOver = false;
            } else {
                isOver = true;
            }
        }
        if (audioNumber == 2) {
            if (audio2TimesPlayed < 2) {
                isOver = false;
            } else {
                isOver = true;
            }
        }
        return isOver;
    }

    public void increaseAudioPlayedTimes() {
        if (audioNumber == 1) {
            audio1TimesPlayed++;
        }
        if (audioNumber == 2) {
            audio2TimesPlayed++;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    answersLayout.setVisibility(View.VISIBLE);
                    answerTextView.setText(result.get(0));
                    mediaLayout.setVisibility(View.INVISIBLE);
                    done.setVisibility(View.VISIBLE);
                }
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
    }
}
