package com.borax12.materialdaterangepickerexample;

/**
 * Created by snote on 2016-05-23.
 */
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

/**
 * Created by snote on 2015-07-24.
 */
@SuppressWarnings("deprecation")
public class homeActivity extends TabActivity{
    public static Activity AActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        AActivity=homeActivity.this;

        TabHost mTab = getTabHost();
        mTab.addTab(mTab.newTabSpec("DateTab")
                .setIndicator("",getResources().getDrawable(R.drawable.date2))
                .setContent(new Intent(this,MyScheduleActivity.class)));

        mTab.addTab(mTab.newTabSpec("TimeSlot")
                .setIndicator("",getResources().getDrawable(R.drawable.friend))
                .setContent(new Intent(this,TimeSlotActivity.class)));

        mTab.addTab(mTab.newTabSpec("Manage")
                .setIndicator("",getResources().getDrawable(R.drawable.management))
                .setContent(new Intent(this,ManageActivity.class)));

        // mTab.setCurrentTab(1);
    }
}
