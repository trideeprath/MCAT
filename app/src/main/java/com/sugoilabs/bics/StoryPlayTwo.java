package com.sugoilabs.bics;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sugoilabs.bics.R;

import java.util.Timer;
import java.util.TimerTask;

public class StoryPlayTwo extends ActionBarActivity implements View.OnClickListener {


    Toolbar toolbar;
    LinearLayout mediaLayout;
    TextView wordNumberTextView;
    Button start;
    Button done;
    MediaPlayer player;
    TextView text_shown;
    Handler seekHandler = new Handler();
    int audioPlayedCount =0;
    int audioNumber=0;
    int score = 0;
    Intent nextActivityIntent;
    storyData sd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_play_two);
        showInstructionDialogBox();
        initializeLayouts();
        sd = new storyData(this);
    }


    public void showInstructionDialogBox() {
        MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(this).title(R.string.instruction).content(R.string.story_play_one_instruction).positiveText(R.string.next).cancelable(false);
        dialogBuilder.show();
        dialogBuilder.callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                Log.d("OK ", "great");
            }
        });
    }



    private void initializeLayouts() {
        mediaLayout = (LinearLayout) findViewById(R.id.word_audio_media);
        start = (Button) findViewById(R.id.word_audio_start);
        start.setOnClickListener(this);
        done = (Button) findViewById(R.id.word_audio_done);
        done.setOnClickListener(this);
        player = MediaPlayer.create(this, R.raw.audio_apple);

        wordNumberTextView = (TextView) findViewById(R.id.audio_number_textview);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_story_play_two, menu);
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


    public void setWordNumberText(int wordNumber){
        if(wordNumber == 1){
            wordNumberTextView.setText("Playing...");
        }
        if(wordNumber == 2){
            wordNumberTextView.setText("Completed");
            start.setText(R.string.restart);
        }

    }


    @Override
    public void onClick(View v){
        if (v.getId() == R.id.word_audio_start) {
            if(audioPlayedCount<2){
                audioNumber=0;
                AsyncTask<String, Integer, Void> Response = new playAudio().execute();
                audioPlayedCount++;
            } else {
                Toast.makeText(this, R.string.attemps_over, Toast.LENGTH_SHORT).show();
            }
        }
        if(v.getId() == R.id.word_audio_done){
            //nextActivityIntent = new Intent(this,WordAudioFlashAnswerImmediateTab.class);
            Intent intent = getIntent();
            int score = intent.getIntExtra("StoryOneScore",0);
            nextActivityIntent = new Intent(this, ImmediateRecallStoryTwo.class);
            nextActivityIntent.putExtra("StoryOneScore",score);
            startActivity(nextActivityIntent);
        }
    }




    private class playAudio extends AsyncTask<String, Integer, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setWordNumberText(1);

        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                //storyData.setStoryArray();
                player = MediaPlayer.create(StoryPlayTwo.this, storyData.storyArray.get(1));

                //player = MediaPlayer.create(StoryPlayTwo.this, R.raw.story_marylwanmore);

                player.start();
                audioNumber++;
                int count=0;
                Thread.sleep(player.getDuration());


                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // this code will be executed after 2 seconds
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                done.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }, 0);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            setWordNumberText(2);

        }

        //Not being called
        protected void onProgressUpdate(Integer... progress){
            int progressWord = progress[0];
            //setWordNumberText(progressWord);
        }
    }

    @Override
    public void onBackPressed() {
    }





}
