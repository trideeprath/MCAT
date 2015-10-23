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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import org.apache.http.NameValuePair;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class WordAudioFlash extends ActionBarActivity implements View.OnClickListener{

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_audio_flash);
        //toolbar = (Toolbar) findViewById(R.id.app_bar);
        //setSupportActionBar(toolbar);

        wordFlashData wordFlashData = new wordFlashData(this);
        wordFlashData.setWordFlashDrawableArray();
        showInstructionDialogBox();
        initializeLayouts();
    }

    private void initializeLayouts() {
        mediaLayout = (LinearLayout) findViewById(R.id.word_audio_media);
        start = (Button) findViewById(R.id.word_audio_start);
        start.setOnClickListener(this);
        done = (Button) findViewById(R.id.word_audio_done);
        done.setOnClickListener(this);
        done.setVisibility(View.INVISIBLE);

        player = MediaPlayer.create(this, R.raw.audio_apple);

        wordNumberTextView = (TextView) findViewById(R.id.audio_number_textview);
    }

    private void showInstructionDialogBox() {
        MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(this).title(R.string.instruction).content(R.string.wordAudioInstruction).positiveText(R.string.next).cancelable(false);
        dialogBuilder.show();
        dialogBuilder.callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                Log.d("OK ", "great");
            }
        });
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
            //nextActivityIntent = new Intent(this,SimilarPicture.class);
            //nextActivityIntent = new Intent(this,WordAudioFlashAnswerImmediate.class);
            nextActivityIntent = new Intent(this,WordAudioFlashAnswerImmediateTab.class);
            startActivity(nextActivityIntent);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_word_audio_flash, menu);
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
        if(wordNumber == 5){
            wordNumberTextView.setText("Completed");
            start.setText(R.string.restart);
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

                player = MediaPlayer.create(WordAudioFlash.this, wordFlashData.wordFlashDrawableArray.get(0));


                player.start();
                audioNumber++;
                int count=0;
                publishProgress(audioNumber);
                Thread.sleep(2000);

                player = MediaPlayer.create(WordAudioFlash.this, wordFlashData.wordFlashDrawableArray.get(1));
                player.start();
                audioNumber++;
                publishProgress(audioNumber);
                Thread.sleep(2000);

                //player = MediaPlayer.create(WordAudioFlash.this, R.raw.audio_penny);
                player = MediaPlayer.create(WordAudioFlash.this, wordFlashData.wordFlashDrawableArray.get(2));

                player.start();
                audioNumber++;
                publishProgress(audioNumber);
                Thread.sleep(2000);

                player = MediaPlayer.create(WordAudioFlash.this, wordFlashData.wordFlashDrawableArray.get(3));
                player.start();
                audioNumber++;
                publishProgress(audioNumber);
                Thread.sleep(2000);

                player = MediaPlayer.create(WordAudioFlash.this, wordFlashData.wordFlashDrawableArray.get(4));
                player.start();
                audioNumber++;
                publishProgress(audioNumber);
                Thread.sleep(2000);

                /*
                player = MediaPlayer.create(WordAudioFlash.this, R.raw.audio_apple);


                player.start();
                audioNumber++;
                int count=0;
                publishProgress(audioNumber);
                Thread.sleep(2000);

                player = MediaPlayer.create(WordAudioFlash.this, R.raw.audio_rose);
                player.start();
                audioNumber++;
                publishProgress(audioNumber);
                Thread.sleep(2000);

                //player = MediaPlayer.create(WordAudioFlash.this, R.raw.audio_penny);
                player = MediaPlayer.create(WordAudioFlash.this, R.raw.audio_money);

                player.start();
                audioNumber++;
                publishProgress(audioNumber);
                Thread.sleep(2000);

                player = MediaPlayer.create(WordAudioFlash.this, R.raw.audio_table);
                player.start();
                audioNumber++;
                publishProgress(audioNumber);
                Thread.sleep(2000);

                player = MediaPlayer.create(WordAudioFlash.this, R.raw.audio_yellow);
                player.start();
                audioNumber++;
                publishProgress(audioNumber);
                Thread.sleep(2000);
                */

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
            setWordNumberText(5);
            done.setVisibility(View.VISIBLE);

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
