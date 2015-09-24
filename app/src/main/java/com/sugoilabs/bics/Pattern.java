package com.sugoilabs.bics;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;


public class Pattern extends ActionBarActivity implements View.OnClickListener {

    Toolbar toolbar;
    Button show;
    Button draw;
    final String Prefs_Score = "Prefs_Score";
    SharedPreferences scoreSharedPreference;
    SharedPreferences.Editor scoreEditor;
    LinearLayout imageLayout ;
    ImageView imageView;
    int attempts;
    Intent nextActivity;
    static final int PICK_CONTACT_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern);
        //toolbar = (Toolbar) findViewById(R.id.app_bar);
        //setSupportActionBar(toolbar);
        showInstructionDialogBox();
        initializeLayout();

    }

    private void initializeLayout() {
        show = (Button) findViewById(R.id.pattern_show);
        show.setOnClickListener(this);


        draw = (Button) findViewById(R.id.pattern_draw);
        draw.setOnClickListener(this);

        imageLayout = (LinearLayout) findViewById(R.id.pattern_picture_layout);

        imageView = (ImageView) findViewById((R.id.pattern_picture_image_view));

        imageView.setVisibility(View.INVISIBLE);

    }

    public void showInstructionDialogBox(){
        MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(this).title(R.string.instruction).content(R.string.patternDrawInstruction).positiveText(R.string.next).cancelable(false);
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
        getMenuInflater().inflate(R.menu.menu_pattern, menu);
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
        if(v.getId()== R.id.pattern_show){
            boolean wrapInScrollView = true;
            new MaterialDialog.Builder(this).title("Pattern").customView(R.layout.pattern_show_layout, wrapInScrollView).positiveText("OK").build().show();
        }
        if(v.getId() == R.id.pattern_draw){
            startActivity(new Intent(this, patternDraw.class));
        }
    }

    @Override
    public void onBackPressed() {
    }



}
