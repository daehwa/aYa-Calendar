package com.borax12.materialdaterangepickerexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.borax12.materialdaterangepicker.floatingactionbutton.FloatingActionButton;
import com.borax12.materialdaterangepicker.time.RadialPickerLayout;
import com.borax12.materialdaterangepicker.time.TimePickerDialog;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by snote on 2016-05-23.
 */
public class TimeSlotActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener{
    ArrayList<String> items;
    ArrayAdapter<String> adapter;
    ListView listFriends;
    int [] num=new int [6];
    boolean flag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_time_slot);

        items=new ArrayList<String>();
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,items);
        listFriends=(ListView)findViewById(R.id.friends);
        listFriends.setAdapter(adapter);
        listFriends.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        Button dateSelect=(Button)findViewById(R.id.dateSelect);
        FloatingActionButton findTime = (FloatingActionButton) findViewById(R.id.findTime);
        findTime.attachToListView(listFriends);


        //list 생성
        final String [] title=getTitleList();
        for(int i=0;i<title.length;i++){
            items.add(title[i]);
            adapter.notifyDataSetChanged();
        }

        //날짜범위설정
        dateSelect.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                    Calendar now = Calendar.getInstance();
                    DatePickerDialog dpd = com.borax12.materialdaterangepicker.date.DatePickerDialog.newInstance(
                            TimeSlotActivity.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                    dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        //찾기
        findTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimeSlotActivity.this, TimeSlotPrintActivity.class);
                long []isChecked=listFriends.getCheckItemIds();
                final String[] checked =new String[title.length];
                int n=0;
                for(int i=0;i<title.length;i++)
                    if(isChecked.length>0) {
                        checked[n] = title[(int)isChecked[i]];
                        n++;
                    }
                intent.putExtra("checked",checked);
                if(isChecked.length>0) {
                    intent.putExtra("num",num);
                    if(flag==false)
                        Toast.makeText(getApplicationContext(),"날짜를 설정해 주세요",Toast.LENGTH_SHORT).show();
                    else if((num[0]<num[3]||(num[0]==num[3]&&num[1]<num[4])||(num[0]==num[3]&&num[1]==num[4]&&num[2]<=num[5]))&&flag)
                        startActivity(intent);
                    else
                        Toast.makeText(getApplicationContext(),"날짜가 잘못설정되었습니다",Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(),"사람을 선택해 주세요",Toast.LENGTH_SHORT).show();
            }
        });
    }
    //날짜 from to
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth,int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        num[0]=year;
        num[1]=++monthOfYear;
        num[2]=dayOfMonth;
        num[3]=yearEnd;
        num[4]=++monthOfYearEnd;
        num[5]=dayOfMonthEnd;
        flag=true;
    }
    //time은 필요없는데 있어야 인터페이스 쓸수 있어서
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int hourOfDayEnd, int minuteEnd) {}


    //파일이름가져오기
    private String[] getTitleList() //알아 보기 쉽게 메소드 부터 시작합니다.
    {
        try
        {
            FilenameFilter fileFilter = new FilenameFilter()  //이부분은 특정 확장자만 가지고 오고 싶을 경우 사용하시면 됩니다.
            {
                public boolean accept(File dir, String name)
                {
                    return name.endsWith("txt"); //이 부분에 사용하고 싶은 확장자를 넣으시면 됩니다.
                } //end accept
            };
            File file = new File(getFilesDir().getAbsolutePath()); //경로를 SD카드로 잡은거고 그 안에 있는 A폴더 입니다. 입맛에 따라 바꾸세요.
            File[] files = file.listFiles(fileFilter);//위에 만들어 두신 필터를 넣으세요. 만약 필요치 않으시면 fileFilter를 지우세요.
            String [] titleList = new String [files.length]; //파일이 있는 만큼 어레이 생성했구요
            for(int i = 0;i < files.length;i++)
            {
                titleList[i] = files[i].getName();	//루프로 돌면서 어레이에 하나씩 집어 넣습니다.
            }//end for
            return titleList;
        } catch( Exception e )
        {
            return null;
        }//end catch()
    }//end getTitleList
}
