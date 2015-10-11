package com.sugoilabs.bics;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Trideep on 11-10-2015.
 */
public class storyData {
    public static Context context;
    public storyData(Context context) {
        this.context = context;
    }

    public static ArrayList<Integer> storyArray = new ArrayList<Integer>();
    public static ArrayList<String> answerStringArray = new ArrayList<String>();
    public static int set=0;
    public static void setStoryArray(){
        Random rand = new Random();
        set = rand.nextInt(2);
        Log.d("story set", String.valueOf(set));

        switch (set){
            case 0: storyArray.add(R.raw.story_johnsneighbor_robert);
                storyArray.add(R.raw.story_marylwanmore);
                answerStringArray.add(context.getResources().getString(R.string.story_one_string));
                answerStringArray.add(context.getResources().getString(R.string.story_two_string));
                set = 0;
                break;

            case 1:
                storyArray.add(R.raw.hampton);
                storyArray.add(R.raw.johnson);
                answerStringArray.add(context.getResources().getString(R.string.story_three_string));
                answerStringArray.add(context.getResources().getString(R.string.story_four_string));
                //answerStringArray = context.getString(R.string.base_word_audio_flash_answer2);
                set = 1;
                break;


        }
    }

}
