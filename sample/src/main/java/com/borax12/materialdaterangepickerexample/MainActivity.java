package com.borax12.materialdaterangepickerexample;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.borax12.materialdaterangepicker.time.RadialPickerLayout;
import com.borax12.materialdaterangepicker.time.TimePickerDialog;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements
    DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener
{
    private TextView dateTextView;
    private TextView timeTextView;

    int[] num=new int [17];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(0xEEEEEEEE));*/

        // Find our View instances
        dateTextView = (TextView)findViewById(R.id.date_textview);
        timeTextView = (TextView)findViewById(R.id.time_textview);
        final ImageButton dateButton = (ImageButton)findViewById(R.id.date_button);
        final ImageButton timeButton = (ImageButton)findViewById(R.id.time_button);
        final TextView date_textview = (TextView)findViewById(R.id.date_textview);
        final TextView time_textview = (TextView)findViewById(R.id.time_textview);
        TextView OKBtn = (TextView)findViewById(R.id.OKBtn);
        final EditText titleSchedule = (EditText)findViewById(R.id.titleSchedule);
        final CheckBox everyWeek = (CheckBox)findViewById(R.id.everyWeek);
        final CheckBox allDay = (CheckBox)findViewById(R.id.allDay);
        final LinearLayout weekBox = (LinearLayout)findViewById(R.id.weekBox);
        final CheckBox Mon = (CheckBox)findViewById(R.id.Mon);
        final CheckBox Tue = (CheckBox)findViewById(R.id.Tue);
        final CheckBox Wed = (CheckBox)findViewById(R.id.Wed);
        final CheckBox Thu = (CheckBox)findViewById(R.id.Thu);
        final CheckBox Fri = (CheckBox)findViewById(R.id.Fri);
        final CheckBox Sat = (CheckBox)findViewById(R.id.Sat);
        final CheckBox Sun = (CheckBox)findViewById(R.id.Sun);

        everyWeek.setOnClickListener(new CheckBox.OnClickListener(){
            public void onClick(View v){
                    if(everyWeek.isChecked())
                    {
                        weekBox.setVisibility(View.VISIBLE);
                        dateButton.setVisibility(View.INVISIBLE);
                        date_textview.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        weekBox.setVisibility(View.INVISIBLE);
                        dateButton.setVisibility(View.VISIBLE);
                        date_textview.setVisibility(View.VISIBLE);
                    }
                }
        });
        allDay.setOnClickListener(new CheckBox.OnClickListener(){
            public void onClick(View v){
                if(allDay.isChecked()){
                    timeButton.setVisibility(View.INVISIBLE);
                    time_textview.setVisibility(View.INVISIBLE);
                }
                else
                {
                    timeButton.setVisibility(View.VISIBLE);
                    time_textview.setVisibility(View.VISIBLE);
                }
            }
        });

        // Show a datepicker when the dateButton is clicked
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = com.borax12.materialdaterangepicker.date.DatePickerDialog.newInstance(
                        MainActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        MainActivity.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                );
                tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Log.d("TimePicker", "Dialog was cancelled");
                    }
                });
                tpd.show(getFragmentManager(), "Timepickerdialog");
            }
        });
        OKBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                OutputStream os = null;
                BufferedWriter bOut = null;
                String title = titleSchedule.getText().toString();
                boolean flag=false;
                if(everyWeek.isChecked())
                {
                    flag=true;
                    for(int i=0;i<7;i++){num[i+10]=-1;}
                    if(Mon.isChecked()){flag=false; num[10]=1;}
                    if(Tue.isChecked()){flag=false; num[11]=1;}
                    if(Wed.isChecked()){flag=false; num[12]=1;}
                    if(Thu.isChecked()){flag=false; num[13]=1;}
                    if(Fri.isChecked()){flag=false; num[14]=1;}
                    if(Sat.isChecked()){flag=false; num[15]=1;}
                    if(Sun.isChecked()){flag=false; num[16]=1;}
                }
                if(title.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"일정을 입력하세요",Toast.LENGTH_SHORT).show();
                }
                else if(flag){Toast.makeText(getApplicationContext(),"요일을 입력하세요",Toast.LENGTH_SHORT).show();}
                else if(num[0]<=0&&!everyWeek.isChecked()){Toast.makeText(getApplicationContext(),"날짜를 입력하세요",Toast.LENGTH_SHORT).show();}
                else if((num[6]>num[8])||(num[6]==num[8]&&num[7]>num[9])){Toast.makeText(getApplicationContext(),"시간 설정이 잘못되었습니다",Toast.LENGTH_SHORT).show();}
                else{
                    //file IO
                    String fileName="aYaUserData.txt";
                    String text="";
                    if(allDay.isChecked())
                    {
                        num[6]=0;num[7]=0;num[8]=23;num[9]=59;
                    }
                    if((num[6]==num[8])&&(num[7]==num[9])){
                        if(everyWeek.isChecked()){
                            text = "-2" + "::" + num[10]+"::"+num[11]+"::"+num[12]+"::"+num[13]+"::"+num[14]+"::"+num[15]+"::"+num[16] + "::" + title;
                        }
                        else{
                            text = "-2" + "::" + num[0] + "::" + num[1] + "::" + num[2] + "::" + num[3] + "::" + num[4] + "::" + num[5] + "::" + title;
                        }
                        Toast.makeText(getApplicationContext(), "메모용으로 저장되었습니다", Toast.LENGTH_SHORT).show();
                    }//메모
                    else if(num[0]>0)
                    {
                        text=num[0]+"::"+num[1]+"::"+num[2]+"::"+num[6]+"::"+num[7]+"::"+num[8]+"::"+num[9]+"::"+title;
                    }//날짜로
                    else if(everyWeek.isChecked())
                    {
                        text=num[10]+"::"+num[11]+"::"+num[12]+"::"+num[13]+"::"+num[14]+"::"+num[15]+"::"+num[16]+"::"+num[6]+"::"+num[7]+"::"+num[8]+"::"+num[9]+"::"+title;
                    }//매주
                    try {

                        os=openFileOutput(fileName, Context.MODE_APPEND);
                        bOut=new BufferedWriter(new OutputStreamWriter(os));
                        bOut.write(text+"\r\n");
                        bOut.close();
                    } catch (FileNotFoundException e) {

                        Toast.makeText(getApplicationContext(),"파일 못찾음",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(),"입출력 안됌",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    //intent
                    Intent intent=new Intent(MainActivity.this,homeActivity.class);
                    startActivity(intent);
                    //finish another activity
                    MyScheduleActivity aActivity = (MyScheduleActivity)MyScheduleActivity.AActivity;
                    aActivity.finish();

                    finish();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");
        if(dpd != null) dpd.setOnDateSetListener(this);
    }
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth,int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        String date = year+"/"+(++monthOfYear)+"/"+dayOfMonth+" ~ "+yearEnd+"/"+(++monthOfYearEnd)+"/"+dayOfMonthEnd;
        num[0]=year;
        num[1]=monthOfYear;
        num[2]=dayOfMonth;
        num[3]=yearEnd;
        num[4]=monthOfYearEnd;
        num[5]=dayOfMonthEnd;
        dateTextView.setText(date);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int hourOfDayEnd, int minuteEnd) {
        String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        String hourStringEnd = hourOfDayEnd < 10 ? "0"+hourOfDayEnd : ""+hourOfDayEnd;
        String minuteStringEnd = minuteEnd < 10 ? "0"+minuteEnd : ""+minuteEnd;
        String time = hourString+"h"+minuteString+" ~ "+hourStringEnd+"h"+minuteStringEnd;

        num[6]=hourOfDay;
        num[7]=minute;
        num[8]=hourOfDayEnd;
        num[9]=minuteEnd;

        timeTextView.setText(time);
    }


}
