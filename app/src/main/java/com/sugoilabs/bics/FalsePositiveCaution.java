package com.sugoilabs.bics;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class FalsePositiveCaution extends ActionBarActivity implements View.OnClickListener{

    Button nextButton;
    Intent nextActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_false_positive_caution);

        nextButton = (Button) findViewById(R.id.activity_false_positive_next_button);
        nextButton.setOnClickListener(this);

        //nextActivity = new Intent(this, Step1.class);
        nextActivity = new Intent(this, UserDetailForm.class);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_false_positive_caution, menu);
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
        if(v.getId() == R.id.activity_false_positive_next_button){
            startActivity(nextActivity);
        }
    }

    @Override
    public void onBackPressed() {
    }
}
