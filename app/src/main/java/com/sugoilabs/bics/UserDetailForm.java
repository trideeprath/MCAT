package com.sugoilabs.bics;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sugoilabs.bics.R;


public class UserDetailForm extends ActionBarActivity implements View.OnClickListener {

    Button nextButton;
    EditText nameEditText;
    EditText emailEditText;
    TextView incorrectTextView;
    String email;
    String name;

    final String user_detail = "User_Details";
    SharedPreferences userDetailSharedPreference;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail_form);
        initializeLayout();
    }

    private void initializeLayout() {
        nextButton = (Button)findViewById(R.id.user_detail_next);
        nextButton.setOnClickListener(this);

        nameEditText = (EditText) findViewById(R.id.textview_name);
        emailEditText = (EditText) findViewById(R.id.textview_email);
        incorrectTextView = (TextView)findViewById(R.id.user_details_missing);

        userDetailSharedPreference = getSharedPreferences(user_detail, 0);
        editor = userDetailSharedPreference.edit();
        editor.clear().commit();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_detail_form, menu);
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
    public void onClick(View view) {
        if(view.getId() == R.id.user_detail_next){
            email = emailEditText.getText().toString();
            name  = nameEditText.getText().toString();
            if(!email.isEmpty() && !name.isEmpty() && isValidEmailAddress(email)){

                editor.putString("email", email);
                editor.putString("name" , name);
                editor.commit();
                startActivity(new Intent(this, InitialQuestionForm.class));
            }
            else {
                incorrectTextView.setVisibility(View.VISIBLE);
            }

        }
    }


    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

}
