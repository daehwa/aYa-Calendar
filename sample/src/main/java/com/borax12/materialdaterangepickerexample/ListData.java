package com.borax12.materialdaterangepickerexample;

import java.text.Collator;
import java.util.Comparator;

/**
 * Created by snote on 2016-05-08.
 */
public class ListData {
    public String mTitle;
    public String startHour;
    public String startMin;
    public String endHour;
    public String endMin;
    public String memo;

    public static final Comparator<ListData> NUM_COMPARATOR = new Comparator<ListData>() {
        private final Collator sCollator = Collator.getInstance();

        @Override
        public int compare(ListData mListDate_1, ListData mListDate_2) {
            return sCollator.compare(mListDate_1.startHour, mListDate_2.startHour);
        }
    };
}
