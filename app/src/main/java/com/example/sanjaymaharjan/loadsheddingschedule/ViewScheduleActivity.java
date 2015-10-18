package com.example.sanjaymaharjan.loadsheddingschedule;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.content.res.Resources;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ViewScheduleActivity extends AppCompatActivity {
    private Resources r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        r = getResources();
        //main layout
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setBackgroundColor(Color.BLACK);


        LayoutParams mainLayoutDetails = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
        );

        Helper helper = new Helper();
        //String jsonString = helper.getJSON("http://www.maharjansanjay.co.nf/GetLoadshedding.php",2000);
        String jsonString = loadJSONFromAsset("loadshedding");
        try {
            JSONObject Schedule = new JSONObject(jsonString);
            JSONArray groups = Schedule.getJSONArray("Schedule");

            for (int i = 0; i < groups.length(); i++) {
                JSONObject group = groups.getJSONObject(i);
                if (group.getInt("Group") == 1) {
                    JSONArray routine = group.getJSONArray("Routine");
                    for (int j = 0; j < routine.length(); j++) {
                        Calendar calendar = Calendar.getInstance();
                        int today = calendar.get(Calendar.DAY_OF_WEEK);

                        JSONObject dayRoutine = routine.getJSONObject(j);
                        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);

                        //Calendar calendar = Calendar.getInstance();


                        //box layout
                        LinearLayout boxLayout = new LinearLayout(this);
                        boxLayout.setOrientation(LinearLayout.VERTICAL);

                        if (today == dayRoutine.getInt("Day")) {
                            boxLayout.setBackgroundColor(Color.GREEN);
                        } else {
                            boxLayout.setBackgroundColor(Color.GRAY);
                        }
                        int boxPadding = Helper.GetDP(5, r);
                        boxLayout.setPadding(boxPadding,boxPadding,boxPadding,boxPadding);

                        LinearLayout.LayoutParams boxLayoutDetails = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

                        int boxMargin = Helper.GetDP(5, r);
                        boxLayoutDetails.setMargins(boxMargin, boxMargin, boxMargin, boxMargin);

                        TextView day = new TextView(this);
                        //String dayText = dayRoutine.getString("Day");
                        String dayText = GetDayString(dayRoutine.getInt("Day"));
                        day.setText(dayText);

                        boxLayout.addView(day);

                        JSONArray shifts = dayRoutine.getJSONArray("Shift");
                        for (int k = 0; k < shifts.length(); k++) {
                            JSONObject shift = shifts.getJSONObject(k);
                            String startTime = shift.getString("StartTime");
                            String endTime = shift.getString("EndTime");
                            TextView shiftView = new TextView(this);
                            shiftView.setText("shift " + (k + 1) + ": " + startTime + " - " + endTime);

                            boxLayout.addView(shiftView);
                        }

                        mainLayout.addView(boxLayout, boxLayoutDetails);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setContentView(mainLayout, mainLayoutDetails);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_schedule, menu);
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

    public String loadJSONFromAsset(String filename) {
        String file = filename + ".json";
        String json = null;
        try {

            InputStream is = getAssets().open(file);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public String GetDayString(int day) {
        switch (day) {
            case 1: {
                return "Sunday";
            }
            case 2: {
                return "Monday";
            }
            case 3: {
                return "Tuesday";
            }
            case 4: {
                return "Wednesday";
            }
            case 5: {
                return "Thursday";
            }
            case 6: {
                return "Friday";
            }
            case 7: {
                return "Saturday";
            }
            default:
                return "";
        }
    }
}
