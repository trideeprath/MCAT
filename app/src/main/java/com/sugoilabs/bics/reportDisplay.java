package com.sugoilabs.bics;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class reportDisplay extends ActionBarActivity implements View.OnClickListener{

    TextView finalReportTextView;
    TextView reportDate;
    ArrayList<String> reportScore;
    StringBuilder reportStringBuilder;
    String moduleName;
    String moduleScore;
    String reportString="";
    Button emailReport;
    String final_points;
    Intent intent ;
    final String Prefs_Pre_Post_Injury = "Prefs_Pre_Post";
    SharedPreferences prePostInjurySharedPreference;
    String user_detail = "User_Details";
    SharedPreferences userDetailSharedPreference;
    String prePostString;
    Button exit;
    String email;
    String name;
    Map<String,Integer> immediateVisualRecall = new HashMap<String ,Integer>();
    Map<String,Integer> immediateAuditoryRecall = new HashMap<String,Integer>();
    Map<String,Integer> delayedRecall = new HashMap<String,Integer>();
    Map<String,Integer> disinhibition = new HashMap<String,Integer>();
    Map<String,Integer> attentionAndExecutive = new HashMap<String,Integer>();
    Map<String,Integer> semanticOrLanguage = new HashMap<String,Integer>();
    Map<String,Integer> numberRecall = new HashMap<String,Integer>();

    double first =0, second =0, third =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_display);
        finalReportTextView = (TextView) findViewById(R.id.report_textview);
        emailReport = (Button) findViewById(R.id.email_report);
        emailReport.setOnClickListener(this);

        exit = (Button) findViewById(R.id.email_report_exit);
        exit.setOnClickListener(this);

        ArrayList<String> reportScore = reportScoreSave.scoreSave;
        intent = getIntent();
        prePostInjurySharedPreference = getSharedPreferences(Prefs_Pre_Post_Injury, 0);
        prePostString = prePostInjurySharedPreference.getString("pre_post_injury", "0");

        userDetailSharedPreference  = getSharedPreferences(user_detail, 0);
        email = userDetailSharedPreference.getString("email", "abc@gmail.com");
        name = userDetailSharedPreference.getString("name","abcdef");

        Log.d("pre post: ", prePostString);

        final_points = intent.getStringExtra("final_point");
        if(final_points.length() >4){
            final_points = final_points.substring(0,4);
        }
        reportDate = (TextView) findViewById(R.id.report_date);

        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        String dateString = sdf.format(date);
        Log.d("date: ", dateString);

        reportDate.setText(prePostString + "\n" +dateString);



        int j =0;
        for(int i=0 ; i<reportScore.size() ; i ++){
            j = i+1;
            moduleName = reportScore.get(i).split(":")[0];
            moduleScore = reportScore.get(i).split(":")[1];
            Log.d("ModuleName" , moduleName);
            Log.d("ModuleScore" , moduleScore);
            if(moduleScore.length() >3){
                if(i==0 || i==4) {
                    if(moduleScore.length() >6) {
                        moduleScore = moduleScore.substring(0,7);
                    }
                    else{
                        moduleScore = moduleScore.substring(0,6);
                    }
                }
                else{
                    moduleScore = moduleScore.substring(0, 3);
                }
            }
            // saveModuleToScoreArrayList(j, moduleName, moduleScore);
            //printModuleScore();

            putArrayListIntoHashMap(j,moduleName,moduleScore);

            reportString = reportString + moduleName + " : " +moduleScore +"\n";

        }


        //Log.d("ArrayList", reportStringArrayList.toString());

        Log.d("ReportString", reportString.toString());

        String reportStringNew ="Name: "+ name + "\n"+ "Date: "+  dateString + "\n" + printMap(immediateVisualRecall,1) + "\n" + printMap(immediateAuditoryRecall,2)+ "\n" +printMap(delayedRecall,3) + "\n" +printMap(disinhibition,4)+
                "\n" + printMap(attentionAndExecutive,5) +  "\n" + printMap(semanticOrLanguage,6) + "\n" + printMap(numberRecall,7);

        Log.d("ReportStringNew", reportStringNew);


        first = getDouble(reportTimeSave.timeSave.get(0));
        second = getDouble(reportTimeSave.timeSave.get(1));
        third = getDouble(reportTimeSave.timeSave.get(2));

        double totalTimeInTimeModules = findTotalTimeInDouble();
        double totalAvgInTimeModules = findAvgTimeInDouble();
        DecimalFormat df = new DecimalFormat("#.000");

        reportStringNew = reportStringNew + "\n" + "Cognitive Processing Speed - " + df.format(totalTimeInTimeModules);
        reportStringNew = reportStringNew + "\n" + "Visual Connect Dots = " + first;
        reportStringNew = reportStringNew + "\n" + "Visual Picture Drawing = " + second;
        reportStringNew = reportStringNew + "\n" + "Semantic Colors = " + third;
        Log.d("report" , reportString);

        reportString = reportString + "Total Score: " + final_points;

        finalReportTextView.setText(reportStringNew);

        sendReportOnline(reportStringNew);


    }


    private double findAvgTimeInDouble() {
        return (first + second +third)/3;
    }

    private double findTotalTimeInDouble() {
        return first + second +third;
    }

    private double getDouble(Integer integer) {
        String integerString;
        double doubleValue;
        int afterDecimal = 1 + (int)(Math.random() * ((990 - 1) + 1));
        integerString = String.valueOf(integer) + "."+ String.valueOf(afterDecimal);
        return Double.parseDouble(integerString);
    }




    private int findAvgTime() {

        int time=0;
        for(int x: reportTimeSave.timeSave){
            time= time + x;
        }

        return time/3;
    }

    private int findTotalTime() {
        int time=0;
        for(int x: reportTimeSave.timeSave){
            time= time + x;
        }

        return time;
    }


    private void putArrayListIntoHashMap(int j,String moduleName, String moduleScore) {
        moduleScore = moduleScore.trim();

        if(moduleScore.contains(".")){
            moduleScore= moduleScore.split("\\.")[0];
        }
        if(moduleScore.contains(",")){
            moduleScore = moduleScore.split(",")[0];
        }
        int score = 0;
        if(isInteger(moduleScore)){
            score = Integer.parseInt(moduleScore);
        }

        Log.d("Name ", String.valueOf(moduleName));
        Log.d("Score ", String.valueOf(score));

        if(moduleName.equalsIgnoreCase("Immediate Visual Picture Recall") || moduleName.equalsIgnoreCase("Immediate Visual Spatial Select Picture") ||
                moduleName.equalsIgnoreCase("Immediate Visual Drawing Recall")){
            immediateVisualRecall.put(moduleName , score);
        }
        else if(moduleName.equalsIgnoreCase("Immediate Number Recall") || moduleName.equalsIgnoreCase("Immediate Word Recall") ||
                moduleName.equalsIgnoreCase("Immediate Auditory Recall Story")){
            immediateAuditoryRecall.put(moduleName , score);
        }
        else if(moduleName.equalsIgnoreCase("Delayed Auditory Word Recall") || moduleName.equalsIgnoreCase("Delayed Auditory Story Recall") ||
                moduleName.equalsIgnoreCase("Delayed Visual Picture Recall")){
            delayedRecall.put(moduleName,score);
        }

        else if(moduleName.equalsIgnoreCase("Disinhibition V Tap") || moduleName.equalsIgnoreCase("Disinhibition Square and Triangle")){
            score = (int)(score *1.5);
            disinhibition.put(moduleName,score);
        }
        else if(moduleName.equalsIgnoreCase("Cognitive Processing connect the dot") || moduleName.equalsIgnoreCase("Question Attention") ||
                moduleName.equalsIgnoreCase("Attention Auditory Reverse Number Recall")){
            Log.d("Semantic:", moduleName);
            attentionAndExecutive.put(moduleName, score);
        }
        else if(moduleName.equalsIgnoreCase("Semantic Color Score") || moduleName.equalsIgnoreCase("Semantic Question") || moduleName.equalsIgnoreCase("Semantic Group Matching")){
            Log.d("Semantic:", moduleName);
            semanticOrLanguage.put(moduleName,score);
        }
        if(moduleName.equalsIgnoreCase("Immediate Number Recall") || moduleName.equalsIgnoreCase("Attention Auditory Reverse Number Recall") ||
                moduleName.equalsIgnoreCase("Visual Number Recall")){
            numberRecall.put(moduleName,score);
        }



        /*
        if(j==1 || j==8|| j==9){
            immediateVisualRecall.put(moduleName , score);
        }
        else if( j == 7 || j == 2 || j== 5){
            immediateAuditoryRecall.put(moduleName , score);
        }
        else if(j == 13 || j == 17 || j ==15){
            delayedRecall.put(moduleName,score);
        }
        else if(j ==16 || j==14){
            score = (int)(score *1.5);
            disinhibition.put(moduleName,score);
        }
        else if(j ==  10 || j == 6 || j ==3){
            attentionAndExecutive.put(moduleName, score);
        }
        else if(j ==  11 || j == 12 || j == 4) {
            semanticOrLanguage.put(moduleName,score);
        }
        */

    }


    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public static String printMap(Map mp,int groupCount) {
        Iterator it = mp.entrySet().iterator();
        String titleString="";
        String detailString="";
        int totalScore=0;
        int percentage=0;
        String completeString;
        switch (groupCount){
            case 1:
                titleString = "Immediate Visual Recall";
                break;
            case 2:
                titleString = "Immediate Auditory Recall";
                break;
            case 3:
                titleString= "Delayed Recall";
                break;
            case 4:
                titleString= "Disinhibition";
                break;
            case 5:
                titleString = "Attention and Executive";
                break;
            case 6:
                titleString = "Semantic or Language";
                break;
            case 7:
                titleString = "Number Recall";
                break;
            default:
                titleString="";
        }

        titleString = titleString;


        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            //Log.d("Hash Map",pair.getKey() + " = " + pair.getValue());
            detailString = detailString + pair.getKey() + " = " + pair.getValue()+"\n";
            totalScore = totalScore + Integer.parseInt(pair.getValue().toString());
            it.remove(); // avoids a ConcurrentModificationException
        }

        percentage = (int)((totalScore*10)/3);
        titleString = titleString + " total - " + String.valueOf(totalScore) + " is " + String.valueOf(percentage) +"\n";

        completeString= titleString + detailString ;

        Log.d(" Final String" , completeString );

        return completeString;


    }


    private void sendReportOnline(String report){

        if(isOnline()){
            Log.d("email", email);
            Log.d("name", name);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("report", report));
            nameValuePairs.add(new BasicNameValuePair("email", email));
            nameValuePairs.add(new BasicNameValuePair("name", name));

            AsyncTask<List<NameValuePair>, Void, String> Response = new sendLoginDataAndroid().execute(nameValuePairs);
        }else {
            Toast.makeText(this, "Unable to send report to database", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public void showToast(){
        Toast.makeText(this,"Sending report to server", Toast.LENGTH_SHORT).show();
    }


    private class sendLoginDataAndroid extends AsyncTask<List<NameValuePair>, Void , String> {

        @Override
        protected void onPreExecute() {
            showToast();
        }
        @Override
        protected String doInBackground(List<NameValuePair>... loginDetail) {
            AndroidHttpClient client = AndroidHttpClient.newInstance("AndroidAgent");
            //HttpPost httpPost = new HttpPost("http://planourmeet.herokuapp.com/register");
            HttpPost httpPost = new HttpPost("http://regainmemory.org/ws-mct/index.php?menu=insert_mct_info");
            Log.d("background", "inBackground start");
            HttpResponse response;
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(loginDetail[0]));
                response = client.execute(httpPost);
                return EntityUtils.toString(response.getEntity());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                client.close();
                Log.d("background","inBackground end");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("result", result);
            if(result.equals("ok")){
                Log.d("report saved", "yes");
            }
            else {
                Log.d("report saved" , "no");
            }

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_report_display, menu);
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
        if(v.getId() == R.id.email_report){
            Intent email = new Intent(Intent.ACTION_SEND);
            String emailContent = finalReportTextView.getText().toString();
            emailContent = emailContent.replace("Pre-Concussion Injury",name);
            String report= reportDate.getText().toString().replace("Pre-Concussion Injury", name);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
            email.putExtra(Intent.EXTRA_SUBJECT, "DMAC Report");

            //email.putExtra(Intent.EXTRA_TEXT, reportDate.getText().toString()+"\n" +finalReportTextView.getText().toString());
            email.putExtra(Intent.EXTRA_TEXT, report+"\n" + "Dynamic Mobile Assessement of Cognition" + "\n" + emailContent);
            email.setType("message/rfc822");
            startActivity(Intent.createChooser(email, "Choose an Email client :"));
        }
        if(v.getId() == R.id.email_report_exit){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

}
