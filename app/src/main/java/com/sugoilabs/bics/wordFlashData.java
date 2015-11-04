package com.sugoilabs.bics;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Trideep on 10-10-2015.
 */
public class wordFlashData {

    public static Context context;
    public wordFlashData(Context context) {
        this.context = context;
    }

    public static ArrayList<Integer> wordFlashDrawableArray = new ArrayList<Integer>();
    public static String answerString;
    public static int set=0;

    public static void setContext(Context cxt){
        context = cxt;
    }

    public static void setWordFlashDrawableArray(){
        Random rand = new Random();
        set = rand.nextInt(3);
        //set =2;
        Log.d("audio set", String.valueOf(set));

        switch (set){
            case 0: wordFlashDrawableArray.add(R.raw.audio_apple);
                wordFlashDrawableArray.add(R.raw.audio_rose);
                wordFlashDrawableArray.add(R.raw.audio_money);
                wordFlashDrawableArray.add(R.raw.audio_table);
                wordFlashDrawableArray.add(R.raw.audio_yellow);
                answerString = context.getString(R.string.base_word_audio_flash_answer1);
                set = 0;
                break;

            case 1:
                wordFlashDrawableArray.add(R.raw.violet);
                wordFlashDrawableArray.add(R.raw.zoo);
                wordFlashDrawableArray.add(R.raw.pen);
                wordFlashDrawableArray.add(R.raw.cup);
                wordFlashDrawableArray.add(R.raw.rope);
                answerString = context.getString(R.string.base_word_audio_flash_answer2);
                set = 1;
                break;

            case 2:
                wordFlashDrawableArray.add(R.raw.ring);
                wordFlashDrawableArray.add(R.raw.bear);
                wordFlashDrawableArray.add(R.raw.plate);
                wordFlashDrawableArray.add(R.raw.door);
                wordFlashDrawableArray.add(R.raw.laptop);
                answerString = context.getString(R.string.base_word_audio_flash_answer3);
                set = 2;
                break;

        }
    }


    public static void unsetWordFlashDrawableArray(){
        wordFlashDrawableArray.clear();

    }

}
