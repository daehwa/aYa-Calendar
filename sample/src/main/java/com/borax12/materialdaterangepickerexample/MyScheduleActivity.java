package com.borax12.materialdaterangepickerexample;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.borax12.materialdaterangepicker.floatingactionbutton.FloatingActionButton;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;


/**
 * Created by snote on 2016-05-07.
 */

public class MyScheduleActivity extends AppCompatActivity
{
    private ListView mListView = null;
    private ListViewAdapter mAdapter = null;
    public static Activity AActivity;
    private ArrayList<ListData> mListData = new ArrayList<ListData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule);

        /*android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(0xEEEEEEEE));*/

        AActivity = MyScheduleActivity.this;
        mListView = (ListView) findViewById(R.id.mListView);
        //read file
        try
        {
            InputStream is=openFileInput("aYaUserData.txt");
            BufferedReader bIn=new BufferedReader(new InputStreamReader(is,"euc-kr"));

            String str;
            String [] data;
            while((str=bIn.readLine())!=null)
            {
                System.out.println(str);
                data=str.split("::");
                if(Integer.valueOf(data[0])==-2)
                {
                    mAdapter = new ListViewAdapter(this);
                    mListView.setAdapter(mAdapter);
                    if(data.length==8){mAdapter.addItem(data[7],String.valueOf(data[0]),String.valueOf(data[0]),String.valueOf(data[0]),String.valueOf(data[0]),"메모용");}
                    else{mAdapter.addItem(data[8],String.valueOf(data[0]),String.valueOf(data[0]),String.valueOf(data[0]),String.valueOf(data[0]),"메모용");}
                }//메모용
                else if(data.length==12)
                {
                    mAdapter = new ListViewAdapter(this);
                    mListView.setAdapter(mAdapter);
                    mAdapter.addItem(data[11],String.valueOf(data[7]),String.valueOf(data[8]),String.valueOf(data[9]),String.valueOf(data[10]),"");
                }//매주
                else
                {
                    mAdapter = new ListViewAdapter(this);
                    mListView.setAdapter(mAdapter);
                    mAdapter.addItem(data[7],String.valueOf(data[3]),String.valueOf(data[4]),String.valueOf(data[5]),String.valueOf(data[6]),"");
                }//날짜
            }
        }
        catch (FileNotFoundException e) {
            Toast.makeText(getApplicationContext(),"aYa 캘린더 설치 감사합니다!\n일정을 추가해주세요",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),"읽기못함",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

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
}

