package com.borax12.materialdaterangepickerexample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

/**
 * Created by snote on 2016-05-23.
 */
public class ManageActivity extends AppCompatActivity {
   /* private KakaoLink kakaoLink;
    private KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder;
    private final String imgSrcLink = getResources().getDrawable(R.mipmap.logo).toString();*/
    private ImageButton SendBtn;
    private EditText scheduleFriend;
    private Button addFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState){
       super.onCreate(savedInstanceState);
        setContentView(R.layout.management);
        SendBtn=(ImageButton)findViewById(R.id.kakao_message);
        final EditText Name=(EditText)findViewById(R.id.name);
        scheduleFriend=(EditText)findViewById(R.id.schedule_friend);
        addFriend=(Button)findViewById(R.id.add_friend);

        addFriend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String name=Name.getText().toString();
                if(name.isEmpty())
                    Toast.makeText(getApplicationContext(),"친구이름을 입력해주세요",Toast.LENGTH_SHORT).show();
                else
                {
                    String scheduleFriendString=scheduleFriend.getText().toString();
                    if(scheduleFriendString.isEmpty()) Toast.makeText(getApplicationContext(),"친구일정을 입력해주세요",Toast.LENGTH_SHORT).show();
                    else{
                        if(scheduleFriendString.length()<8) Toast.makeText(getApplicationContext(),"잘못된 형식의 일정입니다",Toast.LENGTH_SHORT).show();
                        else{
                            try {
                                OutputStream os=openFileOutput(name+".txt", Context.MODE_PRIVATE);
                                BufferedWriter bOs=new BufferedWriter(new OutputStreamWriter(os));
                                bOs.write(scheduleFriend.getText().toString());
                                bOs.close();
                                Toast.makeText(getApplicationContext(),name+"님이 추가되었습니다",Toast.LENGTH_SHORT).show();

                                //Intent
                                Intent intent=new Intent(ManageActivity.this,homeActivity.class);
                                startActivity(intent);
                                finish();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });

        SendBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                File files=null;
                String str_name=Name.getText().toString();
                if(!str_name.isEmpty()) {
                    files = new File(getFilesDir().getAbsolutePath(), str_name + ".txt");
                    if(files.exists()==true)
                    {
                        Intent intentSend=new Intent(android.content.Intent.ACTION_SEND);
                        intentSend.setPackage("com.kakao.talk");
                        intentSend.setType("text/plain");

                        String str="",strTemp;
                        InputStream is= null;
                        try {
                            is = openFileInput(str_name+".txt");
                            BufferedReader bIn=new BufferedReader(new InputStreamReader(is,"euc-kr"));
                            while((strTemp=bIn.readLine())!=null)
                                str+=strTemp+"\r\n";

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }catch (IOException e) {
                            e.printStackTrace();
                        }

                        //intentSend.putExtra(Intent.EXTRA_STREAM, Uri.parse(getFilesDir().getAbsolutePath()+"/"+str_name+".txt"));
                        intentSend.putExtra(Intent.EXTRA_TEXT, str);
                        startActivity(Intent.createChooser(intentSend,"일정 보내기"));
                    }
                    else{
                        Toast.makeText(getApplicationContext(), str_name+"님의 정보가 없습니다", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(ManageActivity.this, "사람을 입력해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
