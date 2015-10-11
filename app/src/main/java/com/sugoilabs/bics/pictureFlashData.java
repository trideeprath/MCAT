package com.sugoilabs.bics;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Trideep on 10-10-2015.
 */
public class pictureFlashData {

    public static Context context;
    public pictureFlashData(Context context) {
        this.context = context;
    }
    public static ArrayList<Drawable> pictureFlashDrawableArray = new ArrayList<Drawable>();
    public static String answerString;
    public static int set=0;
    public static void setPictureFlashDrawableArray(){
        Random rand = new Random();
        set = rand.nextInt(3);
        //set =2;
        switch (set){
            case 0: pictureFlashDrawableArray.add(context.getResources().getDrawable(R.drawable.cow));
                    pictureFlashDrawableArray.add(context.getResources().getDrawable(R.drawable.bird));
                    pictureFlashDrawableArray.add(context.getResources().getDrawable(R.drawable.tree));
                    pictureFlashDrawableArray.add(context.getResources().getDrawable(R.drawable.boat));
                    pictureFlashDrawableArray.add(context.getResources().getDrawable(R.drawable.key));
                    answerString = context.getString(R.string.base_picture_flash_answer1);
                    set = 0;
                    break;

            case 1: pictureFlashDrawableArray.add(context.getResources().getDrawable(R.drawable.duck));
                    pictureFlashDrawableArray.add(context.getResources().getDrawable(R.drawable.lamp));
                    pictureFlashDrawableArray.add(context.getResources().getDrawable(R.drawable.leaf));
                    pictureFlashDrawableArray.add(context.getResources().getDrawable(R.drawable.chair));
                    pictureFlashDrawableArray.add(context.getResources().getDrawable(R.drawable.kite));
                    answerString = context.getString(R.string.base_picture_flash_answer2);
                    set = 1;
                    break;

            case 2: pictureFlashDrawableArray.add(context.getResources().getDrawable(R.drawable.clock));
                    pictureFlashDrawableArray.add(context.getResources().getDrawable(R.drawable.bus));
                    pictureFlashDrawableArray.add(context.getResources().getDrawable(R.drawable.cat));
                    pictureFlashDrawableArray.add(context.getResources().getDrawable(R.drawable.spoon));
                    pictureFlashDrawableArray.add(context.getResources().getDrawable(R.drawable.shoe));
                    answerString = context.getString(R.string.base_picture_flash_answer3);
                    set = 2;
                    break;

        }
    }

}
