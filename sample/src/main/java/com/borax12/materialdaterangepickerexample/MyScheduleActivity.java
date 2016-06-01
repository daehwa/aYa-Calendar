package com.borax12.materialdaterangepickerexample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.borax12.materialdaterangepicker.floatingactionbutton.FloatingActionButton;
import com.imanoweb.calendarview.CalendarListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;


/**
 * Created by snote on 2016-05-07.
 */

public class MyScheduleActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
{
    private ListView mListView = null;
    private ListViewAdapter mAdapter = null;
    public static Activity AActivity;
    private ArrayList<ListData> mListData = new ArrayList<ListData>();
    private String [] tempSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule);

        /*android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(0xEEEEEEEE));*/

        AActivity = MyScheduleActivity.this;
        mListView = (ListView) findViewById(R.id.mListView);
        //read file


        //Floating action btn
        FloatingActionButton addSchedule = (FloatingActionButton) findViewById(R.id.addSchedule);
        addSchedule.attachToListView(mListView);

        addSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyScheduleActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //delete btn
        mListView.setOnItemClickListener(this);




        //Initialize CustomCalendarView from layout
        com.imanoweb.calendarview.CustomCalendarView calendarView = (com.imanoweb.calendarview.CustomCalendarView) findViewById(R.id.calendar_view);

        //Initialize calendar with date
        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());

        //Show Monday as first date of week
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);

        //Show/hide overflow days of a month
        calendarView.setShowOverflowDate(false);

        //call refreshCalendar to update calendar the view
        calendarView.refreshCalendar(currentCalendar);

        //Handling custom calendar events
        calendarView.setCalendarListener(new CalendarListener() {
            @Override
            public void onDateSelected(Date date) {
                mListData.clear();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                String str=df.format(date);
                String []data=str.split("-");
                getScheduleShow(data);
            }

            @Override
            public void onMonthChanged(Date date) {
                mListData.clear();
            }
        });
    }

    private class ViewHolder {
        public TextView mText;
        public TextView startHour;
        public TextView startMin;
        public TextView endHour;
        public TextView endMin;
        public TextView memo;
        public LinearLayout timeSlot;
    }
    private class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;

        public ListViewAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.custom_schedule, null);

                holder.mText = (TextView) convertView.findViewById(R.id.title);
                holder.startHour = (TextView) convertView.findViewById(R.id.startHour);
                holder.startMin = (TextView) convertView.findViewById(R.id.startMin);
                holder.endHour = (TextView) convertView.findViewById(R.id.endHour);
                holder.endMin = (TextView) convertView.findViewById(R.id.endMin);
                holder.memo = (TextView) convertView.findViewById(R.id.memo);
                holder.timeSlot = (LinearLayout) convertView.findViewById(R.id.timeSlot);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            ListData mData = mListData.get(position);

            holder.mText.setText(mData.mTitle);
            holder.startHour.setText(mData.startHour);
            holder.startMin.setText(mData.startMin);
            holder.endHour.setText(mData.endHour);
            holder.endMin.setText(mData.endMin);
            holder.memo.setText(mData.memo);

            holder.memo.setVisibility(View.GONE);
            holder.timeSlot.setVisibility(View.VISIBLE);
            if(mData.memo!="") {
                holder.memo.setVisibility(View.VISIBLE);
                holder.timeSlot.setVisibility(View.GONE);
            }

            return convertView;
        }
        //overloading
        public void addItem(String mTitle,String startHour,String startMin,String endHour,String endMin,String memo) {
            ListData addInfo = new ListData();
            addInfo.mTitle = mTitle;
            addInfo.startHour = startHour;
            addInfo.startMin = startMin;
            addInfo.endHour = endHour;
            addInfo.endMin = endMin;
            addInfo.memo=memo;

            mListData.add(addInfo);
            mAdapter.notifyDataSetChanged();
        }

        public void remove(int position) {
            mListData.remove(position);
            dataChange();
        }

        public void sort() {
            Collections.sort(mListData, ListData.NUM_COMPARATOR);
            dataChange();
        }

        public void dataChange() {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void onItemClick(AdapterView<?> parent, View v, final int position, long id)
    {
        //삭제 다이얼로그에 보여줄 메시지
        String message = "해당 일정을 삭제하시겠습니까?";

        DialogInterface.OnClickListener deleteListener = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //선택된 아이템을 리스트에서 삭제
                mListData.remove(position);
                mAdapter.notifyDataSetChanged();
                try{
                    File file = new File(getFilesDir().getAbsolutePath()+"/"+"aYaUserData.txt");
                    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                    String dummy = "";
                    String line=br.readLine();
                    while(!line.equals(tempSchedule[position]))
                    {
                        System.out.println("비우기전 line: "+line);
                        line = br.readLine();//읽으며이동
                        dummy += ( line + "\r\n" );
                    }
                    System.out.println("삭제될내용 "+tempSchedule[position]+" "+line);
                    while((line=br.readLine())!=null){
                        dummy +=(line+"\r\n");
                        System.out.println("비운후 line: "+line);
                    }
                    FileWriter fw = new FileWriter(getFilesDir().getAbsolutePath()+"/"+"aYaUserData.txt");
                    fw.write(dummy);
                    fw.close();
                    br.close();
                }
                catch(Exception e){
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        };

        //삭제 물어보는 다이얼로그 생성
        new AlertDialog.Builder(this)
                .setMessage(Html.fromHtml(message))
                .setPositiveButton("삭제", deleteListener)
                .show();
    }
    public void getScheduleShow(String [] dayData)
    {
        tempSchedule=new String[50];
        int n=0;
        try
        {
            InputStream is=openFileInput("aYaUserData.txt");
            BufferedReader bIn=new BufferedReader(new InputStreamReader(is,"euc-kr"));
            String str;
            String [] data;
            int date = (Integer.valueOf(dayData[0]) + ((Integer.valueOf(dayData[1]) + 1) * 26) / 10 + Integer.valueOf(dayData[2]) % 100 + (Integer.valueOf(dayData[2]) % 100) / 4 + Integer.valueOf(dayData[2]) / 400 - 2 * Integer.valueOf(dayData[2]) / 100 +5) % 7;
            boolean flag=true;
            while((str=bIn.readLine())!=null)
            {
                System.out.println(str);
                data=str.split("::");
                if((Integer.valueOf(data[0])==-2)&&Integer.valueOf(data[1])%1000==Integer.valueOf(dayData[2])%1000&&Integer.valueOf(data[2])==Integer.valueOf(dayData[1])&&Integer.valueOf(data[3])==Integer.valueOf(dayData[0]))
                {
                    tempSchedule[n]=str;
                    n++;
                    flag=false;
                    mAdapter = new ListViewAdapter(this);
                    mListView.setAdapter(mAdapter);
                    if(data.length==8){mAdapter.addItem(data[7],String.valueOf(data[0]),String.valueOf(data[0]),String.valueOf(data[0]),String.valueOf(data[0]),"메모용");}
                    else{mAdapter.addItem(data[8],String.valueOf(data[0]),String.valueOf(data[0]),String.valueOf(data[0]),String.valueOf(data[0]),"메모용");}
                }//메모용
                else if(data.length==12&&(Integer.valueOf(data[date])==1))
                {
                    tempSchedule[n]=str;
                    n++;
                    flag=false;
                    mAdapter = new ListViewAdapter(this);
                    mListView.setAdapter(mAdapter);
                    mAdapter.addItem(data[11],String.valueOf(data[7]),String.valueOf(data[8]),String.valueOf(data[9]),String.valueOf(data[10]),"");
                }//매주
                else if(Integer.valueOf(data[0])%1000==Integer.valueOf(dayData[2])%1000&&Integer.valueOf(data[1])==Integer.valueOf(dayData[1])&&Integer.valueOf(data[2])==Integer.valueOf(dayData[0]))
                {
                    tempSchedule[n]=str;
                    n++;
                    flag=false;
                    mAdapter = new ListViewAdapter(this);
                    mListView.setAdapter(mAdapter);
                    mAdapter.addItem(data[7],String.valueOf(data[3]),String.valueOf(data[4]),String.valueOf(data[5]),String.valueOf(data[6]),"");
                }//날짜
            }
            if(flag)
            {
                mAdapter = new ListViewAdapter(this);
                mListView.setAdapter(mAdapter);
                mAdapter.addItem("","","","","","일정이없습니다");
            }
        }
        catch (FileNotFoundException e) {
            Toast.makeText(getApplicationContext(),"aYa 캘린더 설치 감사합니다!\n일정을 추가해주세요",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),"읽기못함",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
};
