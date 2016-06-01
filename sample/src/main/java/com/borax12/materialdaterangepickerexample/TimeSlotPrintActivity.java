package com.borax12.materialdaterangepickerexample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.borax12.materialdaterangepicker.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by snote on 2016-05-24.
 */
public class TimeSlotPrintActivity extends AppCompatActivity {
    ArrayList<String> items;
    ArrayAdapter<String> adapter;
    ListView listTimeSlot;
    EditText nameOfSchedule;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_time_slot_print);

        items = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, items);
        listTimeSlot = (ListView) findViewById(R.id.time_slot);
        listTimeSlot.setAdapter(adapter);
        listTimeSlot.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        FloatingActionButton findTime = (FloatingActionButton) findViewById(R.id.add_time_for_all);
        findTime.attachToListView(listTimeSlot);
        nameOfSchedule = (EditText)findViewById(R.id.name_of_schedule);

        Intent intent = getIntent();
        String[] checked = intent.getStringArrayExtra("checked");
        int[] num = intent.getIntArrayExtra("num");
        findTimeForALL(checked, num);

        findTime.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String name;
                name=nameOfSchedule.getText().toString();
                System.out.println("name: "+name);
                String selectedTitle;
                if(!name.isEmpty()) {
                    int isChecked = listTimeSlot.getCheckedItemPosition();
                    if(listTimeSlot.isItemChecked(isChecked)) {
                        selectedTitle = items.get(isChecked);
                        selectedTitle = selectedTitle.replaceAll("\\D+", "::");
                        System.out.println(selectedTitle);
                        if(selectedTitle.length()==12||selectedTitle.length()==13||selectedTitle.length()==14){Toast.makeText(getApplicationContext(), "이 일정은 추가할 수 없습니다", Toast.LENGTH_SHORT).show();}
                        else {
                            selectedTitle += name;
                            try {
                                OutputStream os = openFileOutput("aYaUserData.txt", Context.MODE_APPEND);
                                BufferedWriter bOut = new BufferedWriter(new OutputStreamWriter(os));
                                bOut.write(selectedTitle+"\r\n");
                                bOut.close();
                                Toast.makeText(getApplicationContext(), "일정이 저장되었습니다", Toast.LENGTH_SHORT).show();

                                //화면옮긺
                                Intent intent=new Intent(TimeSlotPrintActivity.this,homeActivity.class);
                                startActivity(intent);
                                //이전 종료
                                homeActivity aActivity = (homeActivity) homeActivity.AActivity;
                                aActivity.finish();
                                finish();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else{Toast.makeText(getApplicationContext(),"일정을 선택해주세요",Toast.LENGTH_SHORT).show();}
                }
                else{Toast.makeText(getApplicationContext(),"일정 이름을 입력하세요",Toast.LENGTH_SHORT).show();}
            }
        });
    }

    public class personSchedule {
        public int[][] day;
        public int[][] date;
        public int[][] time1;
        public int[][] time2;
        public String[] why = null;

        public void initial(int n) {
            day = new int[n][3];
            date=new int[n][7];
            time1 = new int[n][2];
            time2 = new int[n][2];
            why = new String[n];
        }
    }

    public void findTimeForALL(String[] checked, int[] num) {
        personSchedule[] person = new personSchedule[checked.length];
        System.out.println("길이길이: "+checked.length);
        for(int i=0;i<checked.length;i++)
            person[i]=new personSchedule();
        int i = -1;
        while (++i < checked.length) {
            String temp;
            int number = 0;
            try {
                InputStream is = openFileInput(checked[i]);
                BufferedReader bIn = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                while ((temp = bIn.readLine()) != null) {
                    String[]data=temp.split("::");
                    if(Integer.valueOf(data[0])!=-2)
                        number++;
                }
                person[i].initial(number);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
            //몇일을 계산할건지(윤년도 고려완료~)
            int Y = num[0], M = num[1], D = num[2], days = 0;
            int date = (num[2] + ((num[1] + 1) * 26) / 10 + num[0] % 100 + (num[0] % 100) / 4 + num[0] / 400 - 2 * num[0] / 100 + 5) % 7;
            System.out.println("date: "+date);
            while (num[0] != num[3]) {
                if (num[0] % 4 == 0 && num[0] % 400 != 0)
                    days += 366;
                else
                    days += 365;
                num[0]++;
            }
            num[0] = Y;
            while (num[1] != num[4]) {
                if (num[1] == 2 && (Y%4==0&&Y%100!=0||Y%400==0))
                    days += 29;
                else if (Y == 2)
                    days += 28;
                else if ((M % 2 == 0 && M < 7) || (M % 2 == 1 &&M > 7))
                    days += 30;
                else
                    days += 31;
                num[1]++;
                if (num[1] == 13)
                    num[1] = 1;
            }
            days = days + num[5] - num[2] + 1;

            //사람정보입력
            i = -1;
            while (++i < checked.length) {
                int j=0;
                try {
                    InputStream is = openFileInput(checked[i]);
                    BufferedReader bIn = new BufferedReader(new InputStreamReader(is, "euc-kr"));
                    String str;
                    String[] data;
                    while ((str = bIn.readLine()) != null) {
                        data = str.split("::");
                        int k=0;
                        if (Integer.valueOf(data[0])== -2) {
                            continue;
                        }
                        else if (Integer.valueOf(data[0]) == -1 || Integer.valueOf(data[0]) == 1) {
                            for(k=0;k<7;k++)
                                person[i].date[j][k]=Integer.valueOf(data[k]);
                        }
                        else
                            for(k=0;k<3;k++)
                                person[i].day[j][k]=Integer.valueOf(data[k]);
                        person[i].time1[j][0]=Integer.valueOf(data[k]); k++; person[i].time1[j][1]=Integer.valueOf(data[k]); k++;
                        person[i].time2[j][0]=Integer.valueOf(data[k]); k++; person[i].time2[j][1]=Integer.valueOf(data[k]); k++;
                        person[i].why[j]=data[k];
                        System.out.println(person[i].why[j]);
                        j++;
                    }
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), checked[i] + "님의 정보가 없습니다", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    Toast.makeText(getApplicationContext(), checked[i] + "님의 정보 접근 에러", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), checked[i] + "님의 정보 접근 에러", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
           int all=0;
           while(all!=days)
            {
                //맞는 날짜에 집어넣기
              all++;
                int [][]time1=new int[100][2], time2=new int [100][2], Time1=new int[100][2], Time2=new int[100][2];
                int t,n=1,cnt=0,lastday=0,lastyear;
                for(i=0;i<checked.length;i++){
                    for(int j=0;j<person[i].why.length;j++){
                        if(M==2&&(Y%4==0&&Y%100!=0||Y%400==0)) lastday=29;
                        else if(M==2) lastday=28;
                        else if(M%2==0&&M<7||M%2==1&&M>7) lastday=30;
                        else lastday=31;

                        if(Y%4==0&&Y%400!=0) lastyear=366;
                        else lastyear=365;
                        if(person[i].day[j][0]==Y&&person[i].day[j][1]==M&&person[i].day[j][2]==D)
                        {
                            time1[cnt][0]=person[i].time1[j][0];
                            time1[cnt][1]=person[i].time1[j][1];
                            time2[cnt][0]=person[i].time2[j][0];
                            time2[cnt][1]=person[i].time2[j][1];
                            cnt++;
                            System.out.println(time1[cnt][0]+" "+time1[cnt][1]);
                        }
                        if(person[i].date[j][(all+date-1)%7]==1)
                        {
                            time1[cnt][0]=person[i].time1[j][0];
                            time1[cnt][1]=person[i].time1[j][1];
                            time2[cnt][0]=person[i].time2[j][0];
                            time2[cnt][1]=person[i].time2[j][1];
                            cnt++;
                        }
                    }
                }
                //일정을 비교해서 정리
               for(int j=0;j<cnt;j++)
                {
                    for(i=0;i<cnt-j-1;i++)
                    {
                        if(time1[i][0]*100+time1[i][1]>time1[i+1][0]*100+time1[i+1][1])
                        {
                            t=time1[i+1][0];
                            time1[i+1][0]=time1[i][0];
                            time1[i][0]=t;
                            t=time1[i+1][1];
                            time1[i+1][1]=time1[i][1];
                            time1[i][1]=t;

                            t=time2[i+1][0];
                            time2[i+1][0]=time2[i][0];
                            time2[i][0]=t;
                            t=time2[i+1][1];
                            time2[i+1][1]=time2[i][1];
                            time2[i][1]=t;
                        }
                    }
                }//여기까지 순서대로 배열해주는 애였음

                Time2[0][0]=0;
                Time2[0][1]=0;
                Time1[0][0]=23;
                Time1[0][1]=59;

                Time1[1][0]=time1[0][0];
                Time1[1][1]=time1[0][1];
                Time2[1][0]=time2[0][0];
                Time2[1][1]=time2[0][1];
                for(i=1;i<cnt;i++)
                {
                    if(time1[i][0]*100+time1[i][1]>Time2[n][0]*100+Time2[n][1])
                    {
                        n++;
                        Time1[n][0]=time1[i][0];
                        Time1[n][1]=time1[i][1];
                        Time2[n][0]=time2[i][0];
                        Time2[n][1]=time2[i][1];
                    }
                    else
                    {
                        if(time2[i][0]*100+time2[i][1]>Time2[n][0]*100+Time2[n][0])
                        {
                            Time2[n][0]=time2[i][0];
                            Time2[n][1]=time2[i][1];
                        }
                    }
                }

                //이제 출력!!
                boolean flag=false;
                String title=Y+"년 "+M+"월 "+D+"일";
                System.out.println("year: "+Y);
                if(Time1[n][0]==0&&Time1[n][1]==0&&Time2[n][0]==23&&Time2[n][1]==59)
                    title+="\r\n가능한 시간이 없습니다";
                else if(cnt==0)
                {
                    title+="\r\n모든 시간이 가능합니다";
                    if(D==lastday)
                    {
                        M++;
                        D=1;
                        if(M==13)
                        {
                            Y++;
                            M=1;
                        }
                    }
                    else
                        D++;
                    items.add(title);
                    adapter.notifyDataSetChanged();
                    continue;
                }
                else
                    for(i=1;i<=n+1;i++)
                    {
                        if(Time1[1][0]==0&&Time1[1][1]==0&&i==1)
                            continue;
                        title+="\r\n"+Time2[i-1][0]+"시 "+Time2[i-1][1]+"분 ~ ";
                        if(i==n+1)
                        {
                            i=0;
                            flag=true;
                        }
                        title+=+Time1[i][0]+"시 "+Time1[i][1]+"분";
                        if(flag)
                            break;
                        if(Time2[i][0]==23&&Time2[i][1]==59)
                            break;
                        items.add(title);
                        adapter.notifyDataSetChanged();
                        title=Y+"년 "+M+"월 "+D+"일";
                    }
                items.add(title);
                adapter.notifyDataSetChanged();
                if(D==lastday)
                {
                    M++;
                    D=1;
                    if(M==13)
                    {
                        Y++;
                        M=1;
                    }
                }
                else
                    D++;
            }
        }
    }